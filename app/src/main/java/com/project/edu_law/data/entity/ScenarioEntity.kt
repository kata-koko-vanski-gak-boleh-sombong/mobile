package com.project.edu_law.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.project.edu_law.data.ScenarioData
import com.project.edu_law.data.local.Converters

@Entity(tableName = "scenarios")
@TypeConverters(Converters::class)
data class ScenarioEntity(
    @PrimaryKey
    val id: String,

    val scenario_id: String,
    val title: String,
    val subtitle: String,
    val character: String,
    val context: String,
    val difficulty: String,
    val estimated_duration_minutes: Int,

    val metrics_baseline: ScenarioData.MetricsBaseline,
    val nodes: List<ScenarioData.Node>
)

fun ScenarioData.toEntity(): ScenarioEntity {
    return ScenarioEntity(
        id = this.id ?: "",
        scenario_id = this.scenario_id ?: "",
        title = this.title ?: "",
        subtitle = this.subtitle ?: "",
        character = this.character ?: "Unknown",
        context = this.context ?: "",
        difficulty = this.difficulty ?: "",
        estimated_duration_minutes = this.estimated_duration_minutes ?: 0,
        metrics_baseline = this.metrics_baseline,

        nodes = this.nodes ?: emptyList()
    )
}