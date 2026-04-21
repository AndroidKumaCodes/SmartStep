package com.akcrba.smartstep.feature.home.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.akcrba.smartstep.feature.home.presentation.composables.BottomSheetType
import com.akcrba.smartstep.feature.home.presentation.composables.SmartStepBottomSheet
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun PermissionHandler(context: Context) {
    val activityRecognitionState = rememberPermissionState(Manifest.permission.ACTIVITY_RECOGNITION)
    val isIgnoringOptimizations = rememberIsIgnoringBatteryOptimizations(context)

    var hasRequestedPermission by rememberSaveable { mutableStateOf(false) }
    var isSystemDialogVisible by rememberSaveable { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {
            isSystemDialogVisible = false
            hasRequestedPermission = true
        },
    )

    // Vereinfachte Status-Prüfungen
    val status = activityRecognitionState.status
    val isGranted = status.isGranted
    val isDenied = status is PermissionStatus.Denied
    val shouldShowRationale = isDenied && (status as PermissionStatus.Denied).shouldShowRationale

    // 1. Initialer Trigger für den System-Dialog
    LaunchedEffect(status) {
        if (!isGranted && !hasRequestedPermission && !isSystemDialogVisible) {
            isSystemDialogVisible = true
            permissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
        }
    }

    // 2. Deklaratives Darstellen der UI basierend auf dem flachen State
    when {
        // Fall 1: Wir warten noch auf den Nutzer/Dialog (BottomSheet pausieren)
        !isGranted && (!hasRequestedPermission || isSystemDialogVisible) -> Unit

        // Fall 2: Verweigert, aber Rationale zeigen (Motion Sensor Content)
        !isGranted && shouldShowRationale -> {
            SmartStepBottomSheet(
                type = BottomSheetType.MotionSensor,
                onDismissRequest = { /* Handle dismiss */ },
                onAction = {
                    isSystemDialogVisible = true
                    permissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
                }
            )
        }

        // Fall 3: Dauerhaft verweigert / Don't ask again (Manual Access via Einstellungen)
        !isGranted && !shouldShowRationale -> {
            SmartStepBottomSheet(
                type = BottomSheetType.ManualAccess,
                onDismissRequest = { /* Handle dismiss */ },
                onAction = { context.openAppSettings() }
            )
        }

        // Fall 4: Motion Sensor ist aktiv, jetzt Hintergrund-Berechtigung abfragen
        !isIgnoringOptimizations -> {
            SmartStepBottomSheet(
                type = BottomSheetType.BackgroundAccess,
                onDismissRequest = { /* Handle dismiss */ },
                onAction = { context.requestIgnoreBatteryOptimizations() }
            )
        }
    }
}

// --- HILFSFUNKTIONEN ---

/** Eigenes "remember", das sich automatisch um den Lifecycle und PowerManager kümmert. */
@Composable
private fun rememberIsIgnoringBatteryOptimizations(context: Context): Boolean {
    val powerManager = remember { context.getSystemService(Context.POWER_SERVICE) as PowerManager }
    var isIgnoring by remember { mutableStateOf(powerManager.isIgnoringBatteryOptimizations(context.packageName)) }
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                isIgnoring = powerManager.isIgnoringBatteryOptimizations(context.packageName)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
    return isIgnoring
}

private fun Context.openAppSettings() {
    startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = android.net.Uri.fromParts("package", packageName, null)
    })
}

@SuppressLint("BatteryLife")
private fun Context.requestIgnoreBatteryOptimizations() {
    startActivity(Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
        data = android.net.Uri.fromParts("package", packageName, null)
    })
}
