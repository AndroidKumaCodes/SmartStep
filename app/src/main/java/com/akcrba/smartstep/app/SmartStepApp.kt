package com.akcrba.smartstep.app

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.KoinApplication
import org.koin.core.annotation.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.plugin.module.dsl.startKoin

@Module
@ComponentScan("com.akcrba.smartstep")
@Configuration
internal class AppModule

@KoinApplication
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
        initKoin {
            androidContext(this@SmartStepApp)
        }
    }

    private fun initKoin(config: KoinAppDeclaration? = null) {
        startKoin<SmartStepApp> {
            config?.invoke(this)
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
