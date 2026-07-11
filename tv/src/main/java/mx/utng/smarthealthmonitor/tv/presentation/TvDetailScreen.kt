package mx.utng.smarthealthmonitor.tv.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.tv.material3.*
import mx.utng.smarthealthmonitor.tv.TvViewModel
import mx.utng.smarthealthmonitor.tv.TvViewModelFactory

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun TvDetailScreen(
    lecturaId: Int,
    navController: NavController,
    viewModel: TvViewModel = viewModel(factory = TvViewModelFactory(LocalContext.current)),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val lectura = state.lecturas.find { it.id == lecturaId } ?: return
    
    // FocusRequester para mover el foco al primer botón al entrar
    val firstBtnFocus = remember { FocusRequester() }
    
    LaunchedEffect(Unit) {
        firstBtnFocus.requestFocus()
    }
    
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D1117))
            .padding(64.dp),
        horizontalArrangement = Arrangement.spacedBy(48.dp)
    ) {
        // Panel izquierdo - ícono + datos
        Column(
            modifier = Modifier.weight(0.4f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Ícono de corazón
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .background(
                        color = if (lectura.esNormal) Color(0xFF1B5E20) else Color(0xFFB3261E),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("❤", fontSize = 80.sp, color = Color.White)
            }
            
            Text(
                text = "${lectura.valorBpm} BPM",
                style = MaterialTheme.typography.displayMedium,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold
            )
            
            Text(
                text = "Estado: ${if (lectura.esNormal) "✓ Normal" else "⚠ Alerta"}",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White.copy(alpha = 0.8f)
            )
            
            Text(
                text = "Hora: ${lectura.hora}",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.6f)
            )
        }
        
        // Panel derecho - botones de acción
        Column(
            modifier = Modifier.weight(0.6f),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))
            
            // Botón Reproducir
            Surface(
                onClick = { 
                    navController.navigate("playback") 
                },
                modifier = Modifier
                    .focusRequester(firstBtnFocus)
                    .fillMaxWidth(0.8f)
                    .height(70.dp),
                colors = ClickableSurfaceDefaults.colors(
                    containerColor = Color(0xFF1B5E20),
                    focusedContainerColor = Color(0xFF43A047)
                ),
                shape = ClickableSurfaceDefaults.shape(RoundedCornerShape(12.dp))
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "▶ REPRODUCIR ALERTA",
                        color = Color.White,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            // Botón Volver
            Surface(
                onClick = { 
                    navController.popBackStack() 
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(70.dp),
                colors = ClickableSurfaceDefaults.colors(
                    containerColor = Color(0xFF37474F),
                    focusedContainerColor = Color(0xFF546E7A)
                ),
                shape = ClickableSurfaceDefaults.shape(RoundedCornerShape(12.dp))
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "← VOLVER",
                        color = Color.White,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
