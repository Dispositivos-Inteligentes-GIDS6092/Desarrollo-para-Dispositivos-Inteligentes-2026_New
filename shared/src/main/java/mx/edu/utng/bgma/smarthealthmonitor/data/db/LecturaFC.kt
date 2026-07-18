package mx.edu.utng.bgma.smarthealthmonitor.data.db

import androidx.room.*
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "lecturas_fc")
data class LecturaFC(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val valorBpm: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val hora: String = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date()),
    val fecha: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
    val esNormal: Boolean = valorBpm in 60..100,
    val estado: String = if (valorBpm in 60..100) "Normal" else "Alerta",
    val dispositivo: String = "Mobile", // Wearable, Mobile, TV
    val sincronizado: Boolean = false
)
