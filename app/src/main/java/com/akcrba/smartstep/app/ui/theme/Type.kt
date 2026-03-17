package com.akcrba.smartstep.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.akcrba.smartstep.R

private val Inter = FontFamily(
    Font(R.font.inter, FontWeight.Normal),
    Font(R.font.inter_medium, FontWeight.Medium),
    Font(R.font.inter_semibold, FontWeight.SemiBold),
)

// Aliases
val Typography.titleAccent: TextStyle
    get() = displayLarge

val Typography.bodyLargeRegular: TextStyle
    get() = bodyLarge

val Typography.bodyLargeMedium: TextStyle
    get() = labelLarge

val Typography.bodyMediumRegular: TextStyle
    get() = bodyMedium

val Typography.bodyMediumMedium: TextStyle
    get() = labelMedium

val Typography.bodySmallRegular: TextStyle
    get() = bodySmall

// Maps the Figma typography spec to Material 3 Typography slots.
val Typography = Typography(
    // Title-Accent: Inter SemiBold 64/70
    displayLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.SemiBold,
        fontSize = 64.sp,
        lineHeight = 70.sp,
    ),

    // Title-Medium: Inter Medium 18/24
    titleMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 24.sp,
    ),

    // Body-Large-Regular: Inter Regular 16/24
    bodyLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),

    // Body-Large-Medium: Inter Medium 16/24
    // Use labelLarge as the "medium body 16" slot (common practical mapping).
    labelLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),

    // Body-Medium-Regular: Inter Regular 14/18
    bodyMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 18.sp,
    ),

    // Body-Medium-Medium: Inter Medium 14/18
    labelMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 18.sp,
    ),

    // Body-Small-Regular: Inter Regular 12/16
    bodySmall = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
    ),
)
