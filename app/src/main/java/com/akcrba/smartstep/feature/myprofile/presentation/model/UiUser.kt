package com.akcrba.smartstep.feature.myprofile.presentation.model

import com.akcrba.smartstep.feature.myprofile.domain.model.Gender
import com.akcrba.smartstep.feature.myprofile.domain.model.User
import kotlin.math.roundToInt

// This file defines the "Dynamic State" (the data that changes as the user interacts with the app)

internal const val CM_PER_INCH = 2.54
internal const val LBS_PER_KG = 2.20462
internal const val INCHES_PER_FOOT = 12

data class UiUser(
    val gender: Gender = Gender.FEMALE,
    val bodyStats: BodyStats = BodyStats(),
) {
    val displayGender: Gender
        get() = gender

    val displayHeight: String
        get() = bodyStats.height.formatted(bodyStats.isMetric)

    val displayWeight: String
        get() = bodyStats.weight.formatted(bodyStats.isMetric)
}

data class BodyStats(
    val height: Height = Height(cm = 175, ft = 5, inch = 9),
    val interimHeight: Height = Height(cm = 175, ft = 5, inch = 9),
    val weight: Weight = Weight(kg = 65, lbs = 143),
    val interimWeight: Weight = Weight(kg = 65, lbs = 143),
    val isMetric: Boolean = true,
)

data class Height(
    val cm: Int,
    val ft: Int,
    val inch: Int,
) {
    // Helper to format string without ViewModel logic
    fun formatted(isMetric: Boolean): String = if (isMetric) "$cm cm" else "$ft ft $inch in"

    fun toExactCm(isMetricSource: Boolean): Double = if (isMetricSource) {
        cm.toDouble()
    } else {
        (ft * INCHES_PER_FOOT + inch) * CM_PER_INCH
    }

    /**
     * Why using a companion object here?
     * Kotlin allows only one primary constructor. You cannot have two constructors that both take two Ints (e.g.,
     * if you had Height(cm, dummy) and Height(ft, inch)). Factory methods allow you to have multiple ways to create
     * an object with clear names describing how it's being created.
     */
    companion object {
        // Factory Method 1: Create Height from cm
        fun fromMetric(cm: Int): Height {
            val totalInches = cm / CM_PER_INCH
            val ft = (totalInches / INCHES_PER_FOOT).toInt()
            val rawInch = (totalInches % INCHES_PER_FOOT).roundToInt()

            // Handle edge case where rounding up moves to next foot (e.g. 11.9 in -> 12 in -> 0 in + 1 ft)
            val inch = if (rawInch == 12) 0 else rawInch
            val normalizedFt = if (rawInch == 12) ft + 1 else ft

            return Height(cm = cm, ft = normalizedFt, inch = inch)
        }

        // Factory Method 2: Create Height from ft and inch
        fun fromImperial(
            ft: Int,
            inch: Int,
        ): Height {
            val cm = ((ft * INCHES_PER_FOOT + inch) * CM_PER_INCH).roundToInt()
            return Height(cm = cm, ft = ft, inch = inch)
        }
    }
}

data class Weight(
    val kg: Int,
    val lbs: Int,
) {
    fun formatted(isMetric: Boolean): String = if (isMetric) "$kg kg" else "$lbs lbs"

    fun toExactKg(isMetricSource: Boolean): Double = if (isMetricSource) kg.toDouble() else lbs / LBS_PER_KG

    companion object {
        fun fromMetric(kg: Int): Weight {
            val lbs = (kg * LBS_PER_KG).roundToInt()
            return Weight(kg = kg, lbs = lbs)
        }

        fun fromImperial(lbs: Int): Weight {
            val kg = (lbs / LBS_PER_KG).roundToInt()
            return Weight(kg = kg, lbs = lbs)
        }
    }
}

fun UiUser.toDomainUser(): User = User(
    gender = gender,
    height = bodyStats.height.toExactCm(bodyStats.isMetric),
    weight = bodyStats.weight.toExactKg(bodyStats.isMetric),
    isMetric = bodyStats.isMetric,
)
