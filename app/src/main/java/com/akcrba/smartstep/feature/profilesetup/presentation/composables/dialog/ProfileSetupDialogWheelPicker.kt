package com.akcrba.smartstep.feature.profilesetup.presentation.composables.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.akcrba.smartstep.R
import com.akcrba.smartstep.feature.profilesetup.presentation.model.BodyStats
import com.akcrba.smartstep.feature.profilesetup.presentation.model.WheelPickerData
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

/**
 * Wheel Picker Component for selecting from a list of items.
 *
 * BEHAVIOR:
 * - Uses a fixed height layout where the "Selected" item is always the one
 *   snapped to the top padding (firstVisibleItemIndex).
 * - Visual highlighting and logic are both derived solely from this index.
 */

// --- Configuration Constants ---

// 1. Define the dimensions of a single item
private val ItemHeight = 44.dp

// 2. Define the Layout Strategy
// The picker displays 4 items total.
// The "Selected" item is positioned at the 3rd slot (index 2).
// [Row 0] Visible Item (Above)
// [Row 1] Visible Item (Above)
// [Row 2] SELECTED ITEM (Highlight Band)
// [Row 3] Visible Item (Below)
private const val VISIBLE_ITEM_COUNT = 4
private const val SELECTED_ROW_INDEX = 2

// 3. Derive Padding & Height from the strategy
// TopPadding pushes the first list item down to the Selected Row.
private val SelectedRowOffset = ItemHeight * SELECTED_ROW_INDEX

// BottomPadding ensures the last list item can be scrolled up to the Selected Row.
// (Remaining rows = Total - 1 for selected - rows above)
private val TrailingPadding = ItemHeight * (VISIBLE_ITEM_COUNT - 1 - SELECTED_ROW_INDEX)

// The total height of the component
private val WheelHeight = ItemHeight * VISIBLE_ITEM_COUNT

private val SelectedFontSize = 24.sp

@Composable
internal fun ProfileSetupDialogWheelPicker(
    wheelPickerData: WheelPickerData,
    bodyStats: BodyStats,
    onSingleValueChange: (Int) -> Unit,
    onDoubleValueChange: (Int, Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isMetric = bodyStats.isMetric

    when (wheelPickerData) {
        is WheelPickerData.Height -> {
            val heightItems = wheelPickerData.items

            if (isMetric) {
                SingleWheel(
                    modifier = modifier,
                    wheelItems = heightItems.cmItems,
                    initialValue = bodyStats.interimHeight.cm,
                    unit = stringResource(R.string.ps_dialog_unit_cm),
                    onValueChange = onSingleValueChange,
                )
            } else {
                DoubleWheel(
                    modifier = modifier,
                    leftItems = heightItems.ftItems,
                    rightItems = heightItems.inchItems,
                    initialValueLeft = bodyStats.interimHeight.ft,
                    initialValueRight = bodyStats.interimHeight.inch,
                    leftUnit = stringResource(R.string.ps_dialog_unit_ft),
                    rightUnit = stringResource(R.string.ps_dialog_unit_inch),
                    onValueChange = onDoubleValueChange,
                )
            }
        }

        is WheelPickerData.Weight -> {
            val weightItems = wheelPickerData.items

            // Prepare data first to keep the key-wrapper clean
            val items = if (isMetric) weightItems.kgItems else weightItems.lbsItems
            val initial = if (isMetric) bodyStats.interimWeight.kg else bodyStats.interimWeight.lbs
            val unitRes = if (isMetric) R.string.ps_dialog_unit_kg else R.string.ps_dialog_unit_lbs
            val unit = stringResource(unitRes)

            // IMPORTANT: key(isMetric) forces a fresh start when units change,
            // preventing the wheel from retaining an out-of-bounds scroll index.
            key(isMetric) {
                SingleWheel(
                    modifier = modifier,
                    wheelItems = items,
                    initialValue = initial,
                    unit = unit,
                    onValueChange = onSingleValueChange,
                )
            }
        }
    }
}

@Composable
private fun SingleWheel(
    wheelItems: List<Int>,
    initialValue: Int,
    modifier: Modifier = Modifier,
    unit: String? = null,
    onValueChange: (Int) -> Unit = {},
) {
    val currentOnValueChange by rememberUpdatedState(onValueChange)
    val listState = rememberWheelState(wheelItems, initialValue)

    WheelContainer(modifier = modifier) {
        WheelColumn(
            values = wheelItems,
            listState = listState,
            showUnitOnSelected = false,
            unit = unit,
        )
    }

    LaunchedEffect(listState, wheelItems) {
        // Defensive: if the list is empty, do nothing
        if (wheelItems.isEmpty()) return@LaunchedEffect

        snapshotFlow { listState.snappedIndex }
            .distinctUntilChanged()
            .collect { index ->
                if (index in wheelItems.indices) {
                    currentOnValueChange(wheelItems[index])
                }
            }
    }
}

@Composable
private fun DoubleWheel(
    leftItems: List<Int>,
    rightItems: List<Int>,
    initialValueLeft: Int,
    initialValueRight: Int,
    onValueChange: (Int, Int) -> Unit,
    modifier: Modifier = Modifier,
    leftUnit: String? = null,
    rightUnit: String? = null,
) {
    val currentOnValueChange by rememberUpdatedState(onValueChange)

    val leftListState = rememberWheelState(leftItems, initialValueLeft)
    val rightListState = rememberWheelState(rightItems, initialValueRight)

    WheelContainer(modifier = modifier) {
        Row(modifier = Modifier.fillMaxSize()) {
            WheelColumn(
                values = leftItems,
                listState = leftListState,
                showUnitOnSelected = true,
                unit = leftUnit,
                modifier = Modifier.weight(1f),
            )
            WheelColumn(
                values = rightItems,
                listState = rightListState,
                showUnitOnSelected = true,
                unit = rightUnit,
                modifier = Modifier.weight(1f),
            )
        }
    }

    LaunchedEffect(leftListState, rightListState, leftItems, rightItems) {
        // Defensive: skip if either list is empty
        if (leftItems.isEmpty() || rightItems.isEmpty()) return@LaunchedEffect

        combine(
            snapshotFlow { leftListState.snappedIndex },
            snapshotFlow { rightListState.snappedIndex },
        ) { index1, index2 ->
            val v1 = leftItems.getOrNull(index1)
            val v2 = rightItems.getOrNull(index2)
            if (v1 != null && v2 != null) v1 to v2 else null
        }
            .filterNotNull()
            .distinctUntilChanged()
            .collect { (v1, v2) -> currentOnValueChange(v1, v2) }
    }
}

// --- Extracted Components ---

@Composable
private fun WheelContainer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(WheelHeight),
        contentAlignment = Alignment.TopCenter,
    ) {
        // Highlight Band (Background)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                // The band is pinned to the "Selected Row" defined in our constants
                .padding(top = SelectedRowOffset)
                .height(ItemHeight)
                .background(MaterialTheme.colorScheme.tertiary),
        )
        content()
    }
}

