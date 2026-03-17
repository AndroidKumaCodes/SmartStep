package com.buchwald.smartstep.feature.profilesetup.presentation

import androidx.lifecycle.ViewModel
import com.buchwald.smartstep.feature.profilesetup.domain.model.Gender
import com.buchwald.smartstep.feature.profilesetup.presentation.model.BodyStats
import com.buchwald.smartstep.feature.profilesetup.presentation.model.DialogType
import com.buchwald.smartstep.feature.profilesetup.presentation.model.Height
import com.buchwald.smartstep.feature.profilesetup.presentation.model.ProfileSetupItems
import com.buchwald.smartstep.feature.profilesetup.presentation.model.UiUser
import com.buchwald.smartstep.feature.profilesetup.presentation.model.Weight
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

internal class ProfileSetupViewModel : ViewModel() {
    private val _state = MutableStateFlow(ProfileSetupScreenState())
    val state: StateFlow<ProfileSetupScreenState> = _state.asStateFlow()

    internal val profileSetupItems = ProfileSetupItems()

    internal fun onAction(action: ProfileSetupAction) {
        when (action) {
            ProfileSetupAction.OnClickSkip -> {} // TODO
            ProfileSetupAction.OnClickStart -> {} // TODO
            is ProfileSetupAction.SetGender -> _state.update { state ->
                state.copy(user = state.user.copy(gender = action.gender))
            }

            is ProfileSetupAction.Dialog -> onDialogAction(action)
        }
    }

    private fun onDialogAction(action: ProfileSetupAction.Dialog) {
        when (action) {
            is ProfileSetupAction.Dialog.ShowDialog -> _state.update { state ->
                state.withBodyStats { stats ->
                    stats.copy(
                        interimHeight = stats.height,
                        interimWeight = stats.weight,
                    )
                }.copy(
                    showHeightDialog = action.type == DialogType.HEIGHT,
                    showWeightDialog = action.type == DialogType.WEIGHT,
                )
            }

            is ProfileSetupAction.Dialog.OnClickOk -> _state.update { state ->
                state.withBodyStats { stats ->
                    when (action.type) {
                        DialogType.HEIGHT -> stats.copy(height = stats.interimHeight)
                        DialogType.WEIGHT -> stats.copy(weight = stats.interimWeight)
                    }
                }.copy(
                    showHeightDialog = false,
                    showWeightDialog = false,
                )
            }

            is ProfileSetupAction.Dialog.SetIsMetric -> _state.update { state ->
                state.withBodyStats { stats ->
                    stats.copy(isMetric = action.isMetric)
                }
            }

            is ProfileSetupAction.Dialog.UpdateInterimHeight -> _state.update { state ->
                state.withBodyStats { stats ->
                    stats.copy(interimHeight = action.height)
                }
            }

            is ProfileSetupAction.Dialog.UpdateInterimWeight -> _state.update { state ->
                state.withBodyStats { stats ->
                    stats.copy(interimWeight = action.weight)
                }
            }

            ProfileSetupAction.Dialog.OnClickCancel -> _state.update { state ->
                state.copy(
                    showHeightDialog = false,
                    showWeightDialog = false,
                )
            }
        }
    }

    private fun ProfileSetupScreenState.withBodyStats(block: (BodyStats) -> BodyStats): ProfileSetupScreenState =
        copy(user = user.copy(bodyStats = block(user.bodyStats)))
}

internal data class ProfileSetupScreenState(
    val user: UiUser = UiUser(),
    val showHeightDialog: Boolean = false,
    val showWeightDialog: Boolean = false,
)

internal sealed interface ProfileSetupAction {
    data class SetGender(val gender: Gender) : ProfileSetupAction

    data object OnClickSkip : ProfileSetupAction
    data object OnClickStart : ProfileSetupAction

    // Group Dialog Actions here
    sealed interface Dialog : ProfileSetupAction {
        data class UpdateInterimHeight(val height: Height) : Dialog
        data class UpdateInterimWeight(val weight: Weight) : Dialog

        data class SetIsMetric(val isMetric: Boolean) : Dialog
        data class ShowDialog(val type: DialogType) : Dialog
        data class OnClickOk(val type: DialogType) : Dialog

        data object OnClickCancel : Dialog
    }
}
