package com.akcrba.smartstep.feature.myprofile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
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
import com.akcrba.smartstep.BuildConfig
import com.akcrba.smartstep.R
import com.akcrba.smartstep.app.ui.theme.bodyLargeMedium
import com.akcrba.smartstep.feature.myprofile.domain.model.Gender
import com.akcrba.smartstep.feature.myprofile.presentation.composables.MyProfileDialogTriggerField
import com.akcrba.smartstep.feature.myprofile.presentation.composables.MyProfileDropDownMenu
import com.akcrba.smartstep.feature.myprofile.presentation.composables.dialog.MyProfileDialog
import com.akcrba.smartstep.feature.myprofile.presentation.model.DialogType
import com.akcrba.smartstep.feature.myprofile.presentation.model.MyProfileItems
import com.akcrba.smartstep.feature.myprofile.presentation.model.WheelPickerData
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun MyProfileScreen(
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MyProfileViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    MyProfileContent(
        modifier = modifier,
        state = state,
        onAction = viewModel::onAction,
        onNavigateToHome = onNavigateToHome,
        myProfileItems = viewModel.myProfileItems,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyProfileContent(
    state: MyProfileScreenState,
    onAction: (MyProfileAction) -> Unit,
    onNavigateToHome: () -> Unit,
    myProfileItems: MyProfileItems,
    modifier: Modifier = Modifier,
) {
    // Determine the data for the active dialog dynamically instead of duplicating the Composable call.
    val activeDialogData = when {
        state.showHeightDialog -> WheelPickerData.Height(myProfileItems.heightItems)
        state.showWeightDialog -> WheelPickerData.Weight(myProfileItems.weightItems)
        else -> null
    }

    if (activeDialogData != null) {
        MyProfileDialog(
            bodyStats = state.user.bodyStats,
            wheelPickerData = activeDialogData,
            onAction = onAction,
            onIsMetricSystemChange = {
                onAction(
                    MyProfileAction.Dialog.SetIsMetric(isMetric = !state.user.bodyStats.isMetric),
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
                        text = stringResource(R.string.my_profile_title),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                },
                actions = {
                    TextButton(
                        onClick = {
                            onAction(MyProfileAction.OnClickSkip)
                            onNavigateToHome()
                        },
                    ) {
                        Text(
                            text = stringResource(R.string.my_profile_skip),
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

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter,
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 426.dp)
                    .fillMaxSize(),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(state = rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(modifier = Modifier.height(24.dp))
                    CustomCard(
                        genders = myProfileItems.genderItems,
                        displayGender = state.user.displayGender,
                        displayHeight = state.user.displayHeight,
                        displayWeight = state.user.displayWeight,
                        onAction = onAction,
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Spacer(modifier = Modifier.weight(1f))

                if (BuildConfig.DEBUG) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            """
                            User
                            Gender: ${state.user.displayGender.value}
                            Height: ${state.user.displayHeight}
                            Weight: ${state.user.displayWeight}
                            IsMetric: ${state.user.bodyStats.isMetric}
                            """.trimIndent(),
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    TextButton(
                        modifier = Modifier
                            .widthIn(max = 250.dp)
                            .fillMaxWidth()
                            .height(40.dp)
                            .padding(horizontal = 16.dp)
                            .align(Alignment.CenterHorizontally),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                        ),
                        enabled = state.deleteButtonActive,
                        onClick = { onAction(MyProfileAction.OnClickDeleteUser) },
                    ) {
                        Text(
                            text = "Delete User",
                            style = MaterialTheme.typography.bodyLargeMedium,
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))
                }

                TextButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .padding(horizontal = 16.dp)
                        .align(Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                    onClick = {
                        onAction(MyProfileAction.OnClickStart)
                        onNavigateToHome()
                    },
                ) {
                    Text(
                        text = stringResource(R.string.my_profile_start),
                        style = MaterialTheme.typography.bodyLargeMedium,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomCard(
    genders: List<Gender>,
    displayGender: Gender,
    displayHeight: String,
    displayWeight: String,
    onAction: (MyProfileAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(8.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = stringResource(R.string.my_profile_info_text),
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
            MyProfileDropDownMenu(
                genders = genders,
                label = stringResource(R.string.my_profile_dialog_label_gender),
                displayGender = displayGender,
                onItemSelect = { gender ->
                    onAction(MyProfileAction.SetGender(gender))
                },
            )
            Spacer(modifier = Modifier.height(8.dp))
            MyProfileDialogTriggerField(
                label = stringResource(R.string.my_profile_dialog_label_height),
                displayValue = displayHeight,
                onDialogTrigger = { onAction(MyProfileAction.Dialog.ShowDialog(DialogType.HEIGHT)) },
            )
            Spacer(modifier = Modifier.height(8.dp))
            MyProfileDialogTriggerField(
                label = stringResource(R.string.my_profile_dialog_label_weight),
                displayValue = displayWeight,
                onDialogTrigger = { onAction(MyProfileAction.Dialog.ShowDialog(DialogType.WEIGHT)) },
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
