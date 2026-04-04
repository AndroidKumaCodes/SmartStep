package com.akcrba.smartstep.feature.myprofile.domain.model

data class User(
    val gender: Gender,
    val height: Double,
    val weight: Double,
    val isMetric: Boolean,
)
