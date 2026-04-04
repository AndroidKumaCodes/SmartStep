package com.akcrba.smartstep.feature.myprofile.presentation.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
internal fun MyProfileDialogTriggerField(
    label: String,
    displayValue: String,
    onDialogTrigger: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SelectFieldContent(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                onClick = onDialogTrigger,
            ),
        label = label,
        displayValue = displayValue,
    )
}
