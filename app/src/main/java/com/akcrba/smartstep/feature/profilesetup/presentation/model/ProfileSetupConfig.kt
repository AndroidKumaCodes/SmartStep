package com.akcrba.smartstep.feature.profilesetup.presentation.model

import com.akcrba.smartstep.feature.profilesetup.domain.model.Gender

// This file contains the "Static Configuration" (fixed options, ranges, and types available to choose from)

internal enum class DialogType(
    val unitMetric: String,
    val unitImperial: String,
) {
    WEIGHT(unitMetric = "kg", unitImperial = "lbs"),
    HEIGHT(unitMetric = "cm", unitImperial = "ft/inch"),
}

sealed class WheelPickerData {
    data class Height(val items: HeightItems) : WheelPickerData()
    data class Weight(val items: WeightItems) : WheelPickerData()
}

internal data class ProfileSetupItems(
    val genderItems: List<Gender> = genderItemsList,
    val heightItems: HeightItems = HeightItems(),
    val weightItems: WeightItems = WeightItems(),
)

data class HeightItems(
    val cmItems: List<Int> = defaultCmItems,
    val ftItems: List<Int> = defaultCmItems.map { cm ->
        Height.fromMetric(cm).ft
    }.distinct(),
    val inchItems: List<Int> = (0 until INCHES_PER_FOOT).toList(),
)

data class WeightItems(
    val kgItems: List<Int> = defaultKgItems,
    val lbsItems: List<Int> = (
        Weight.fromMetric(defaultKgItems.first()).lbs..Weight.fromMetric(defaultKgItems.last()).lbs
        ).toList(),
)

// Configuration Constants
private const val MIN_CM = 120
private const val MAX_CM = 210
private const val MIN_KG = 40
private const val MAX_KG = 150
private val genderItemsList: List<Gender> = Gender.entries.map { it }
private val defaultCmItems: List<Int> = (MIN_CM..MAX_CM).toList()
private val defaultKgItems: List<Int> = (MIN_KG..MAX_KG).toList()
