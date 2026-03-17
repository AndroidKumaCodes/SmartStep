package com.buchwald.smartstep.feature.profilesetup.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.buchwald.smartstep.app.ui.theme.bodyLargeRegular
import com.buchwald.smartstep.feature.profilesetup.domain.model.Gender

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProfileSetupDropDownMenu(
    genders: List<Gender>,
    label: String,
    selectedItem: Gender,
    onItemSelect: (Gender) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp),
        expanded = isExpanded,
        onExpandedChange = { newValue ->
            if (isExpanded != newValue) {
                isExpanded = newValue
            }
        },
    ) {
        SelectFieldContent(
            modifier = Modifier.menuAnchor(
                ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                true,
            ),
            label = label,
            displayValue = selectedItem.value,
        )
        ExposedDropdownMenu(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
        ) {
            genders.forEachIndexed { index, gender ->
                DropdownMenuItem(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    text = {
                        Text(
                            text = gender.value,
                            style = MaterialTheme.typography.bodyLargeRegular,
                            color = MaterialTheme.colorScheme.onSecondary,
                        )
                    },
                    onClick = {
                        onItemSelect(gender)
                        isExpanded = false
                    },
                    trailingIcon = {
                        if (selectedItem == gender) {
                            Icon(
                                modifier = Modifier.size(16.dp),
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSecondary,
                            )
                        }
                    },
                    contentPadding = PaddingValues(start = 16.dp, end = 4.dp),
                )
                if (index < genders.size - 1) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}
