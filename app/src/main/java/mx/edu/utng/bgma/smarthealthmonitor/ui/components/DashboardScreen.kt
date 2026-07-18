package mx.edu.utng.bgma.smarthealthmonitor.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.mediarouter.app.MediaRouteButton
import com.google.android.gms.cast.framework.CastButtonFactory
import com.google.android.gms.cast.framework.CastContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mx.edu.utng.bgma.smarthealthmonitor.data.SmartHealthRepository
import mx.edu.utng.bgma.smarthealthmonitor.ui.viewmodel.DashboardViewModel

@Composable
fun CastButton(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    
    // Usamos remember para inicializar el CastContext de forma segura
    var isCastAvailable by remember { mutableStateOf(false) }

    LaunchedEffect(context) {
        try {
            CastContext.getSharedInstance(context)
            isCastAvailable = true
        } catch (e: Exception) {
            Log.w("CastButton", "Cast SDK no disponible")
            isCastAvailable = false
        }
    }

    if (isCastAvailable) {
        AndroidView(
            factory = { ctx ->
                MediaRouteButton(ctx).apply {
                    CastButtonFactory.setUpMediaRouteButton(ctx, this)
                }
            },
            modifier = modifier.size(32.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onHistorialClick: () -> Unit = {},
    onAlertClick: () -> Unit = {},
    viewModel: DashboardViewModel = viewModel(),
    fcManual: Int? = null,
    pasosManual: Int? = null,
    spO2Manual: Int? = null
) {
    val scope = rememberCoroutineScope()
    var mostrarAlerta by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    val fcState by viewModel.fc.collectAsState()
    val pasosState by viewModel.pasos.collectAsState()
    val spO2State by viewModel.spO2.collectAsState()
    val historial by viewModel.historial.collectAsState()

    val fc = fcManual ?: fcState
    val pasos = pasosManual ?: pasosState
    val spO2 = spO2Manual ?: spO2State

    val isMqttConnected by mx.edu.utng.bgma.smarthealthmonitor.mqtt.MqttAppService.getInstance().isConnected.collectAsState()
    var showSuccessMessage by remember { mutableStateOf(false) }

    // Función para mostrar el mensaje de éxito temporalmente
    val triggerSuccess = {
        scope.launch {
            showSuccessMessage = true
            delay(3000)
            showSuccessMessage = false
        }
    }

    LaunchedEffect(isMqttConnected) {
        if (isMqttConnected) {
            delay(10000)
            triggerSuccess()
        }
    }

    if (mostrarAlerta) {
// ...
        AlertaScreen(
            fc = fc,
            onDismiss = { mostrarAlerta = false },
            onConfirmar = {
                mostrarAlerta = false
                viewModel.enviarAlertaEmergencia()
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = "✅ Alerta enviada",
                        duration = SnackbarDuration.Long
                    )
                }
            }
        )
    }

    Scaffold(
        snackbarHost = { 
            SnackbarHost(hostState = snackbarHostState)
            if (showSuccessMessage) {
                Snackbar(
                    modifier = Modifier.padding(16.dp),
                    action = {}
                ) {
                    Text("✅ Conexión al servidor establecida correctamente")
                }
            }
        },
        topBar = {
            TopAppBar(
                title = { Text("SmartHealth") },
                actions = {
                    CastButton(modifier = Modifier.padding(end = 8.dp))
                    IconButton(onClick = { /* Opcional */ }) {
                        Icon(Icons.Default.Settings, contentDescription = "Configuración")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { mostrarAlerta = true },
                containerColor = MaterialTheme.colorScheme.error
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Alerta",
                    tint = MaterialTheme.colorScheme.onError
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                TarjetaDato(
                    valor = fc.toString(),
                    unidad = "bpm",
                    label = "Frecuencia cardíaca",
                    colorValor = MaterialTheme.colorScheme.error,
                    esNormal = fc in 60..100
                )
            }

            item {
                TarjetaDato(
                    valor = "%,d".format(pasos),
                    unidad = "pasos",
                    label = "Pasos del día",
                    colorValor = MaterialTheme.colorScheme.primary
                )
            }

            item {
                TarjetaDato(
                    valor = spO2.toString(),
                    unidad = "%",
                    label = "Saturación de Oxígeno",
                    colorValor = MaterialTheme.colorScheme.tertiary,
                    esNormal = spO2 >= 95
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Historial reciente", style = MaterialTheme.typography.titleMedium)
                    TextButton(onClick = onHistorialClick) {
                        Text("Ver todo")
                    }
                }
            }

            items(historial, key = { it.id }) { lectura ->
                FilaHistorial(lectura = lectura)
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(
                        onClick = {
                            mx.edu.utng.bgma.smarthealthmonitor.mqtt.MqttAppService.getInstance().simularEnvioATv()
                            triggerSuccess()
                        },
                        modifier = Modifier
                            .size(16.dp)
                            .background(
                                color = Color(0xFF0D1B4A),
                                shape = CircleShape
                            )
                    ) { }
                }

                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(
                    onClick = {
                        scope.launch {
                            SmartHealthRepository.actualizarFC((60..120).random())
                            SmartHealthRepository.actualizarPasos((1000..9000).random())
                            SmartHealthRepository.actualizarSpO2((90..100).random())
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Simular Wearable (Debug)")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DashboardScreenPreview() {
    DashboardScreen(fcManual = 75, pasosManual = 4500, spO2Manual = 98)
}
