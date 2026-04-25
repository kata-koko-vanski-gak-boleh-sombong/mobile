package com.project.edu_law.data

import com.google.gson.annotations.SerializedName

data class ScenarioData(
    val id: String,
    val title: String,
    val subtitle: String,
    @SerializedName("character_role")
    val character: String,
    val context: String,
    val difficulty: String,
    val estimated_duration_minutes: Int,
    val metrics_baseline: MetricsBaseline,
    @SerializedName("nodes_data")
    val nodes: List<Node>,
    val scenario_id: String,

    val id_user: String? = null,
    val is_active: Boolean? = null,
    val created_at: String? = null,
    val updated_at: String? = null
) {
    data class MetricsBaseline(
        val civil_justice: Int,
        val corruption: Int,
        val criminal_justice: Int,
        val fundamental_rights: Int
    )

    data class Node(
        val choices: List<Choice>,
        val content: Content,
        val ending_data: EndingData,
        val id: String,
        val is_end_node: Boolean,
        val is_start_node: Boolean,
        val sequence_order: Int,
        val type: String
    ) {
        data class Choice(
            val choice_type: String,
            val id: String,
            val impact: Impact,
            val insight: Insight,
            val next_node_id: String,
            val text: String
        ) {
            data class Impact(
                val civil_justice: Int,
                val corruption: Int,
                val criminal_justice: Int,
                val fundamental_rights: Int
            )

            data class Insight(
                val body: String,
                val source: String,
                val title: String
            )
        }

        data class Content(
            val body: String,
            val title: String
        )

        data class EndingData(
            val ending_code: String,
            val ending_type: String,
            val final_metrics: FinalMetrics,
            val real_world_case: RealWorldCase,
            val summary: String
        ) {
            data class FinalMetrics(
                val civil_justice: Int,
                val corruption: Int,
                val criminal_justice: Int,
                val fundamental_rights: Int
            )

            data class RealWorldCase(
                val detail: String,
                val source: String,
                val title: String,
                val url: String,
                val year: String
            )
        }
    }
}