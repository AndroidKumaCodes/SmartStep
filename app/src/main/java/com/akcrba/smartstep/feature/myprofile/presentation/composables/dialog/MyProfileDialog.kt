package com.akcrba.smartstep.feature.myprofile.presentation.composables.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.akcrba.smartstep.R
import com.akcrba.smartstep.app.ui.theme.SmartStepTheme
import com.akcrba.smartstep.app.ui.theme.bodyLargeMedium
import com.akcrba.smartstep.app.ui.theme.bodyMediumRegular
import com.akcrba.smartstep.feature.myprofile.presentation.MyProfileAction
import com.akcrba.smartstep.feature.myprofile.presentation.model.BodyStats
import com.akcrba.smartstep.feature.myprofile.presentation.model.DialogType
import com.akcrba.smartstep.feature.myprofile.presentation.model.Height
import com.akcrba.smartstep.feature.myprofile.presentation.model.HeightItems
import com.akcrba.smartstep.feature.myprofile.presentation.model.Weight
import com.akcrba.smartstep.feature.myprofile.presentation.model.WeightItems
import com.akcrba.smartstep.feature.myprofile.presentation.model.WheelPickerData

// --- Configuration Constants ---
private val contentPadding = 24.dp
private val NoopInt: (Int) -> Unit = {}

@Composable
internal fun MyProfileDialog(
    bodyStats: BodyStats,
    wheelPickerData: WheelPickerData,
    onAction: (MyProfileAction) -> Unit,
    onIsMetricSystemChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isMetric = bodyStats.isMetric

    val type = when (wheelPickerData) {
        is WheelPickerData.Height -> DialogType.HEIGHT
        is WheelPickerData.Weight -> DialogType.WEIGHT
    }

    val titleRes = when (type) {
        DialogType.HEIGHT -> R.string.my_profile_dialog_label_height
        DialogType.WEIGHT -> R.string.my_profile_dialog_label_weight
    }

    Dialog(onDismissRequest = { onAction(MyProfileAction.Dialog.OnClickCancel) }) {
        Column(
            modifier = modifier
                .widthIn(max = 328.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Header
            Column(Modifier.padding(contentPadding)) {
                Text(
                    text = stringResource(titleRes),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.my_profile_dialog_subtitle),
                    style = MaterialTheme.typography.bodyMediumRegular,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start,
                )
                Spacer(modifier = Modifier.height(16.dp))
                MyProfileDialogSegmentedButton(
                    isMetricSystem = isMetric,
                    unitMetric = stringResource(type.unitMetric),
                    unitImperial = stringResource(type.unitImperial),
                    onIsMetricSystemChange = onIsMetricSystemChange,
                )
            }

            val onSingleValueChangeInternal: (Int) -> Unit = remember(type, isMetric, onAction) {
                if (type == DialogType.WEIGHT || (type == DialogType.HEIGHT && isMetric)) {
                    { value ->
                        when (type) {
                            DialogType.WEIGHT -> onAction(
                                MyProfileAction.Dialog.UpdateInterimWeight(
                                    if (isMetric) Weight.fromMetric(value) else Weight.fromImperial(value),
                                ),
                            )

                            DialogType.HEIGHT -> onAction(
                                MyProfileAction.Dialog.UpdateInterimHeight(
                                    Height.fromMetric(
                                        value,
                                    ),
                                ),
                            )
                        }
                    }
                } else {
                    NoopInt
                }
            }

            val onDoubleValueChangeInternal: (Int, Int) -> Unit = remember(type, isMetric, onAction) {
                { ft, inch ->
                    // Double wheel is only used for imperial height
                    if (type == DialogType.HEIGHT && !isMetric) {
                        onAction(MyProfileAction.Dialog.UpdateInterimHeight(Height.fromImperial(ft, inch)))
                    }
                }
            }

            MyProfileDialogWheelPicker(
                wheelPickerData = wheelPickerData,
                bodyStats = bodyStats,
                onSingleValueChange = onSingleValueChangeInternal,
                onDoubleValueChange = onDoubleValueChangeInternal,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(contentPadding),
                horizontalArrangement = Arrangement.End,
            ) {
                TextButton(
                    onClick = {
                        onAction(MyProfileAction.Dialog.OnClickCancel)
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary,
                    ),
                ) {
                    Text(
                        text = stringResource(R.string.my_profile_dialog_cancel),
                        style = MaterialTheme.typography.bodyLargeMedium,
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(
                    onClick = {
                        onAction(MyProfileAction.Dialog.OnClickOk(type = type))
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary,
                    ),
                ) {
                    Text(
                        text = stringResource(R.string.my_profile_dialog_ok),
                        style = MaterialTheme.typography.bodyLargeMedium,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun MyProfileWeightDialogPreview() {
    SmartStepTheme {
        MyProfileDialog(
            bodyStats = BodyStats(
                height = Height(cm = 175, ft = 5, inch = 9),
                interimHeight = Height(cm = 175, ft = 5, inch = 9),
                weight = Weight(kg = 65, lbs = 143),
                interimWeight = Weight(kg = 65, lbs = 143),
                isMetric = true,
            ),
            wheelPickerData = WheelPickerData.Weight(
                items = WeightItems(),
            ),
            onAction = {},
            onIsMetricSystemChange = {},
        )
    }
}

@Preview
@Composable
private fun MyProfileHeightDialogPreview() {
    SmartStepTheme {
        MyProfileDialog(
            bodyStats = BodyStats(
                height = Height(cm = 175, ft = 5, inch = 9),
                interimHeight = Height(cm = 175, ft = 5, inch = 9),
                weight = Weight(kg = 65, lbs = 143),
                interimWeight = Weight(kg = 65, lbs = 143),
                isMetric = true,
            ),
            wheelPickerData = WheelPickerData.Height(
                items = HeightItems(),
            ),
            onAction = {},
            onIsMetricSystemChange = {},
        )
    }
}
