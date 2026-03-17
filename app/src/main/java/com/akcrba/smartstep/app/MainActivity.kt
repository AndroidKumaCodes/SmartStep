package com.akcrba.smartstep.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.akcrba.smartstep.app.ui.theme.SmartStepTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        setSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartStepTheme {
                SmartStepAppRoot()
            }
        }
    }

    private fun setSplashScreen() {
        installSplashScreen().apply {
            var isTimerReady = false
            val app = application as SmartStepApp

            lifecycleScope.launch {
                println("Timer gestartet")
                delay(1000L)
                println("Timer fertig")
                isTimerReady = true
            }
            setKeepOnScreenCondition {
                !isTimerReady || !app.isDataReady
            }
        }
    }
}
