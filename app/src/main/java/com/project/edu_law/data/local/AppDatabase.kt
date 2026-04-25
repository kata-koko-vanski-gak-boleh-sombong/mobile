package com.project.edu_law.data.local

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.Gson
import com.project.edu_law.data.ScenarioData
import com.project.edu_law.data.entity.ScenarioEntity
import com.project.edu_law.data.entity.toEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [ScenarioEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scenarioDao(): ScenarioDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Pastikan signature-nya seperti ini:
        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "edu_law_db"
                )
                    .addCallback(AppDatabaseCallback(context, scope)) // Scope ini diteruskan ke callback
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class AppDatabaseCallback(
        private val context: Context, // Kita butuh context untuk buka assets
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            Log.d("DB_SEED", "onCreate Database terpanggil!")

            // Jangan pakai INSTANCE? di sini karena mungkin belum fully initialized
            // Kita gunakan scope dan akses database dari provider
            scope.launch(Dispatchers.IO) {
                INSTANCE?.let { database ->
                    populateDatabase(database.scenarioDao())
                } ?: Log.e("DB_SEED", "Database INSTANCE masih null saat onCreate")
            }
        }

        suspend fun populateDatabase(dao: ScenarioDao) {
            try {
                Log.d("DB_SEED", "Membaca file dari assets...")
                val jsonString = context.assets.open("scenarios_seed.json")
                    .bufferedReader().use { it.readText() }

                val gson = Gson()
                val scenarioData = gson.fromJson(jsonString, ScenarioData::class.java)

                if (scenarioData != null) {
                    dao.insertScenario(scenarioData.toEntity())
                    Log.d("DB_SEED", "Seeding Berhasil ke Database!")
                }
            } catch (e: Exception) {
                Log.e("DB_SEED", "Seeding Gagal: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}