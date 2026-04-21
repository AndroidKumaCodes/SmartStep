package com.akcrba.smartstep.feature.home.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
internal class HomeViewModel : ViewModel() {

    private val _state = MutableStateFlow(HomeScreenState())
    val state: StateFlow<HomeScreenState> = _state.asStateFlow()

    internal fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.HamburgerMenu.OnClickHamburgerMenu -> TODO()
            HomeAction.HamburgerMenu.OnClickFixStopCountingStepsIssue -> TODO()
            HomeAction.HamburgerMenu.OnClickPersonalSettings -> TODO()
            HomeAction.HamburgerMenu.OnClickStepGoal -> TODO()
            HomeAction.HamburgerMenu.OnClickExit -> TODO()
            HomeAction.StepGoal.onClickCancel -> TODO()
            HomeAction.StepGoal.onClickSave -> TODO()
            HomeAction.ExitDialog.OnClickOk -> TODO()
            HomeAction.BottomSheet.OnClickAllowAccess -> TODO()
            HomeAction.BottomSheet.OnClickOpenSettings -> TODO()
            HomeAction.BottomSheet.OnClickContinue -> TODO()
        }
    }
}
