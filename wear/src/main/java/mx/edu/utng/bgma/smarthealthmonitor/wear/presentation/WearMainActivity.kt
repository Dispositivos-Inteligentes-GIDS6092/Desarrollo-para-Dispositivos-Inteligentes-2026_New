package mx.edu.utng.bgma.smarthealthmonitor.wear.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import mx.edu.utng.bgma.smarthealthmonitor.data.SmartHealthRepository
import mx.edu.utng.bgma.smarthealthmonitor.wear.HealthDataService
import androidx.compose.runtime.*
import androidx.wear.compose.material.*
import androidx.health.services.client.HealthServices
import androidx.health.services.client.MeasureCallback
import androidx.health.services.client.data.Availability
import androidx.health.services.client.data.DataPointContainer
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.DeltaDataType

class WearMainActivity : ComponentActivity() {

    private var isMeasuring = false
    private val measureClient by lazy { HealthServices.getClient(this).measureClient }

    private val measureCallback = object : MeasureCallback {
        override fun onAvailabilityChanged(
            dataType: DeltaDataType<*, *>,
            availability: Availability
        ) {
            Log.d("MainActivity", "Disponibilidad del sensor cambiada: $availability")
        }

        override fun onDataReceived(data: DataPointContainer) {
            val heartRateData = data.getData(DataType.HEART_RATE_BPM)
            val lastFC = heartRateData.lastOrNull()
            if (lastFC != null) {
                val bpm = (lastFC.value as Number).toInt()
                Log.d("WearMainActivity", "FC recibida: $bpm BPM")

                // Actualizamos el repositorio para que el ViewModel lo muestre
                lifecycleScope.launch {
                    SmartHealthRepository.actualizarFC(bpm)
                    // Enviar datos al teléfono
                    HealthDataService.enviarFCDirectamente(this@WearMainActivity, bpm)
                }
            }

            val stepsData = data.getData(DataType.STEPS_DAILY)
            val lastSteps = stepsData.lastOrNull()
            if (lastSteps != null) {
                val pasos = (lastSteps.value as Number).toInt()
                lifecycleScope.launch {
                    SmartHealthRepository.actualizarPasos(pasos)
                    // Enviar datos al teléfono
                    HealthDataService.enviarPasosDirectamente(this@WearMainActivity, pasos)
                }
            }
        }
    }

    private val permissionsToRequest: Array<String>
        get() = arrayOf(
            Manifest.permission.ACTIVITY_RECOGNITION,
            Manifest.permission.BODY_SENSORS
        )

    private fun checkPermissionsGranted(): Boolean {
        return permissionsToRequest.all { permission ->
            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
        }
    }

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.all { it.value }) {
            iniciarMedicion()
        } else {
            Log.w("MainActivity", "Permisos denegados")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setTheme(android.R.style.Theme_DeviceDefault)

        // Inicializar repositorio para Wear
        SmartHealthRepository.init(this)

        // Registrar servicio de datos en segundo plano
        lifecycleScope.launch {
            HealthDataService.registrar(this@WearMainActivity)
        }

        verificarYSolicitarPermisos()

        setContent {
            WearApp()
        }
    }

    override fun onStart() {
        super.onStart()
        if (checkPermissionsGranted()) {
            iniciarMedicion()
        }
    }

    private fun verificarYSolicitarPermisos() {
        if (checkPermissionsGranted()) {
            iniciarMedicion()
        } else {
            permissionLauncher.launch(permissionsToRequest)
        }
    }

    override fun onStop() {
        super.onStop()
        detenerMedicion()
    }

    private fun iniciarMedicion() {
        if (isMeasuring) return
        try {
            // Activamos el registro del callback para recibir datos reales
            measureClient.registerMeasureCallback(DataType.HEART_RATE_BPM, measureCallback)
            isMeasuring = true
            Log.d("MainActivity", "Medición iniciada correctamente")
        } catch (e: Exception) {
            Log.e("MainActivity", "Error al iniciar medición: ${e.message}")
        }
    }

    private fun detenerMedicion() {
        if (!isMeasuring) return
        try {
            measureClient.unregisterMeasureCallbackAsync(DataType.HEART_RATE_BPM, measureCallback)
            isMeasuring = false
            Log.d("MainActivity", "Medición detenida")
        } catch (e: Exception) {
            Log.e("MainActivity", "Error al detener medición: ${e.message}")
        }
    }
}

@Composable
fun WearApp() {
    SmartHealthMonitorTheme {
        // Solo llamamos al NavGraph, él maneja las pantallas
        SmartHealthWearNavGraph()
    }
}

@Composable
fun SmartHealthMonitorTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        content = content
    )
}
