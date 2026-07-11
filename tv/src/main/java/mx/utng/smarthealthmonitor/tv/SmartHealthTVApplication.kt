package mx.utng.smarthealthmonitor.tv

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mx.edu.utng.bgma.smarthealthmonitor.data.SmartHealthRepository

class SmartHealthTVApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        SmartHealthRepository.init(this)
        
        // Generar datos de prueba para la TV si está vacío
        CoroutineScope(Dispatchers.IO).launch {
            SmartHealthRepository.generarDatosDePrueba()
        }
    }
}
