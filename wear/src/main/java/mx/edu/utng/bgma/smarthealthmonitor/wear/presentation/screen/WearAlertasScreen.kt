package mx.edu.utng.bgma.smarthealthmonitor.wear.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.foundation.lazy.AutoCenteringParams
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.CompactChip
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.scrollAway
import kotlinx.coroutines.launch
import mx.edu.utng.bgma.smarthealthmonitor.data.SmartHealthRepository
import mx.edu.utng.bgma.smarthealthmonitor.wear.HealthDataService
import mx.edu.utng.bgma.smarthealthmonitor.wear.presentation.WearDashboardViewModel
import mx.edu.utng.bgma.smarthealthmonitor.wear.presentation.components.WearFCCard
import kotlin.random.Random

@Composable
fun WearAlertasScreen(
    fc: Int,
    onConfirmar: () -> Unit,
    onCancelar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
    ) {
        Text(
            text = "FC: $fc bpm",
            style = MaterialTheme.typography.title3,
            color = MaterialTheme.colors.error
        )
        Text(
            text = "¿Enviar alerta?",
            style = MaterialTheme.typography.body2
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onConfirmar,
                modifier = Modifier.size(52.dp),
                colors = ButtonDefaults.primaryButtonColors(
                    backgroundColor = MaterialTheme.colors.error
                )
            ) {
                Text("✓", fontSize = 24.sp)
            }

            Button(
                onClick = onCancelar,
                modifier = Modifier.size(52.dp)
            ) {
                Text("✗", fontSize = 24.sp)
            }
        }
    }
}

@Composable
fun WearDashboardScreen(
    onAlertClick: () -> Unit,
    onHistorialClick: () -> Unit,
    viewModel: WearDashboardViewModel = viewModel()
) {
    val fc by viewModel.fc.collectAsState()
    val pasos by viewModel.pasos.collectAsState()
    val listState = rememberScalingLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        timeText = {
            TimeText(modifier = Modifier.scrollAway(listState))
        },
        positionIndicator = {
            PositionIndicator(scalingLazyListState = listState)
        },
        modifier = Modifier.fillMaxSize()
    ) {
        ScalingLazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                top = 32.dp,
                start = 8.dp,
                end = 8.dp,
                bottom = 32.dp
            ),
            autoCentering = AutoCenteringParams(itemIndex = 0)
        ) {
            item {
                WearFCCard(
                    fc = fc,
                    pasos = pasos
                )
            }

            item {
                Button(
                    onClick = {
                        val randomFC = Random.nextInt(60, 150)
                        coroutineScope.launch {
                            SmartHealthRepository.actualizarFC(randomFC)
                            HealthDataService.enviarFCDirectamente(context, randomFC)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.primaryButtonColors()
                ) {
                    Text("Enviar datos")
                }
            }

            item {
                Chip(
                    label = { Text("⚠️ Alerta") },
                    onClick = onAlertClick,
                    colors = ChipDefaults.primaryChipColors(
                        backgroundColor = MaterialTheme.colors.error
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Chip(
                    label = { Text("📋 Historial") },
                    onClick = onHistorialClick,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                CompactChip(
                    label = {
                        Text(if (pasos > 0) "$pasos pasos" else "— pasos")
                    },
                    onClick = { /* Opcional */ },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
