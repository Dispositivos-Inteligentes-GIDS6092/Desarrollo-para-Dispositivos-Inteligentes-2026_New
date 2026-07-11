package mx.utng.smarthealthmonitor.tv

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import mx.edu.utng.bgma.smarthealthmonitor.data.db.SmartHealthDB

class TvViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TvViewModel::class.java)) {
            val dao = SmartHealthDB.getDatabase(context).lecturaDao()
            return TvViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
