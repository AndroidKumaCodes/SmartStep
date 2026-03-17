package com.akcrba.smartstep.feature.profilesetup.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.akcrba.smartstep.R
import com.akcrba.smartstep.app.ui.theme.bodyLargeMedium
import com.akcrba.smartstep.feature.profilesetup.domain.model.Gender
import com.akcrba.smartstep.feature.profilesetup.presentation.composables.ProfileSetupDialogTriggerField
import com.akcrba.smartstep.feature.profilesetup.presentation.composables.ProfileSetupDropDownMenu
import com.akcrba.smartstep.feature.profilesetup.presentation.composables.dialog.ProfileSetupDialog
import com.akcrba.smartstep.feature.profilesetup.presentation.model.DialogType
import com.akcrba.smartstep.feature.profilesetup.presentation.model.ProfileSetupItems
import com.akcrba.smartstep.feature.profilesetup.presentation.model.WheelPickerData

@Composable
internal fun ProfileSetupScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileSetupViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ProfileSetupContent(
        modifier = modifier,
        state = state,
        onAction = viewModel::onAction,
        profileSetupItems = viewModel.profileSetupItems,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileSetupContent(
    state: ProfileSetupScreenState,
    onAction: (ProfileSetupAction) -> Unit,
    profileSetupItems: ProfileSetupItems,
    modifier: Modifier = Modifier,
) {
    // Determine the data for the active dialog dynamically instead of duplicating the Composable call.
    val activeDialogData = when {
        state.showHeightDialog -> WheelPickerData.Height(profileSetupItems.heightItems)
        state.showWeightDialog -> WheelPickerData.Weight(profileSetupItems.weightItems)
        else -> null
    }

    if (activeDialogData != null) {
        ProfileSetupDialog(
            bodyStats = state.user.bodyStats,
            wheelPickerData = activeDialogData,
            onAction = onAction,
            onIsMetricSystemChange = {
                onAction(
                    ProfileSetupAction.Dialog.SetIsMetric(isMetric = !state.user.bodyStats.isMetric),
                )
            },
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.profile_setup_title),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                },
                actions = {
                    TextButton(onClick = { TODO() }) {
                        Text(
                            text = stringResource(R.string.profile_setup_skip),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
            )
        },
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(state = rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                CustomCard(
                    genders = profileSetupItems.genderItems,
                    gender = state.user.gender,
                    displayHeight = state.user.displayHeight,
                    displayWeight = state.user.displayWeight,
                    onAction = onAction,
                )
                // Add some padding at the bottom of scroll content so it doesn't touch the button immediately
                Spacer(modifier = Modifier.height(16.dp))
            }

            TextButton(
                modifier = Modifier
                    .widthIn(max = 426.dp)
                    .fillMaxWidth()
                    .height(40.dp)
                    .padding(horizontal = 16.dp)
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.textButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                onClick = { /*TODO*/ },
            ) {
                Text(
                    text = stringResource(R.string.profile_setup_start),
                    style = MaterialTheme.typography.bodyLargeMedium,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomCard(
    genders: List<Gender>,
    gender: Gender,
    displayHeight: String,
    displayWeight: String,
    onAction: (ProfileSetupAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .widthIn(max = 426.dp)
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(8.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = stringResource(R.string.profile_setup_info_text),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surface)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(12.dp),
                ),
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            ProfileSetupDropDownMenu(
                genders = genders,
                label = stringResource(R.string.ps_dialog_label_gender),
                selectedItem = gender,
                onItemSelect = { gender ->
                    onAction(ProfileSetupAction.SetGender(gender))
                },
            )
            Spacer(modifier = Modifier.height(8.dp))
            ProfileSetupDialogTriggerField(
                label = stringResource(R.string.ps_dialog_label_height),
                displayValue = displayHeight,
                onDialogTrigger = { onAction(ProfileSetupAction.Dialog.ShowDialog(DialogType.HEIGHT)) },
            )
            Spacer(modifier = Modifier.height(8.dp))
            ProfileSetupDialogTriggerField(
                label = stringResource(R.string.ps_dialog_label_weight),
                displayValue = displayWeight,
                onDialogTrigger = { onAction(ProfileSetupAction.Dialog.ShowDialog(DialogType.WEIGHT)) },
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
