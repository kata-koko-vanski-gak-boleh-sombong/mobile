package com.project.edu_law.data

data class Question(
    val id: Int,
    val category: String,
    val title: String,
    val description: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val insight: String
)
