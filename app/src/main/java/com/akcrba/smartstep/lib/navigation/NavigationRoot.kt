package com.akcrba.smartstep.lib.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.akcrba.smartstep.feature.home.presentation.HomeScreen
import com.akcrba.smartstep.feature.myprofile.presentation.MyProfileScreen
import kotlin.text.clear

@Composable
internal fun NavigationRoot(
    modifier: Modifier = Modifier,
) {
    val backStack = rememberNavBackStack(Route.MyProfile)

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = { key ->
            when (key) {
                Route.MyProfile -> NavEntry(key) {
                    MyProfileScreen(
                        onNavigateToHome = {
                            // Clear history so user can't 'back' into the profile screen
                            backStack.clear()
                            backStack.add(Route.Home)
                        }
                    )
                }

                Route.Home -> NavEntry(key) {
                    HomeScreen()
                }

                else -> error("Unknown NavKey: $key")
            }
        },
    )
}
