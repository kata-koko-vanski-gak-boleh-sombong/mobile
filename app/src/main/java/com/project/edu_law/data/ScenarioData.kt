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
        val id: String,
        val type: String,
        val is_end_node: Boolean,
        val is_start_node: Boolean,
        val sequence_order: Int,
        val content: Content,

        val choices: List<Choice>? = null,
        val ending_data: EndingData? = null
    ) {
        fun getSafeChoices(): List<Choice>? {
            return this.content.choices ?: this.choices
        }

        fun getSafeEndingData(): EndingData? {
            return this.content.ending_data ?: this.ending_data
        }

        data class Content(
            val title: String,
            val body: String,
            val choices: List<Choice>? = null,
            val ending_data: EndingData? = null
        )

        data class Choice(
            val id: String,
            val text: String,
            val choice_type: String,
            val next_node_id: String,
            val impact: Impact,
            val insight: Insight
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

        data class EndingData(
            val ending_code: String,
            val ending_type: String,
            val summary: String,
            val final_metrics: FinalMetrics,
            val real_world_case: RealWorldCase
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