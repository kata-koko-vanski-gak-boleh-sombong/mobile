package com.project.edu_law.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.project.edu_law.data.entity.ScenarioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScenarioDao {
    @Query("SELECT * FROM scenarios")
    fun getAllScenarios(): Flow<List<ScenarioEntity>>

    @Query("SELECT * FROM scenarios WHERE scenario_id = :id")
    suspend fun getScenarioById(id: String): ScenarioEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScenarios(scenarios: List<ScenarioEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScenario(scenario: ScenarioEntity)
}