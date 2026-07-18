package mx.edu.utng.bgma.smarthealthmonitor.wear.mqtt

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import mx.edu.utng.bgma.smarthealthmonitor.mqtt.FcMessage
import mx.edu.utng.bgma.smarthealthmonitor.mqtt.MqttConfig
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import javax.net.ssl.SSLSocketFactory

class MqttWearPublisher {
    private var mqttClient: MqttClient? = null
    private val TAG = "MqttWearPublisher"

    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    fun connect() {
        try {
            val persistence = MemoryPersistence()
            mqttClient = MqttClient(MqttConfig.BROKER_URL, MqttConfig.CLIENT_WEAR, persistence)
            
            val options = MqttConnectOptions().apply {
                userName = MqttConfig.USERNAME
                password = MqttConfig.PASSWORD.toCharArray()
                socketFactory = SSLSocketFactory.getDefault()
                isCleanSession = true
                isAutomaticReconnect = true
            }

            mqttClient?.connect(options)
            _isConnected.value = true
            Log.d(TAG, "✅ Conectado a HiveMQ Cloud")
        } catch (e: Exception) {
            _isConnected.value = false
            Log.e(TAG, "❌ Error al conectar: ${e.message}")
        }
    }

    fun publishFC(bpm: Int, estado: String) {
        try {
            if (mqttClient?.isConnected == true) {
                _isConnected.value = true
                val message = FcMessage(bpm, estado)
                val json = Json.encodeToString(message)
                val mqttMessage = MqttMessage(json.toByteArray()).apply {
                    qos = MqttConfig.QOS
                    isRetained = true
                }
                mqttClient?.publish(MqttConfig.TOPIC_FC, mqttMessage)
                Log.d(TAG, "📤 Publicado: $bpm bpm")
            } else {
                _isConnected.value = false
                Log.w(TAG, "⚠️ Cliente no conectado, reintentando...")
                connect()
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error al publicar: ${e.message}")
        }
    }

    fun disconnect() {
        try {
            mqttClient?.disconnect()
            _isConnected.value = false
            Log.d(TAG, "🔌 Desconectado")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error al desconectar: ${e.message}")
        }
    }
}
