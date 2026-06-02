package mx.edu.utng.bgma.smarthealthmonitor.wear

import android.content.Context
import android.util.Log
import androidx.health.services.client.HealthServices
import androidx.health.services.client.PassiveListenerService
import androidx.health.services.client.data.DataPointContainer
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.PassiveListenerConfig
import androidx.health.services.client.data.SampleDataPoint
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.*

class HealthDataService : PassiveListenerService() {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private lateinit var messageClient: MessageClient
    private val TAG = "HealthDataService"

    override fun onCreate() {
        super.onCreate()
        messageClient = Wearable.getMessageClient(this)
        Log.d(TAG, "HealthDataService creado")
    }

    override fun onNewDataPointsReceived(dataPoints: DataPointContainer) {
        val fcDataPoints = dataPoints.getData(DataType.HEART_RATE_BPM)

        fcDataPoints.forEach { dataPoint ->
            if (dataPoint is SampleDataPoint<Double>) {
                val bpm = dataPoint.value.toInt()
                Log.d(TAG, "FC recibida: $bpm BPM")
                scope.launch {
                    enviarFC(bpm)
                }
            }
        }
    }

    private suspend fun enviarFC(bpm: Int) {
        try {
            val data = bpm.toString().toByteArray()
            messageClient.sendMessage(
                "smartphone",
                "/smarthealthmonitor/fc",
                data
            )
            Log.d(TAG, "FC enviada al teléfono: $bpm")
        } catch (e: Exception) {
            Log.e(TAG, "Error enviando FC: ${e.message}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
        Log.d(TAG, "HealthDataService destruido")
    }

    companion object {
        suspend fun registrar(context: Context) {
            try {
                val hsClient = HealthServices.getClient(context)
                val passiveClient = hsClient.passiveMonitoringClient

                val config = PassiveListenerConfig.builder()
                    .setDataTypes(setOf(DataType.HEART_RATE_BPM))
                    .setShouldUserActivityInfoBeRequested(true)
                    .build()

                passiveClient.setPassiveListenerServiceAsync(
                    HealthDataService::class.java,
                    config
                )
                Log.d("HealthDataService", "Health Services registrado correctamente")
            } catch (e: Exception) {
                Log.e("HealthDataService", "Error registrando: ${e.message}")
            }
        }
    }
}