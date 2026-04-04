package com.akcrba.smartstep.feature.myprofile.presentation

import androidx.lifecycle.ViewModel
import com.akcrba.smartstep.feature.myprofile.domain.model.Gender
import com.akcrba.smartstep.feature.myprofile.domain.usecase.SaveUserUseCase
import com.akcrba.smartstep.feature.myprofile.presentation.model.BodyStats
import com.akcrba.smartstep.feature.myprofile.presentation.model.DialogType
import com.akcrba.smartstep.feature.myprofile.presentation.model.Height
import com.akcrba.smartstep.feature.myprofile.presentation.model.MyProfileItems
import com.akcrba.smartstep.feature.myprofile.presentation.model.UiUser
import com.akcrba.smartstep.feature.myprofile.presentation.model.Weight
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
internal class MyProfileViewModel(val saveUserUseCase: SaveUserUseCase) : ViewModel() {
    private val _state = MutableStateFlow(MyProfileScreenState())
    val state: StateFlow<MyProfileScreenState> = _state.asStateFlow()

    internal val myProfileItems = MyProfileItems()

    internal fun onAction(action: MyProfileAction) {
        when (action) {
            MyProfileAction.OnClickSkip -> {} // TODO
            MyProfileAction.OnClickStart -> {} // TODO
            is MyProfileAction.SetGender -> _state.update { state ->
                state.copy(user = state.user.copy(gender = action.gender))
            }

            is MyProfileAction.Dialog -> onDialogAction(action)
        }
    }

    private fun onDialogAction(action: MyProfileAction.Dialog) {
        when (action) {
            is MyProfileAction.Dialog.ShowDialog -> _state.update { state ->
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

            is MyProfileAction.Dialog.OnClickOk -> _state.update { state ->
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

            is MyProfileAction.Dialog.SetIsMetric -> _state.update { state ->
                state.withBodyStats { stats ->
                    stats.copy(isMetric = action.isMetric)
                }
            }

            is MyProfileAction.Dialog.UpdateInterimHeight -> _state.update { state ->
                state.withBodyStats { stats ->
                    stats.copy(interimHeight = action.height)
                }
            }

            is MyProfileAction.Dialog.UpdateInterimWeight -> _state.update { state ->
                state.withBodyStats { stats ->
                    stats.copy(interimWeight = action.weight)
                }
            }

            MyProfileAction.Dialog.OnClickCancel -> _state.update { state ->
                state.copy(
                    showHeightDialog = false,
                    showWeightDialog = false,
                )
            }
        }
    }

    private fun MyProfileScreenState.withBodyStats(block: (BodyStats) -> BodyStats): MyProfileScreenState =
        copy(user = user.copy(bodyStats = block(user.bodyStats)))
}

internal data class MyProfileScreenState(
    val user: UiUser = UiUser(),
    val showHeightDialog: Boolean = false,
    val showWeightDialog: Boolean = false,
)

internal sealed interface MyProfileAction {
    data class SetGender(val gender: Gender) : MyProfileAction

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
