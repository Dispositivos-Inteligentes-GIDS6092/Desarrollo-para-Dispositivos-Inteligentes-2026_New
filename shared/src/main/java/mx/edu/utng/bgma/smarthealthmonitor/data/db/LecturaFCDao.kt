package mx.edu.utng.bgma.smarthealthmonitor.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface LecturaFCDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(lectura: LecturaFC)

    @Query("SELECT * FROM lecturas_fc ORDER BY timestamp DESC LIMIT 50")
    fun obtenerUltimas(): Flow<List<LecturaFC>>

    @Query("SELECT * FROM lecturas_fc WHERE id = :id")
    suspend fun obtenerPorId(id: Int): LecturaFC?

    @Query("SELECT * FROM lecturas_fc ORDER BY timestamp DESC LIMIT 5")
    suspend fun obtenerUltimos5(): List<LecturaFC>

    @Query("SELECT COUNT(*) FROM lecturas_fc")
    suspend fun contarRegistros(): Int

    @Query("DELETE FROM lecturas_fc WHERE timestamp < :limite")
    suspend fun limpiarViejos(limite: Long)
}
