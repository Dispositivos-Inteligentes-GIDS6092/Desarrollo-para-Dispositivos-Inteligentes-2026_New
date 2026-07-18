package mx.utng.smarthealthmonitor.tv.mqtt

import android.util.Log
import kotlinx.serialization.json.Json
import mx.edu.utng.bgma.smarthealthmonitor.mqtt.MqttConfig
import mx.edu.utng.bgma.smarthealthmonitor.mqtt.TvMessage
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import javax.net.ssl.SSLSocketFactory

class MqttTvSubscriber(private val onMessageReceived: (TvMessage) -> Unit) {
    private var mqttClient: MqttClient? = null
    private val TAG = "MqttTvSubscriber"
    private val json = Json { ignoreUnknownKeys = true }

    fun connect() {
        try {
            val persistence = MemoryPersistence()
            mqttClient = MqttClient(MqttConfig.BROKER_URL, MqttConfig.CLIENT_TV, persistence)
            
            val options = MqttConnectOptions().apply {
                userName = MqttConfig.USERNAME
                password = MqttConfig.PASSWORD.toCharArray()
                socketFactory = SSLSocketFactory.getDefault()
                isCleanSession = true
                isAutomaticReconnect = true
            }

            mqttClient?.setCallback(object : MqttCallback {
                override fun connectionLost(cause: Throwable?) {
                    Log.e(TAG, "❌ Conexión perdida: ${cause?.message}")
                }

                override fun messageArrived(topic: String?, message: MqttMessage?) {
                    if (topic == MqttConfig.TOPIC_TV) {
                        val payload = message?.payload?.let { String(it) } ?: return
                        Log.d(TAG, "📺 Recibido: $payload")
                        handleTvMessage(payload)
                    }
                }

                override fun deliveryComplete(token: IMqttDeliveryToken?) {}
            })

            mqttClient?.connect(options)
            mqttClient?.subscribe(MqttConfig.TOPIC_TV, MqttConfig.QOS)
            Log.d(TAG, "✅ TV suscrita a ${MqttConfig.TOPIC_TV}")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error al conectar TV: ${e.message}")
        }
    }

    private fun handleTvMessage(payload: String) {
        try {
            val tvMsg = json.decodeFromString<TvMessage>(payload)
            onMessageReceived(tvMsg)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error al procesar mensaje TV: ${e.message}")
        }
    }

    fun disconnect() {
        try {
            mqttClient?.disconnect()
            Log.d(TAG, "🔌 TV Desconectada")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error al desconectar TV: ${e.message}")
        }
    }
}
