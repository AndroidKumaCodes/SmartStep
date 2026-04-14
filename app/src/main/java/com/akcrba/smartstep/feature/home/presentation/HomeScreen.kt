package com.akcrba.smartstep.feature.home.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.akcrba.smartstep.R
import com.akcrba.smartstep.feature.home.presentation.composables.StepsCard
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreen(modifier: Modifier = Modifier) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    BackHandler(enabled = drawerState.isOpen) {
        scope.launch { drawerState.close() }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { HomeScreenDrawerContent() },
    ) {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Smart Step") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                    ),
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                painter = painterResource(R.drawable.burger_menu),
                                contentDescription = "Menu",
                            )
                        }
                    },
                )
            },
            contentWindowInsets = WindowInsets.safeDrawing,
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center,
            ) {
                StepsCard(
                    steps = 5500,
                    goal = 6000,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .widthIn(max = 426.dp)
                        .fillMaxWidth()
                        .padding(bottom = 110.dp),
                )
            }
        }
    }
}

@Composable
private fun HomeScreenDrawerContent(modifier: Modifier = Modifier) {
    ModalDrawerSheet(modifier = modifier.background(MaterialTheme.colorScheme.surfaceVariant)) {
        Text(
            text = "Menu",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp),
        )

        HorizontalDivider(color = MaterialTheme.colorScheme.outline)

        NavigationDrawerItem(
            label = { Text("""Fix the "Stop Counting Steps" issue""") },
            selected = false,
            onClick = { /* später */ },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
        )
        NavigationDrawerItem(
            label = { Text("Step Goal") },
            selected = false,
            onClick = { /* später */ },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
        )
        NavigationDrawerItem(
            label = { Text("Personal Settings") },
            selected = false,
            onClick = { /* später */ },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
        )
        NavigationDrawerItem(
            label = { Text("Exit") },
            selected = false,
            onClick = { /* später */ },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
        )
    }
}
