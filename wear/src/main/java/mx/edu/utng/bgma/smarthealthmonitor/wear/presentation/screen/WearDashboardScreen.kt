package mx.edu.utng.bgma.smarthealthmonitor.wear.presentation.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.scrollAway
import androidx.compose.ui.graphics.Color
import mx.edu.utng.bgma.smarthealthmonitor.wear.presentation.WearDashboardViewModel
import mx.edu.utng.bgma.smarthealthmonitor.wear.presentation.components.WearFCCard
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState

@Composable
fun WearDashboardScreen(
    onAlertClick: () -> Unit,
    onHistorialClick: () -> Unit,  // ← NUEVO
    viewModel: WearDashboardViewModel = viewModel()
) {
    val fc by viewModel.fc.collectAsState()
    val pasos by viewModel.pasos.collectAsState()
    val listState = rememberScalingLazyListState()

    Scaffold(
        timeText = {
            TimeText(modifier = Modifier.scrollAway(listState))
        },
        positionIndicator = {
            PositionIndicator(scalingLazyListState = listState)
        }
    ) {
        ScalingLazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            // Tarjeta de FC
            item {
                WearFCCard(
                    fc = fc,
                    pasos = pasos,
                    modifier = Modifier.padding(8.dp)
                )
            }

            // Botón de Alerta (rojo)
            item {
                Chip(
                    label = {
                        Text(
                            "🚨 Alerta",
                            color = Color.White
                        )
                    },
                    onClick = onAlertClick,
                    colors = ChipDefaults.primaryChipColors(
                        backgroundColor = Color(0xFFE53935)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                )
            }

            // ← NUEVO: Botón de Historial
            item {
                Chip(
                    label = {
                        Text("📋 Historial")
                    },
                    onClick = onHistorialClick,
                    colors = ChipDefaults.secondaryChipColors(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                )
            }
        }
    }
}