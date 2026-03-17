package com.buchwald.smartstep.feature.profilesetup.presentation.composables.dialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.buchwald.smartstep.app.ui.theme.SmartStepTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProfileSetupDialogSegmentedButton(
    isMetricSystem: Boolean,
    unitMetric: String,
    unitImperial: String,
    onIsMetricSystemChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val options = listOf(unitMetric, unitImperial)
    val selectedIndex = if (isMetricSystem) 0 else 1

    SingleChoiceSegmentedButtonRow(
        modifier = modifier.fillMaxWidth(),
    ) {
        options.forEachIndexed { index, unit ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = 2,
                ),
                onClick = {
                    if (index != selectedIndex) {
                        onIsMetricSystemChange(index == 0) // index 0 = metric, index 1 = imperial
                    }
                },
                selected = index == selectedIndex,
                label = {
                    Text(
                        text = unit,
                        style = MaterialTheme.typography.titleMedium,
                    )
                },
                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = MaterialTheme.colorScheme.secondary,
                    activeBorderColor = MaterialTheme.colorScheme.outline,
                    inactiveBorderColor = MaterialTheme.colorScheme.outline,
                    activeContentColor = MaterialTheme.colorScheme.onSecondary,
                    inactiveContentColor = MaterialTheme.colorScheme.onSecondary,
                ),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileSetupDialogSegmentedButtonPreview() {
    SmartStepTheme {
        ProfileSetupDialogSegmentedButton(
            isMetricSystem = true,
            unitMetric = "kg",
            unitImperial = "lbs",
            onIsMetricSystemChange = {},
        )
    }
}
