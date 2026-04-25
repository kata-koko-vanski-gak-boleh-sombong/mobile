package com.project.edu_law.data.repository

import com.project.edu_law.data.entity.ScenarioEntity
import com.project.edu_law.data.local.ScenarioDao
import kotlinx.coroutines.flow.Flow

class ScenarioRepository(private val scenarioDao: ScenarioDao) {

    /**
     * Mengambil semua skenario dalam bentuk Flow.
     * Flow akan otomatis memberikan update jika data di database berubah.
     */
    fun getAllScenarios(): Flow<List<ScenarioEntity>> {
        return scenarioDao.getAllScenarios()
    }

    /**
     * Mencari skenario spesifik berdasarkan ID.
     * Menggunakan 'suspend' karena ini adalah operasi pembacaan tunggal.
     */
    suspend fun getScenarioById(id: String): ScenarioEntity? {
        return scenarioDao.getScenarioById(id)
    }

    /**
     * Menyimpan daftar skenario baru ke dalam database.
     */
    suspend fun insertScenarios(scenarios: List<ScenarioEntity>) {
        scenarioDao.insertScenarios(scenarios)
    }
}