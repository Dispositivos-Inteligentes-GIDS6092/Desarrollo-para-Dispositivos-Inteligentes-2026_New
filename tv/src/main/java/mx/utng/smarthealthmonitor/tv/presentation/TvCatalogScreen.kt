package mx.utng.smarthealthmonitor.tv.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.foundation.lazy.list.items
import androidx.tv.material3.*
import kotlinx.coroutines.launch
import mx.edu.utng.bgma.smarthealthmonitor.data.db.LecturaFC
import mx.utng.smarthealthmonitor.tv.TvViewModel
import mx.utng.smarthealthmonitor.tv.TvViewModelFactory

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun TvCatalogScreen(
    onCardClick: (Int) -> Unit,
    viewModel: TvViewModel = viewModel(factory = TvViewModelFactory(LocalContext.current))
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF0D1117))) {
        // --- BARRA AZUL DELGADA (IZQUIERDA) ---
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(12.dp)
                .background(Color(0xFF1B4F8A))
                .align(Alignment.CenterStart)
        )

        // --- CONTENIDO PRINCIPAL ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 48.dp, top = 40.dp, end = 32.dp)
        ) {
            // Título Superior Derecha (Alineado)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "SmartHealth TV",
                    style = MaterialTheme.typography.displayMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Light,
                    fontSize = 42.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Lista de Filas (Historial y Alertas)
            TvLazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(40.dp),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                // SECCIÓN 1: Historial FC
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text(
                            "Historial FC",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Normal
                        )
                        TvLazyRow(
                            horizontalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            items(state.lecturas) { lectura ->
                                LecturaCard(
                                    lectura = lectura,
                                    isAlertRow = false,
                                    onClick = { onCardClick(lectura.id) }
                                )
                            }
                        }
                    }
                }

                // SECCIÓN 2: Alertas recientes
                item {
                    val alertas = state.lecturas.filter { !it.esNormal }
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text(
                            "Alertas recientes",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Normal
                        )
                        if (alertas.isEmpty()) {
                            Text("No hay alertas recientes", color = Color.Gray, fontSize = 16.sp)
                        } else {
                            TvLazyRow(
                                horizontalArrangement = Arrangement.spacedBy(20.dp)
                            ) {
                                items(alertas) { lectura ->
                                    LecturaCard(
                                        lectura = lectura,
                                        isAlertRow = true,
                                        onClick = { onCardClick(lectura.id) }
                                    )
                                }
                            }
                        }
                    }
                }
                
                // Botón de simulación al final (para no estorbar)
                item {
                    Button(
                        onClick = {
                            scope.launch {
                                mx.edu.utng.bgma.smarthealthmonitor.data.SmartHealthRepository.actualizarFC((60..150).random())
                            }
                        },
                        modifier = Modifier.padding(top = 20.dp)
                    ) {
                        Text("Simular Nueva Lectura", fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun LecturaCard(
    lectura: LecturaFC,
    isAlertRow: Boolean,
    onClick: () -> Unit
) {
    // Colores exactos de la imagen
    val topColor = if (lectura.esNormal) Color(0xFFD9D9D9) else Color.Red
    val bottomColor = if (isAlertRow) Color(0xFF5C1A1A) else Color(0xFF1B4F8A)

    Card(
        onClick = onClick,
        modifier = Modifier.size(width = 180.dp, height = 150.dp),
        colors = CardDefaults.colors(
            containerColor = Color.Transparent
        ),
        shape = CardDefaults.shape(RectangleShape)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Parte superior: Bloque de color (Gris o Rojo)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.6f)
                    .background(topColor)
            )
            // Parte inferior: Bloque de datos (Azul o Rojo Oscuro)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.4f)
                    .background(bottomColor)
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${lectura.valorBpm} BPM",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Hora: ${lectura.hora}",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 10.sp
                )
            }
        }
    }
}
