package mx.edu.utng.bgma.smarthealthmonitor.wear.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Card
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text

@Composable
fun WearFCCard(
    fc: Int,
    modifier: Modifier = Modifier,
    pasos: Int
) {
    val colorFC = if (fc in 60..100) {
        MaterialTheme.colors.primary
    } else {
        MaterialTheme.colors.error
    }

    Card(
        onClick = { },
        modifier = modifier.fillMaxWidth()  // Cambiar a fillMaxWidth
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),  // Más padding para mejor visualización
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Frecuencia Cardíaca",
                fontSize = 16.sp,
                color = MaterialTheme.colors.onSurfaceVariant
            )
            Text(
                text = "$fc",
                style = MaterialTheme.typography.display3,
                color = colorFC,
                fontWeight = FontWeight.Bold,
                fontSize = 48.sp  // Tamaño más grande para destacar
            )
            Text(
                text = "bpm",
                style = MaterialTheme.typography.caption3,
                color = MaterialTheme.colors.onSurfaceVariant
            )
        }
    }
}