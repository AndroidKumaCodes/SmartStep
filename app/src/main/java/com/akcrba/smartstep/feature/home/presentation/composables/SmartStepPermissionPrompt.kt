package com.akcrba.smartstep.feature.home.presentation.composables

import androidx.annotation.StringRes
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.window.core.layout.WindowSizeClass
import com.akcrba.smartstep.R
import com.akcrba.smartstep.app.ui.theme.SmartStepTheme
import com.akcrba.smartstep.app.ui.theme.bodyLargeMedium
import com.akcrba.smartstep.app.ui.theme.bodyLargeRegular
import com.akcrba.smartstep.app.ui.theme.bodyMediumRegular

internal enum class PermissionPromptType(
    val hasCloseControls: Boolean,
    @get:StringRes val actionButtonText: Int,
) {
    MotionSensor(hasCloseControls = false, actionButtonText = R.string.smart_step_permission_allow_access),
    ManualAccess(hasCloseControls = false, actionButtonText = R.string.smart_step_permission_open_settings),
    BackgroundAccess(hasCloseControls = true, actionButtonText = R.string.smart_step_permission_continue),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SmartStepPermissionPrompt(
    type: PermissionPromptType,
    onDismissRequest: () -> Unit,
    onAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isExpanded = currentWindowAdaptiveInfo()
        .windowSizeClass
        .isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)

    if (isExpanded) {
        SmartStepDialog(
            type = type,
            onDismissRequest = onDismissRequest,
            onAction = onAction,
            modifier = modifier,
        )
    } else {
        SmartStepModalBottomSheet(
            type = type,
            onDismissRequest = onDismissRequest,
            onAction = onAction,
            modifier = modifier,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SmartStepModalBottomSheet(
    type: PermissionPromptType,
    onDismissRequest: () -> Unit,
    onAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        dragHandle = {
            if (type.hasCloseControls) {
                BottomSheetDefaults.DragHandle()
            }
        },
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = modifier,
    ) {
        SmartStepCommonContent(
            type = type,
            isExpanded = false,
            onAction = onAction,
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .padding(
                    top = if (type.hasCloseControls) 0.dp else 16.dp,
                    bottom = 48.dp,
                ),
        )
    }
}

@Composable
private fun SmartStepDialog(
    type: PermissionPromptType,
    onDismissRequest: () -> Unit,
    onAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = modifier.widthIn(max = 312.dp),
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                if (type.hasCloseControls) {
                    IconButton(
                        onClick = onDismissRequest,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }

                SmartStepCommonContent(
                    type = type,
                    isExpanded = true,
                    onAction = onAction,
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .padding(
                            top = if (type.hasCloseControls) 32.dp else 24.dp,
                            bottom = 24.dp,
                        ),
                )
            }
        }
    }
}

@Composable
private fun SmartStepCommonContent(
    type: PermissionPromptType,
    isExpanded: Boolean,
    onAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        when (type) {
            PermissionPromptType.MotionSensor -> MotionSensorContent(isExpanded)
            PermissionPromptType.ManualAccess -> ManualAccessContent(isExpanded)
            PermissionPromptType.BackgroundAccess -> BackgroundAccessContent()
        }

        Spacer(modifier = Modifier.height(32.dp))
        BottomSheetActionButton(
            text = stringResource(type.actionButtonText),
            onClick = onAction,
        )
    }
}

@Composable
private fun MotionSensorContent(
    isExpanded: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = RoundedCornerShape(12.dp),
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Outlined.Place, // Replace with your exact icon if available
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.smart_step_permission_motion_sensor_text),
            style = if (isExpanded) MaterialTheme.typography.bodyLargeRegular else MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun ManualAccessContent(
    isExpanded: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.smart_step_permission_manual_access_title),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.smart_step_permission_manual_access_text),
            style = if (isExpanded) {
                MaterialTheme.typography.bodyMediumRegular
            } else {
                MaterialTheme.typography.bodyLargeRegular
            },
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(32.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = stringResource(R.string.smart_step_permission_manual_access_bullet_one),
                style = MaterialTheme.typography.bodyLargeMedium,
            )
            Text(
                text = stringResource(R.string.smart_step_permission_manual_access_bullet_two),
                style = MaterialTheme.typography.bodyLargeMedium,
            )
            Text(
                text = stringResource(R.string.smart_step_permission_manual_access_bullet_three),
                style = MaterialTheme.typography.bodyLargeMedium,
            )
        }
    }
}

@Composable
private fun BackgroundAccessContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.smart_step_permission_background_access_title),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.smart_step_permission_background_access_text),
            style = MaterialTheme.typography.bodyLargeRegular,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun BottomSheetActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLargeMedium,
        )
    }
}

// --- SMARTPHONE PREVIEWS (ModalBottomSheet) ---

@Preview(
    name = "Phone: Motion Sensor",
    showBackground = true,
    backgroundColor = 0xFF8A8A8A,
)
@Composable
private fun MotionSensorPhonePreview() {
    SmartStepTheme {
        SmartStepPermissionPrompt(
            type = PermissionPromptType.MotionSensor,
            onDismissRequest = {},
            onAction = {},
        )
    }
}

@Preview(
    name = "Phone: Manual Access",
    showBackground = true,
    backgroundColor = 0xFF8A8A8A,
)
@Composable
private fun ManualAccessPhonePreview() {
    SmartStepTheme {
        SmartStepPermissionPrompt(
            type = PermissionPromptType.ManualAccess,
            onDismissRequest = {},
            onAction = {},
        )
    }
}

@Preview(
    name = "Phone: Background Access",
    showBackground = true,
    backgroundColor = 0xFF8A8A8A,
)
@Composable
private fun BackgroundAccessPhonePreview() {
    SmartStepTheme {
        SmartStepPermissionPrompt(
            type = PermissionPromptType.BackgroundAccess,
            onDismissRequest = {},
            onAction = {},
        )
    }
}

// --- TABLET PREVIEWS (Dialog) ---

@Preview(
    name = "Tablet: Motion Sensor",
    device = Devices.TABLET,
    showBackground = true,
    backgroundColor = 0xFF8A8A8A,
)
@Composable
private fun MotionSensorTabletPreview() {
    SmartStepTheme {
        SmartStepPermissionPrompt(
            type = PermissionPromptType.MotionSensor,
            onDismissRequest = {},
            onAction = {},
        )
    }
}

@Preview(
    name = "Tablet: Manual Access",
    device = Devices.TABLET,
    showBackground = true,
    backgroundColor = 0xFF8A8A8A,
)
@Composable
private fun ManualAccessTabletPreview() {
    SmartStepTheme {
        SmartStepPermissionPrompt(
            type = PermissionPromptType.ManualAccess,
            onDismissRequest = {},
            onAction = {},
        )
    }
}

@Preview(
    name = "Tablet: Background Access",
    device = Devices.TABLET,
    showBackground = true,
    backgroundColor = 0xFF8A8A8A,
)
@Composable
private fun BackgroundAccessTabletPreview() {
    SmartStepTheme {
        SmartStepPermissionPrompt(
            type = PermissionPromptType.BackgroundAccess,
            onDismissRequest = {},
            onAction = {},
        )
    }
}
