package com.akcrba.smartstep.feature.myprofile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akcrba.smartstep.feature.myprofile.domain.usecase.DeleteUserUseCase
import com.akcrba.smartstep.feature.myprofile.domain.usecase.GetUserUseCase
import com.akcrba.smartstep.feature.myprofile.domain.usecase.SaveUserUseCase
import com.akcrba.smartstep.feature.myprofile.presentation.model.BodyStats
import com.akcrba.smartstep.feature.myprofile.presentation.model.DialogType
import com.akcrba.smartstep.feature.myprofile.presentation.model.Height
import com.akcrba.smartstep.feature.myprofile.presentation.model.MyProfileItems
import com.akcrba.smartstep.feature.myprofile.presentation.model.UiUser
import com.akcrba.smartstep.feature.myprofile.presentation.model.Weight
import com.akcrba.smartstep.feature.myprofile.presentation.model.toDomainUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel
import kotlin.math.roundToInt

@KoinViewModel
internal class MyProfileViewModel(
    getUserUseCase: GetUserUseCase,
    private val saveUserUseCase: SaveUserUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(MyProfileScreenState())
    val state: StateFlow<MyProfileScreenState> = _state.asStateFlow()

    internal val myProfileItems = MyProfileItems()

    init {
        getUserUseCase().onEach { domainUser ->
            _state.update { currentState ->
                when (domainUser) {
                    null -> if (currentState.user == UiUser()) currentState else currentState.copy(user = UiUser())
                    else -> currentState.copy(
                        user = UiUser(
                            gender = domainUser.gender,
                            bodyStats = BodyStats(
                                height = Height.fromMetric(domainUser.height.roundToInt()),
                                weight = Weight.fromMetric(domainUser.weight.roundToInt()),
                                isMetric = domainUser.isMetric,
                            ),
                        ),
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    internal fun onAction(action: MyProfileAction) {
        when (action) {
            MyProfileAction.OnClickDeleteUser -> {
                viewModelScope.launch {
                    deleteUserUseCase()
                }
            }

            MyProfileAction.OnClickSkip -> {
                viewModelScope.launch {
                    val user = UiUser()
                    saveUserUseCase(user.toDomainUser())
                }
            }
            MyProfileAction.OnClickStart -> {
                viewModelScope.launch {
                    saveUserUseCase(state.value.user.toDomainUser())
                }
            }

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
