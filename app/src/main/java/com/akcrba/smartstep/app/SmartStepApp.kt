package com.akcrba.smartstep.app

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SmartStepApp : Application() {

    @Volatile
    var isDataReady: Boolean = false
        private set

    private val applicationScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()

        applicationScope.launch {
            isDataReady = getAllDataAndSetThingsUp()
        }
    }

    private suspend fun getAllDataAndSetThingsUp(): Boolean {
        // Simulate heavy work
        println("getAllDataAndSetThingsUp gestartet")
        delay(500L)
        println("getAllDataAndSetThingsUp fertig")
        // If this is < 2000, the splash waits for the 2s timer in MainActivity.
        // If this is > 2000, the splash waits for this function to finish.
        return true
    }
}
