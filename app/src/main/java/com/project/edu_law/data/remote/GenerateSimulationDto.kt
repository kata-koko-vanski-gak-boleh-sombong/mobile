package com.project.edu_law.data.remote

import com.project.edu_law.data.ScenarioData

data class GenerateRequest(
    val id_user: String,
    val difficulty: String,
    val prompt: String
)

data class SingleScenarioResponseWrapper(
    val status: String,
    val data: ScenarioData
)