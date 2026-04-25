package com.project.edu_law.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_table")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val scenarioId: String,
    val scenarioTitle: String,
    val fundamentalRights: Int,
    val criminalJustice: Int,
    val civilJustice: Int,
    val corruption: Int,
    val endingType: String,
    val timestamp: Long = System.currentTimeMillis()
)