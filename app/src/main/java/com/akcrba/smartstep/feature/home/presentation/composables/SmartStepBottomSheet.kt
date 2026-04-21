package com.akcrba.smartstep.feature.home.presentation.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

internal sealed interface BottomSheetType {
    data object MotionSensor : BottomSheetType
    data object ManualAccess : BottomSheetType
    data object BackgroundAccess : BottomSheetType
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SmartStepBottomSheet(
    type: BottomSheetType,
    onDismissRequest: () -> Unit,
    onAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        // Only the 3rd screenshot shows a drag handle
        dragHandle = {
            if (type == BottomSheetType.BackgroundAccess) {
                BottomSheetDefaults.DragHandle()
            }
        },
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(
                    top = if (type == BottomSheetType.BackgroundAccess) 0.dp else 16.dp,
                    bottom = 48.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            when (type) {
                BottomSheetType.MotionSensor -> MotionSensorContent(onAction)
                BottomSheetType.ManualAccess -> ManualAccessContent(onAction)
                BottomSheetType.BackgroundAccess -> BackgroundAccessContent(onAction)
            }
        }
    }
}

@Composable
private fun MotionSensorContent(onAction: () -> Unit) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.Place, // Replace with your exact icon if available
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface
        )
    }

    Spacer(modifier = Modifier.height(24.dp))

    Text(
        text = "To count your steps,\nSmart Step needs access to your\nmotion sensors.",
        style = MaterialTheme.typography.titleMedium,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onSurface
    )

    Spacer(modifier = Modifier.height(32.dp))

    BottomSheetActionButton(text = "Allow access", onClick = onAction)
}

@Composable
private fun ManualAccessContent(onAction: () -> Unit) {
    Text(
        text = "Enable access manually",
        style = MaterialTheme.typography.titleMedium,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onSurface
    )

    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = "To track your steps, please enable the\npermission in your device settings.",
        style = MaterialTheme.typography.bodyMedium,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Spacer(modifier = Modifier.height(32.dp))

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = "1. Open Permissions", style = MaterialTheme.typography.bodyMedium)
        Text(text = "2. Tap Physical activity", style = MaterialTheme.typography.bodyMedium)
        Text(text = "3. Select Allow", style = MaterialTheme.typography.bodyMedium)
    }

    Spacer(modifier = Modifier.height(32.dp))

    BottomSheetActionButton(text = "Open settings", onClick = onAction)
}

@Composable
private fun BackgroundAccessContent(onAction: () -> Unit) {
    Text(
        text = "Background access recommended",
        style = MaterialTheme.typography.titleMedium,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onSurface
    )

    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = "Background access helps Smart Step track your\nactivity more reliably.",
        style = MaterialTheme.typography.bodyMedium,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Spacer(modifier = Modifier.height(32.dp))

    BottomSheetActionButton(text = "Continue", onClick = onAction)
}

@Composable
private fun BottomSheetActionButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, backgroundColor = 0xFF8A8A8A) // Gray background to simulate the dimmed scrim
@Composable
private fun MotionSensorBottomSheetPreview() {
    MaterialTheme {
        SmartStepBottomSheet(
            type = BottomSheetType.MotionSensor,
            onDismissRequest = {},
            onAction = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, backgroundColor = 0xFF8A8A8A)
@Composable
private fun ManualAccessBottomSheetPreview() {
    MaterialTheme {
        SmartStepBottomSheet(
            type = BottomSheetType.ManualAccess,
            onDismissRequest = {},
            onAction = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, backgroundColor = 0xFF8A8A8A)
@Composable
private fun BackgroundAccessBottomSheetPreview() {
    MaterialTheme {
        SmartStepBottomSheet(
            type = BottomSheetType.BackgroundAccess,
            onDismissRequest = {},
            onAction = {}
        )
    }
}

