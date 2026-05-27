package mx.edu.utng.bgma.smarthealthmonitor.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
// Importamos tus modelos y datos de prueba
import mx.edu.utng.bgma.smarthealthmonitor.data.models.MockData
import mx.edu.utng.bgma.smarthealthmonitor.data.models.LecturaFC
import mx.edu.utng.bgma.smarthealthmonitor.ui.theme.SmartHealthMonitorTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialScreen(onBack: () -> Unit) {
    SmartHealthMonitorTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Historial",
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Regresar"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(MockData.historialFC) { lectura ->
                    FilaHistorial(lectura)
                }
            }
        }
    }
}

// Ahora que ya tiene el tema, el Preview se verá exactamente como en el dispositivo
@Preview(showBackground = true, name = "Historial - Light")
@Composable
private fun HistorialScreen() {
    HistorialScreen(onBack = {})
}


@Composable
fun FilaHistorial(lectura: LecturaFC) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            // El color cambia según esNormal (Verde claro si es normal, rojo claro si no)
            containerColor = if (lectura.esNormal) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Hora: ${lectura.hora}",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray
                )
                Text(
                    text = "${lectura.valorBpm} BPM",
                    style = MaterialTheme.typography.headlineSmall,
                    color = if (lectura.esNormal) Color(0xFF2E7D32) else Color(0xFFC62828)
                )
            }

            // Etiqueta de estado
            Surface(
                shape = MaterialTheme.shapes.small,
                color = if (lectura.esNormal) Color(0xFF2E7D32) else Color(0xFFC62828)
            ) {
                Text(
                    text = if (lectura.esNormal) "NORMAL" else "ALERTA",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    color = Color.White,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}