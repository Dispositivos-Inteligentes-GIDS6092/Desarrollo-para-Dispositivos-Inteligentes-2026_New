package mx.edu.utng.bgma.smarthealthmonitor.mqtt

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import mx.edu.utng.bgma.smarthealthmonitor.data.SmartHealthRepository
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import java.text.SimpleDateFormat
import java.util.*
import javax.net.ssl.SSLSocketFactory

class MqttAppService private constructor() {
    companion object {
        @Volatile
        private var INSTANCE: MqttAppService? = null

        fun getInstance(): MqttAppService {
            return INSTANCE ?: synchronized(this) {
                MqttAppService().also { INSTANCE = it }
            }
        }
    }
    
    private var mqttClient: MqttClient? = null
    private val TAG = "MqttAppService"
    private val json = Json { ignoreUnknownKeys = true }
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    fun connect() {
        serviceScope.launch {
            try {
                val persistence = MemoryPersistence()
                mqttClient = MqttClient(MqttConfig.BROKER_URL, MqttConfig.CLIENT_APP, persistence)
                
                val options = MqttConnectOptions().apply {
                    userName = MqttConfig.USERNAME
                    password = MqttConfig.PASSWORD.toCharArray()
                    socketFactory = SSLSocketFactory.getDefault()
                    isCleanSession = true
                    isAutomaticReconnect = true
                    connectionTimeout = 10
                    keepAliveInterval = 20
                }

                mqttClient?.setCallback(object : MqttCallback {
                    override fun connectionLost(cause: Throwable?) {
                        Log.e(TAG, "❌ Conexión perdida: ${cause?.message}")
                        _isConnected.value = false
                    }

                    override fun messageArrived(topic: String?, message: MqttMessage?) {
                        if (topic == MqttConfig.TOPIC_FC) {
                            val payload = message?.payload?.let { String(it) } ?: return
                            Log.d(TAG, "📩 Mensaje recibido de Wear: $payload")
                            handleFcMessage(payload)
                        }
                    }

                    override fun deliveryComplete(token: IMqttDeliveryToken?) {}
                })

                mqttClient?.connect(options)
                mqttClient?.subscribe(MqttConfig.TOPIC_FC, MqttConfig.QOS)
                _isConnected.value = true
                Log.d(TAG, "✅ Conectado y suscrito a ${MqttConfig.TOPIC_FC}")
            } catch (e: Exception) {
                _isConnected.value = false
                Log.e(TAG, "❌ Error al conectar MQTT: ${e.message}")
            }
        }
    }

    private fun handleFcMessage(payload: String) {
        try {
            val fcMsg = json.decodeFromString<FcMessage>(payload)
            serviceScope.launch {
                SmartHealthRepository.actualizarFC(fcMsg.bpm)
            }
            republishToTv(fcMsg)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error al procesar mensaje FC: ${e.message}")
        }
    }

    private fun republishToTv(fcMsg: FcMessage) {
        try {
            val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            val hora = sdf.format(Date(fcMsg.timestamp))
            val tvMsg = TvMessage(bpm = fcMsg.bpm, estado = fcMsg.estado, hora = hora)
            val payload = json.encodeToString(tvMsg)
            val message = MqttMessage(payload.toByteArray()).apply {
                qos = MqttConfig.QOS
                isRetained = true
            }
            mqttClient?.publish(MqttConfig.TOPIC_TV, message)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error al republicar al TV: ${e.message}")
        }
    }

    fun simularEnvioATv() {
        val fcMsg = FcMessage(bpm = (60..120).random(), estado = "Simulado")
        serviceScope.launch {
            SmartHealthRepository.actualizarFC(fcMsg.bpm)
        }
        republishToTv(fcMsg)
    }

    fun disconnect() {
        try {
            mqttClient?.disconnect()
            _isConnected.value = false
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error al desconectar: ${e.message}")
        }
    }
}