@Composable
private fun WheelColumn(
    values: List<Int>,
    listState: LazyListState,
    showUnitOnSelected: Boolean,
    modifier: Modifier = Modifier,
    unit: String? = null,
) {
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)
    val scope = rememberCoroutineScope()

    val selectedIndex by remember {
        derivedStateOf { listState.snappedIndex }
    }

    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize(),
        flingBehavior = flingBehavior,
        contentPadding = PaddingValues(top = SelectedRowOffset, bottom = TrailingPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(
            count = values.size,
            key = { index -> index },
        ) { index ->
            WheelPickerItem(
                value = values[index],
                // Simple equality check
                isSelected = (index == selectedIndex),
                showUnitOnSelected = showUnitOnSelected,
                unit = unit,
                onItemClick = { scope.launch { listState.animateScrollToItem(index) } },
            )
        }
    }
}

@Composable
private fun WheelPickerItem(
    value: Int,
    isSelected: Boolean,
    showUnitOnSelected: Boolean,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier,
    unit: String? = null,
) {
    val (fontSize, color, fontWeight) = if (isSelected) {
        Triple(SelectedFontSize, MaterialTheme.colorScheme.onSurface, FontWeight.Bold)
    } else {
        Triple(
            MaterialTheme.typography.titleMedium.fontSize,
            MaterialTheme.colorScheme.onSurfaceVariant,
            FontWeight.Normal,
        )
    }

    Box(
        modifier = modifier
            .height(ItemHeight)
            .fillMaxWidth()
            .clickable { onItemClick() },
        contentAlignment = Alignment.Center,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = value.toString(),
                style = MaterialTheme.typography.titleMedium,
                fontSize = fontSize,
                color = color,
                fontWeight = fontWeight,
            )
            if (isSelected && showUnitOnSelected && unit != null) {
                Text(
                    text = unit,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = color,
                    modifier = Modifier.padding(start = 4.dp),
                )
            }
        }
    }
}

// --- Logic Helpers ---

@Composable
private fun rememberWheelState(
    items: List<Int>,
    initialValue: Int,
): LazyListState {
    val initialIndex = remember(items, initialValue) {
        items.takeIf { list -> list.isNotEmpty() }?.indexOf(initialValue)?.takeIf { it != -1 } ?: 0
    }

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)

    LaunchedEffect(initialIndex) {
        if (!listState.isScrollInProgress && listState.snappedIndex != initialIndex) {
            listState.scrollToItem(initialIndex)
        }
    }
    return listState
}

/**
 * Because SelectedRowOffset pushes the first item into the selected row,
 * 'firstVisibleItemIndex' equals the snapped row index
 */
private val LazyListState.snappedIndex: Int
    get() = firstVisibleItemIndex
