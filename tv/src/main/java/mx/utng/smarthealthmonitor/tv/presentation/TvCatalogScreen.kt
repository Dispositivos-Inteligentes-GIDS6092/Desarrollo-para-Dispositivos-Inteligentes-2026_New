package mx.utng.smarthealthmonitor.tv.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.foundation.lazy.list.items
import androidx.tv.material3.*

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun TvCatalogScreen(
    onCardClick: (Int) -> Unit,
    viewModel: TvViewModel = viewModel(factory = TvViewModelFactory(LocalContext.current))
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Box(Modifier.fillMaxSize().background(Color(0xFF0D1B4A))) {
        if (state.isLoading) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
            return@Box
        }

        // Mensaje de éxito flotante
        if (state.showMqttSuccess) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(32.dp)
                    .background(Color(0xFF4CAF50), shape = MaterialTheme.shapes.medium)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "✅ Conexión MQTT Exitosa",
                    color = Color.White,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }

        TvLazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 48.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Fila de Botones de Simulación Discretos
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    androidx.tv.material3.Surface(
                        onClick = { viewModel.simularMensajeRecibido() },
                        modifier = Modifier.size(20.dp),
                        shape = ClickableSurfaceDefaults.shape(CircleShape),
                        colors = ClickableSurfaceDefaults.colors(
                            containerColor = Color(0xFF1A237E),
                            focusedContainerColor = Color(0xFF3F51B5)
                        )
                    ) { }
                }
            }

            // Fila 1: FC actual
            item {
                RowSection(title = "⚡ Estado Actual — ${state.fcActual} bpm") {
                    TvLazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(state.lecturas.takeLast(3)) { lectura ->
                            FcCardItem(
                                lectura = lectura,
                                onClick = { onCardClick(lectura.id) }
                            )
                        }
                    }
                }
            }
            // Fila 2: Historial completo
            item {
                RowSection(title = "   Historial FC") {
                    TvLazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(state.lecturas) { lectura ->
                            FcCardItem(
                                lectura = lectura,
                                onClick = { onCardClick(lectura.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RowSection(title: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = title, 
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White, 
            fontWeight = FontWeight.Bold
        )
        content()
    }
}
