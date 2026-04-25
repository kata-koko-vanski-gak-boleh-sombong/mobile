package com.project.edu_law.data.repository

import android.util.Log
import com.project.edu_law.data.entity.HistoryEntity
import com.project.edu_law.data.entity.ScenarioEntity
import com.project.edu_law.data.entity.toEntity
import com.project.edu_law.data.local.HistoryDao
import com.project.edu_law.data.local.ScenarioDao
import com.project.edu_law.data.remote.ApiClient
import kotlinx.coroutines.flow.Flow

class ScenarioRepository(
    private val scenarioDao: ScenarioDao,
    private val historyDao: HistoryDao
) {

    fun getAllScenarios(): Flow<List<ScenarioEntity>> {
        return scenarioDao.getAllScenarios()
    }

    suspend fun getScenarioById(id: String): ScenarioEntity? {
        return scenarioDao.getScenarioById(id)
    }

    suspend fun insertScenarios(scenarios: List<ScenarioEntity>) {
        scenarioDao.insertScenarios(scenarios)
    }

    suspend fun saveHistory(history: HistoryEntity) {
        historyDao.insertHistory(history)
    }

    fun getAllHistory(): Flow<List<HistoryEntity>> {
        return historyDao.getAllHistory()
    }

    suspend fun syncScenariosFromApi() {
        try {
            val count = scenarioDao.getScenarioCount()

            if (count == 0) {
                Log.d("SYNC_API", "Database kosong. Memulai fetch dari API...")

                val response = ApiClient.instance.getAllScenarios()

                if (response.isSuccessful && response.body() != null) {
                    val scenarioDataList = response.body()!!.data

                    val entities = scenarioDataList.map { it.toEntity() }

                    scenarioDao.insertScenarios(entities)
                    Log.d("SYNC_API", "Berhasil menyimpan ${entities.size} data dari API ke DB lokal!")
                } else {
                    Log.e("SYNC_API", "Gagal fetch API. Error Code: ${response.code()}")
                }
            } else {
                Log.d("SYNC_API", "Database sudah terisi ($count data). Skip fetch API.")
            }
        } catch (e: Exception) {
            Log.e("SYNC_API", "Error Jaringan: ${e.message}")
            e.printStackTrace()
        }
    }
}