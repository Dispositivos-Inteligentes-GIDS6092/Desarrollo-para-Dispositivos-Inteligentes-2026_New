package mx.edu.utng.bgma.smarthealthmonitor.wear.presentation

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import mx.edu.utng.bgma.smarthealthmonitor.wear.HealthDataService
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.wear.compose.material.*
import mx.edu.utng.bgma.smarthealthmonitor.wear.presentation.theme.SmartHealthMonitorTheme

class MainActivity : ComponentActivity() {

    // Lanzador de permisos para sensores de salud
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.BODY_SENSORS] == true) {
            Log.d("MainActivity", "Permiso concedido, registrando Health Services")
            lifecycleScope.launch {
                HealthDataService.registrar(applicationContext)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        setTheme(android.R.style.Theme_DeviceDefault)

        // Solicitar permisos al iniciar
        permissionLauncher.launch(arrayOf(Manifest.permission.BODY_SENSORS))

        setContent {
            WearApp()
        }
    }
}

@Composable
fun WearApp() {
    SmartHealthMonitorTheme {
        Box(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "SmartHealth", style = MaterialTheme.typography.caption1)
                Text(text = "Monitoreando FC...", textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            }
        }
    }
}