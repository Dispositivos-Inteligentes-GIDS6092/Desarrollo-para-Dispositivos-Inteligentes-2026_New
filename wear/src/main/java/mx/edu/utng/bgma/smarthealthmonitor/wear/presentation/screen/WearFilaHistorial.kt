package mx.edu.utng.bgma.smarthealthmonitor.wear.presentation.screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Text
import androidx.compose.ui.unit.dp

@Composable
fun WearFilaHistorial(lectura: LecturaFC) {
    val color = if (lectura.esNormal) {
        Color.Green
    } else {
        Color.Red
    }

    Chip(
        label = {
            Text(
                "${lectura.valorBpm} bpm",
                color = color
            )
        },
        secondaryLabel = {
            Text(lectura.hora)
        },
        onClick = { /* Acción al hacer clic */ },
        colors = ChipDefaults.secondaryChipColors(),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
    )
}

// Data class para LecturaFC (si no existe)
data class LecturaFC(
    val id: String = "",
    val valorBpm: Int,
    val hora: String,
    val esNormal: Boolean = true
)