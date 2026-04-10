package com.akcrba.smartstep.feature.myprofile.data.local.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import com.akcrba.smartstep.feature.myprofile.domain.model.Gender
import com.akcrba.smartstep.feature.myprofile.domain.model.User

@Entity(tableName = "users")
internal data class UserEntity(
    @PrimaryKey val id: Int = 1, // Using a fixed ID of 1 if there's only one user profile in the app
    val gender: String,
    val height: Double,
    val weight: Double,
    val isMetric: Boolean,
)

internal fun User.toEntity() = UserEntity(
    gender = this.gender.value,
    height = this.height,
    weight = this.weight,
    isMetric = this.isMetric,
)

internal fun UserEntity.toUser() = User(
    gender = Gender.valueOf(this.gender.uppercase()),
    height = this.height,
    weight = this.weight,
    isMetric = this.isMetric,
)
