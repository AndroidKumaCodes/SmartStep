package com.akcrba.smartstep.feature.myprofile.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.akcrba.smartstep.app.ui.theme.bodyLargeRegular
import com.akcrba.smartstep.app.ui.theme.bodySmallRegular

@Composable
internal fun SelectFieldContent(
    label: String,
    displayValue: String,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            // 1. Draw Visuals (Border & Background)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(12.dp),
            )
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            // 2. Add Internal Spacing (Padding inside the visuals)
            // MOVED: This must come AFTER background/border to behave as content padding
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmallRegular,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = displayValue,
                style = MaterialTheme.typography.bodyLargeRegular,
                color = MaterialTheme.colorScheme.onSecondary,
            )
        }
        Icon(
            imageVector = Icons.Outlined.KeyboardArrowDown,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSecondary,
        )
    }
}
