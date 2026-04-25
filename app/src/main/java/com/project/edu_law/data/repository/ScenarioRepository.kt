package com.project.edu_law.data.repository

import android.util.Log
import com.project.edu_law.data.entity.HistoryEntity
import com.project.edu_law.data.entity.ScenarioEntity
import com.project.edu_law.data.entity.toEntity
import com.project.edu_law.data.local.HistoryDao
import com.project.edu_law.data.local.ScenarioDao
import com.project.edu_law.data.remote.ApiClient
import com.project.edu_law.data.remote.AskRequest
import com.project.edu_law.data.remote.GenerateRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ScenarioRepository(
    private val scenarioDao: ScenarioDao,
    private val historyDao: HistoryDao
) {
    private val repositoryScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

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
            Log.d("SYNC_API", "Memulai fetch full data dari API...")

            val response = ApiClient.instance.getAllScenarios()

            if (response.isSuccessful && response.body() != null) {
                val scenarioDataList = response.body()!!.data

                val entities = scenarioDataList.map { it.toEntity() }

                scenarioDao.insertScenarios(entities)

                Log.d("SYNC_API", "Berhasil sinkronisasi! Menyimpan/Update ${entities.size} data ke DB lokal.")
            } else {
                Log.e("SYNC_API", "Gagal fetch API. Error Code: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("SYNC_API", "Error Jaringan: ${e.message}")
            e.printStackTrace()
        }
    }

    suspend fun generateNewSimulation(userId: String, difficulty: String, character: String): Result<String> {
        return try {
            val request = GenerateRequest(
                id_user = userId,
                difficulty = difficulty,
                prompt = character
            )

            val response = ApiClient.instance.generateSimulation(request)

            if (response.isSuccessful && response.body() != null) {
                val newScenarioData = response.body()!!.data
                val entity = newScenarioData.toEntity()

                scenarioDao.insertScenario(entity)

                Result.success(entity.id)
            } else {
                Result.failure(Exception("Gagal generate. Kode: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun generateNewSimulationAsync(
        userId: String,
        difficulty: String,
        character: String,
        onResult: (Result<String>) -> Unit
    ) {
        repositoryScope.launch {
            try {
                Log.d("GENERATE_API", "Meminta AI membuat skenario... (Bisa memakan waktu hingga 5 menit)")

                val request = GenerateRequest(
                    id_user = userId,
                    difficulty = difficulty,
                    prompt = character
                )

                val response = ApiClient.instance.generateSimulation(request)

                if (response.isSuccessful && response.body() != null) {
                    val newScenarioData = response.body()!!.data
                    val entity = newScenarioData.toEntity()

                    scenarioDao.insertScenario(entity)
                    Log.d("GENERATE_API", "Berhasil! Skenario disimpan.")

                    onResult(Result.success(entity.id))
                } else {
                    Log.e("GENERATE_API", "Gagal. Kode: ${response.code()}")
                    onResult(Result.failure(Exception("Gagal generate dari server. Kode: ${response.code()}")))
                }
            } catch (e: Exception) {
                Log.e("GENERATE_API", "Error: ${e.message}")
                onResult(Result.failure(e))
            }
        }
    }

    suspend fun askAiQuestion(userId: String, formattedPrompt: String): Result<String> {
        return try {
            val request = AskRequest(id_user = userId, prompt = formattedPrompt)
            val response = ApiClient.instance.askAi(request)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.data.response)
            } else {
                Result.failure(Exception("Gagal mendapat respon AI. Kode: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}