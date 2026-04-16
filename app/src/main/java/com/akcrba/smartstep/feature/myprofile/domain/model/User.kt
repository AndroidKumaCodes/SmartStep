package com.akcrba.smartstep.feature.myprofile.domain.model

internal data class User(
    val gender: Gender,
    val height: Double,
    val weight: Double,
    val isMetric: Boolean,
)
