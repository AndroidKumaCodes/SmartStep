package com.akcrba.smartstep.feature.home.presentation

internal data class HomeScreenState(
    val steps: Int = 0,
    val goal: Int = 6000,
    val showFixStopCountingStepsIssue: Boolean = false,
)

internal sealed interface HomeAction {

    sealed interface HamburgerMenu : HomeAction {
        data object OnClickHamburgerMenu : HomeAction
        data object OnClickFixStopCountingStepsIssue : HamburgerMenu
        data object OnClickStepGoal : HamburgerMenu
        data object OnClickPersonalSettings : HamburgerMenu
        data object OnClickExit : HamburgerMenu
    }

    sealed interface StepGoal : HomeAction {
        data object OnClickSave : StepGoal
        data object OnClickCancel : StepGoal
    }

    sealed interface BottomSheet : HomeAction {
        data object OnClickAllowAccess : BottomSheet
        data object OnClickOpenSettings : BottomSheet
        data object OnClickContinue : BottomSheet
    }

    sealed interface ExitDialog : HomeAction {
        data object OnClickOk : ExitDialog
    }
}
