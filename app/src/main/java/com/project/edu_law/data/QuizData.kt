package com.project.edu_law.data

data class QuizOption(
    val id: Int,
    val text: String
)

data class QuizState(
    val questionNumber: Int,
    val totalQuestions: Int,
    val progress: Float,
    val timeLeft: String,
    val title: String,
    val description: String,
    val options: List<QuizOption>,
    val selectedOptionId: Int? = null
)