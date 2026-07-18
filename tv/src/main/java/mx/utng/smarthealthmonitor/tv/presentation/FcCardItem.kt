package mx.utng.smarthealthmonitor.tv.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.tv.material3.*
import mx.edu.utng.bgma.smarthealthmonitor.data.db.LecturaFC

val LecturaFC.estado: String get() = if (esNormal) "Normal" else "Alerta"

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun FcCardItem(
    lectura: LecturaFC,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.width(200.dp).height(120.dp),
        colors = ClickableSurfaceDefaults.colors(
            containerColor = Color(0xFF1565C0), // azul sin foco
            focusedContainerColor = Color(0xFF42A5F5), // azul claro con foco D-pad
            pressedContainerColor = Color(0xFF0D47A1)
        ),
        shape = ClickableSurfaceDefaults.shape(RoundedCornerShape(12.dp))
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${lectura.valorBpm} bpm",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Column {
                Text(
                    text = lectura.estado,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
                Text(
                    text = lectura.hora,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.6f)
                )
            }
        }
    }
}
