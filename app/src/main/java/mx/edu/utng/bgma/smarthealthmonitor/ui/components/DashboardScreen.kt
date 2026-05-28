package mx.edu.utng.bgma.smarthealthmonitor.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState // Agregado
import androidx.compose.runtime.getValue       // Agregado
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel // Agregado
// Asegúrate de que el import de tu ViewModel sea correcto según tu estructura de carpetas
// import mx.edu.utng.bgma.smarthealthmonitor.ui.viewmodels.DashboardViewModel

import mx.edu.utng.bgma.smarthealthmonitor.data.models.LecturaFC
import mx.edu.utng.bgma.smarthealthmonitor.data.models.MockData
import mx.edu.utng.bgma.smarthealthmonitor.ui.theme.SmartHealthMonitorTheme
import mx.edu.utng.bgma.smarthealthmonitor.ui.viewmodel.DashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onHistorialClick: () -> Unit = {},
    onAlertClick: () -> Unit = {},
    // PASO 5: Inyección del ViewModel
    viewModel: DashboardViewModel = viewModel()
) {
    // PASO 5: Conversión de StateFlow a State de Compose
    val fc by viewModel.fc.collectAsState()
    val pasos by viewModel.pasos.collectAsState()
    val historial = viewModel.historial // Si es una lista fija o StateFlow también

    SmartHealthMonitorTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "SmartHealth",
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor    = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick       = onAlertClick,
                    containerColor = MaterialTheme.colorScheme.error
                ) {
                    Icon(
                        imageVector       = Icons.Default.Warning,
                        contentDescription = "Enviar alerta de emergencia",
                        tint              = MaterialTheme.colorScheme.onError
                    )
                }
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding        = PaddingValues(16.dp),
                verticalArrangement   = Arrangement.spacedBy(12.dp)
            ) {
                // ── Tarjeta FC (Ahora usa el valor real del ViewModel) ──
                item {
                    TarjetaDato(
                        valor      = "$fc",
                        unidad     = "bpm",
                        label      = "Frecuencia cardíaca",
                        colorValor = MaterialTheme.colorScheme.error,
                        esNormal   = fc in 60..100
                    )
                }
                // ── Tarjeta Pasos (Ahora usa el valor real del ViewModel) ──
                item {
                    TarjetaDato(
                        valor      = "%,d".format(pasos),
                        unidad     = "pasos",
                        label      = "Pasos del día",
                        colorValor = MaterialTheme.colorScheme.primary
                    )
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        Text("Historial reciente",
                            style = MaterialTheme.typography.titleMedium)
                        TextButton(onClick = onHistorialClick) {
                            Text("Ver todo")
                        }
                    }
                }

                items(historial, key = { it.id }) { lectura ->
                    FilaHistorial(lectura = lectura)
                }
            }
        }
    }
}

// Nota: Para que la Preview funcione, podrías necesitar pasar un ViewModel falso
// o dejar los parámetros por defecto si el ViewModel tiene valores iniciales.
@Preview(showBackground = true, name = "Dashboard - Light",
    showSystemUi = true, device = "id:pixel_6")
@Composable
private fun DashboardScreenPreview() {
    SmartHealthMonitorTheme {
        DashboardScreen()
    }
}