package com.akcrba.smartstep.feature.myprofile.presentation

import com.akcrba.smartstep.feature.myprofile.domain.model.Gender
import com.akcrba.smartstep.feature.myprofile.presentation.model.DialogType
import com.akcrba.smartstep.feature.myprofile.presentation.model.Height
import com.akcrba.smartstep.feature.myprofile.presentation.model.UiUser
import com.akcrba.smartstep.feature.myprofile.presentation.model.Weight

internal data class MyProfileScreenState(
    val user: UiUser = UiUser(),
    val showHeightDialog: Boolean = false,
    val showWeightDialog: Boolean = false,
    val deleteButtonActive: Boolean = false,
)

internal sealed interface MyProfileAction {
    data class SetGender(val gender: Gender) : MyProfileAction
    data object OnClickDeleteUser : MyProfileAction

    data object OnClickSkip : MyProfileAction
    data object OnClickStart : MyProfileAction

    // Group Dialog Actions here
    sealed interface Dialog : MyProfileAction {
        data class UpdateInterimHeight(val height: Height) : Dialog
        data class UpdateInterimWeight(val weight: Weight) : Dialog

        data class SetIsMetric(val isMetric: Boolean) : Dialog
        data class ShowDialog(val type: DialogType) : Dialog
        data class OnClickOk(val type: DialogType) : Dialog

        data object OnClickCancel : Dialog
    }
}
