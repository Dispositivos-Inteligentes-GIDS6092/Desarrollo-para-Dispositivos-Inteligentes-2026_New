package mx.utng.smarthealthmonitor.tv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import mx.utng.smarthealthmonitor.tv.presentation.TvCatalogScreen
import mx.utng.smarthealthmonitor.tv.presentation.TvDetailScreen
import mx.utng.smarthealthmonitor.tv.presentation.TvPlaybackScreen
import mx.utng.smarthealthmonitor.tv.ui.theme.SmartHealthTvTheme

class TVActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            SmartHealthTvTheme {
                val navController = rememberNavController()
                
                NavHost(
                    navController = navController,
                    startDestination = "catalog"
                ) {
                    // Ruta: Catálogo (lista de cards)
                    composable("catalog") {
                        TvCatalogScreen(
                            onCardClick = { lecturaId ->
                                navController.navigate("detail/$lecturaId")
                            }
                        )
                    }
                    
                    // Ruta: Detalle (con parámetro lecturaId)
                    composable(
                        route = "detail/{lecturaId}",
                        arguments = listOf(
                            navArgument("lecturaId") { 
                                type = NavType.IntType 
                            }
                        )
                    ) { backStackEntry ->
                        val id = backStackEntry.arguments?.getInt("lecturaId") ?: return@composable
                        TvDetailScreen(
                            lecturaId = id,
                            navController = navController
                        )
                    }
                    
                    // Ruta: Reproductor
                    composable("playback") {
                        TvPlaybackScreen(
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}
