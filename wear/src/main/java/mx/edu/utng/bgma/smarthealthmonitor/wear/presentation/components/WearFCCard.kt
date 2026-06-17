package mx.edu.utng.bgma.smarthealthmonitor.wear.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Card
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text

@Composable
fun WearFCCard(
    fc: Int,
    pasos: Int,
    modifier: Modifier = Modifier
) {
    val colorFC = if (fc in 60..100) {
        MaterialTheme.colors.primary
    } else {
        MaterialTheme.colors.error
    }

    Card(
        onClick = { },
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "FC",
                    tint = colorFC,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "Ritmo Cardíaco",
                    fontSize = 12.sp,
                    color = MaterialTheme.colors.onSurfaceVariant
                )
            }
            
            Text(
                text = "$fc",
                style = MaterialTheme.typography.display3,
                color = colorFC,
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp
            )
            
            Text(
                text = "bpm",
                fontSize = 10.sp,
                color = MaterialTheme.colors.onSurfaceVariant
            )

            Spacer(Modifier.padding(vertical = 4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.DirectionsRun,
                    contentDescription = "Pasos",
                    tint = Color.Cyan,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "$pasos pasos",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
