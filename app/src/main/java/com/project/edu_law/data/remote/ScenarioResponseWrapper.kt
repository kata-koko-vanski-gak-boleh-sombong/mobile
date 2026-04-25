package com.project.edu_law.data.remote

import com.project.edu_law.data.ScenarioData

data class ScenarioResponseWrapper(
    val status: String,
    val data: List<ScenarioData>
)