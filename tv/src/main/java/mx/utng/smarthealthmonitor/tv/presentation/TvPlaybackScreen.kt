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
fun TvPlaybackScreen(navController: NavController) {
    val ctx = LocalContext.current

    // Crear ExoPlayer optimizado para el emulador
    val exoPlayer = remember {
        ExoPlayer.Builder(ctx).build().apply {
            // Video de prueba estable con sonido (Big Buck Bunny)
            val mediaItem = MediaItem.fromUri(
                "https://storage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
            )
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true
            volume = 1.0f // Sonido al máximo
        }
    }

    // Liberar recursos al salir
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    Box(Modifier.fillMaxSize().background(Color.Black)) {
        // Vista del reproductor
        AndroidView(
            factory = { context ->
                PlayerView(context).apply {
                    player = exoPlayer
                    useController = true
                    setKeepScreenOn(true) // Evita que la TV se apague
                }
            },
            modifier = Modifier.fillMaxSize()
        )
        
        // Botón Volver (UI idéntica a la imagen)
        Surface(
            onClick = { 
                exoPlayer.stop()
                navController.popBackStack() 
            },
            modifier = Modifier.align(Alignment.TopStart).padding(24.dp),
            colors = ClickableSurfaceDefaults.colors(
                containerColor = Color(0x88000000),
                focusedContainerColor = Color(0xCCFFFFFF)
            )
        ) {
            Text("← Volver", color = Color.White, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
        }
    }
}
