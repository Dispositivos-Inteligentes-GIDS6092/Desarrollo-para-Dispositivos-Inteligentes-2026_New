package mx.utng.smarthealthmonitor.tv.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import androidx.tv.material3.*

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun TvPlaybackScreen(
    navController: NavController,
) {
    val context = LocalContext.current
    
    // Crear ExoPlayer con Media3 para audio/video de alerta
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            // Usando el audio de alerta de la guía oficial
            val mediaItem = MediaItem.fromUri(
                "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"
            )
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true // Iniciar automáticamente
        }
    }
    
    // Liberar recursos al salir
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Reproductor - Si es solo audio, mostrará una carátula genérica o controles
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                    useController = true
                    // Asegurar que el fondo sea negro si es video
                    setBackgroundColor(android.graphics.Color.BLACK)
                }
            },
            modifier = Modifier.fillMaxSize()
        )
        
        // Botón SALIR (Back) en la esquina superior izquierda
        Surface(
            onClick = { 
                exoPlayer.stop()
                navController.popBackStack() 
            },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(32.dp),
            colors = ClickableSurfaceDefaults.colors(
                containerColor = Color(0x88000000),
                focusedContainerColor = Color(0xCCFFFFFF)
            )
        ) {
            Text(
                text = "← SALIR",
                color = Color.White,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}
