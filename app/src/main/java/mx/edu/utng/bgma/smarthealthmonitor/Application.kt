package mx.edu.utng.bgma.smarthealthmonitor

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import mx.edu.utng.bgma.smarthealthmonitor.data.SmartHealthRepository
import mx.edu.utng.bgma.smarthealthmonitor.data.sync.NeonSyncWorker
import mx.edu.utng.bgma.smarthealthmonitor.mqtt.MqttAppService
import java.util.concurrent.TimeUnit

class SmartHealthApp : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        SmartHealthRepository.init(this)  // inicializar Room

        MqttAppService.getInstance().connect()
        iniciarSyncWorker()

        applicationScope.launch(Dispatchers.IO) {
            SmartHealthRepository.limpiarHistorialAntiguo()
        }
    }

    private fun iniciarSyncWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncRequest = PeriodicWorkRequestBuilder<NeonSyncWorker>(30, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueue(syncRequest)
    }
}
