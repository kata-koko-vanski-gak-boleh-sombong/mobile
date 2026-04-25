package com.project.edu_law.data.repository

import com.project.edu_law.data.entity.HistoryEntity
import com.project.edu_law.data.entity.ScenarioEntity
import com.project.edu_law.data.local.HistoryDao
import com.project.edu_law.data.local.ScenarioDao
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
}