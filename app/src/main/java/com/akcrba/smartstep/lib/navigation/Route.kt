package com.akcrba.smartstep.lib.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey {

    @Serializable
    object MyProfile : Route

    @Serializable
    object Home : Route

}
