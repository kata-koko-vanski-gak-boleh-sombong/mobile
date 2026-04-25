package com.project.edu_law.data

data class Question(
    val id: Int,
    val title: String,
    val description: String,
    val options: List<String>,
    val insightTitle: String,
    val insightDescription: String
)
