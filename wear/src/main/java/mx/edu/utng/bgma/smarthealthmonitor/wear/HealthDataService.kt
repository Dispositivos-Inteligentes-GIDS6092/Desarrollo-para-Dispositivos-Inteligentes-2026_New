package mx.edu.utng.bgma.smarthealthmonitor.wear

import android.content.Context
import android.util.Log
import androidx.health.services.client.HealthServices
import androidx.health.services.client.PassiveListenerService
import androidx.health.services.client.data.DataPointContainer
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.PassiveListenerConfig
import com.google.android.gms.tasks.Tasks
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

        scope.launch {
            try {
                val nodeClient = Wearable.getNodeClient(this@HealthDataService)

                val localNode = Tasks.await(nodeClient.localNode)
                Log.d("WEAR_TEST", "Nodo local: ${localNode.displayName}")

                val nodes = Tasks.await(nodeClient.connectedNodes)

                Log.d("WEAR_TEST", "Cantidad de nodos: ${nodes.size}")

                for (node in nodes) {
                    Log.d(
                        "WEAR_TEST",
                        "Nodo remoto: ${node.displayName} (${node.id})"
                    )
                }

            } catch (e: Exception) {
                Log.e("WEAR_TEST", "Error: ${e.message}", e)
            }
        }
    }

    override fun onNewDataPointsReceived(dataPoints: DataPointContainer) {
        val fcDataPoints = dataPoints.getData(DataType.HEART_RATE_BPM)
        val lastFC = fcDataPoints.lastOrNull()
        if (lastFC != null) {
            val bpm = (lastFC.value as Number).toInt()
            Log.d(TAG, "FC recibida (última del lote): $bpm BPM")
             scope.launch {
                enviarFC(bpm)
             }
        }

        val stepsDataPoints = dataPoints.getData(DataType.STEPS_DAILY)
        val lastSteps = stepsDataPoints.lastOrNull()
        if (lastSteps != null) {
            val pasos = (lastSteps.value as Number).toInt()
            Log.d(TAG, "Pasos recibidos (últimos del lote): $pasos")
            scope.launch {
                enviarPasos(pasos)
            }
        }
    }

    private suspend fun enviarFC(bpm: Int) {
        try {
            val data = bpm.toString().toByteArray()
            val nodeClient = Wearable.getNodeClient(this)
            val nodes = Tasks.await(nodeClient.connectedNodes)

            Log.d(TAG, "================================")
            Log.d(TAG, "Nodos encontrados: ${nodes.size}")

            for (node in nodes) {
                Log.d(TAG, "Nodo ID: ${node.id}")
                Log.d(TAG, "Nodo Nombre: ${node.displayName}")
            }
            Log.d(TAG, "================================")
            for (node in nodes) {
                messageClient.sendMessage(
                    node.id,
                    "/smarthealthmonitor/fc",
                    data
                )
                Log.d(TAG, "FC enviada al nodo ${node.displayName} (${node.id}): $bpm")
            }
            if (nodes.isEmpty()) {
                Log.w(TAG, "No hay nodos conectados para enviar la FC.")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error enviando FC: ${e.message}")
        }
    }

    private suspend fun enviarPasos(pasos: Int) {
        try {
            val data = pasos.toString().toByteArray()
            val nodeClient = Wearable.getNodeClient(this)
            val nodes = Tasks.await(nodeClient.connectedNodes)

            Log.d(TAG, "================================")
            Log.d(TAG, "Nodos encontrados: ${nodes.size}")

            for (node in nodes) {
                Log.d(TAG, "Nodo ID: ${node.id}")
                Log.d(TAG, "Nodo Nombre: ${node.displayName}")
            }
            Log.d(TAG, "================================")
            for (node in nodes) {
                Tasks.await(
                    messageClient.sendMessage(
                        node.id,
                        "/smarthealthmonitor/pasos",
                        data
                    )
                )
                Log.d(TAG, "Pasos enviados al nodo ${node.displayName} (${node.id}): $pasos")
            }
            if (nodes.isEmpty()) {
                Log.w(TAG, "No hay nodos conectados para enviar pasos.")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error enviando pasos: ${e.message}")
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
                    .setDataTypes(setOf(
                        DataType.HEART_RATE_BPM,
                        DataType.STEPS_DAILY
                    ))
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

        suspend fun enviarFCDirectamente(
            context: Context,
            bpm: Int
        ) = withContext(Dispatchers.IO) {

            try {

                val messageClient = Wearable.getMessageClient(context)
                val nodeClient = Wearable.getNodeClient(context)

                val nodes = Tasks.await(nodeClient.connectedNodes)

                Log.d("WEAR_TEST", "================================")
                Log.d("WEAR_TEST", "Enviar FC")
                Log.d("WEAR_TEST", "Nodos encontrados: ${nodes.size}")

                val data = bpm.toString().toByteArray()

                for (node in nodes) {

                    Log.d(
                        "WEAR_TEST",
                        "Enviando FC a ${node.displayName}"
                    )

                    Tasks.await(
                        messageClient.sendMessage(
                            node.id,
                            "/smarthealthmonitor/fc",
                            data
                        )
                    )

                    Log.d(
                        "WEAR_TEST",
                        "FC enviada correctamente"
                    )
                }

                if (nodes.isEmpty()) {
                    Log.w(
                        "WEAR_TEST",
                        "NO HAY NODOS CONECTADOS"
                    )
                }

            } catch (e: Exception) {
                Log.e(
                    "WEAR_TEST",
                    "Error enviando FC",
                    e
                )
            }
        }

        suspend fun enviarPasosDirectamente(
            context: Context,
            pasos: Int
        ) = withContext(Dispatchers.IO) {

            try {

                val messageClient = Wearable.getMessageClient(context)
                val nodeClient = Wearable.getNodeClient(context)

                val nodes = Tasks.await(nodeClient.connectedNodes)

                Log.d("WEAR_TEST", "================================")
                Log.d("WEAR_TEST", "Enviar Pasos")
                Log.d("WEAR_TEST", "Nodos encontrados: ${nodes.size}")

                val data = pasos.toString().toByteArray()

                for (node in nodes) {

                    Log.d(
                        "WEAR_TEST",
                        "Enviando pasos a ${node.displayName}"
                    )

                    Tasks.await(
                        messageClient.sendMessage(
                            node.id,
                            "/smarthealthmonitor/pasos",
                            data
                        )
                    )

                    Log.d(
                        "WEAR_TEST",
                        "Pasos enviados correctamente"
                    )
                }

                if (nodes.isEmpty()) {
                    Log.w(
                        "WEAR_TEST",
                        "NO HAY NODOS CONECTADOS"
                    )
                }

            } catch (e: Exception) {
                Log.e(
                    "WEAR_TEST",
                    "Error enviando pasos",
                    e
                )
            }
        }
    }
}