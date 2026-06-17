package mx.edu.utng.bgma.smarthealthmonitor.wear.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import mx.edu.utng.bgma.smarthealthmonitor.wear.presentation.screen.WearAlertasScreen
import mx.edu.utng.bgma.smarthealthmonitor.wear.presentation.screen.WearDashboardScreen
import mx.edu.utng.bgma.smarthealthmonitor.wear.presentation.screen.WearHistorialScreen

object WearScreens {
    const val DASHBOARD = "wear_dashboard"
    const val ALERTA = "wear_alerta"
    const val HISTORIAL = "wear_historial"  // ← NUEVO
}

@Composable
fun SmartHealthWearNavGraph() {
    val navController = rememberSwipeDismissableNavController()
    val viewModel: WearDashboardViewModel = viewModel()

    SwipeDismissableNavHost(
        navController = navController,
        startDestination = WearScreens.DASHBOARD
    ) {
        composable(WearScreens.DASHBOARD) {
            WearDashboardScreen(
                onAlertClick = {
                    navController.navigate(WearScreens.ALERTA)
                },
                onHistorialClick = {  // ← NUEVO
                    navController.navigate(WearScreens.HISTORIAL)
                }
            )
        }

        composable(WearScreens.ALERTA) {
            val fc by viewModel.fc.collectAsState()
            WearAlertasScreen(
                fc = fc,
                onConfirmar = {
                    // Aquí puedes implementar el envío real de la alerta
                    navController.popBackStack()
                },
                onCancelar = {
                    navController.popBackStack()
                }
            )
        }

        // ← NUEVO: Destino Historial
        composable(WearScreens.HISTORIAL) {
            WearHistorialScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}