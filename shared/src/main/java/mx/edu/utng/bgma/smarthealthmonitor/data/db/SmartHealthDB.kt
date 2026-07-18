package mx.edu.utng.bgma.smarthealthmonitor.data.db

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [LecturaFC::class],
    version  = 2,
    exportSchema = false
)
abstract class SmartHealthDB : RoomDatabase() {
    abstract fun lecturaDao(): LecturaFCDao

    companion object {
        @Volatile
        private var INSTANCE: SmartHealthDB? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Agregar columnas nuevas a la tabla existente
                database.execSQL("ALTER TABLE lecturas_fc ADD COLUMN fecha TEXT NOT NULL DEFAULT ''")
                database.execSQL("ALTER TABLE lecturas_fc ADD COLUMN estado TEXT NOT NULL DEFAULT 'Normal'")
                database.execSQL("ALTER TABLE lecturas_fc ADD COLUMN dispositivo TEXT NOT NULL DEFAULT 'Mobile'")
                database.execSQL("ALTER TABLE lecturas_fc ADD COLUMN sincronizado INTEGER NOT NULL DEFAULT 0")
            }
        }

        fun getDatabase(context: Context): SmartHealthDB {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    SmartHealthDB::class.java,
                    "smarthealthmonitor_db"
                )
                .addMigrations(MIGRATION_1_2)
                .fallbackToDestructiveMigration()
                .build().also { INSTANCE = it }
            }
        }
    }
}
