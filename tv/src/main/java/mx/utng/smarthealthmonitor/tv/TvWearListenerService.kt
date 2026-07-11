package mx.utng.smarthealthmonitor.tv

import android.util.Log
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import mx.edu.utng.bgma.smarthealthmonitor.data.SmartHealthRepository

class TvWearListenerService : WearableListenerService() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onMessageReceived(messageEvent: MessageEvent) {
        val data = String(messageEvent.data)
        val path = messageEvent.path
        Log.d("TvWearListener", "Mensaje recibido del Wear: path=$path, data=$data")

        serviceScope.launch {
            when (path) {
                "/smarthealthmonitor/fc" -> {
                    val bpm = data.toIntOrNull() ?: return@launch
                    SmartHealthRepository.actualizarFC(bpm)
                }
                "/smarthealthmonitor/pasos" -> {
                    val pasos = data.toIntOrNull() ?: return@launch
                    SmartHealthRepository.actualizarPasos(pasos)
                }
            }
        }
    }
}
