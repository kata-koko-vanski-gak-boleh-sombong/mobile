package com.project.edu_law.data.remote

data class AskRequest(
    val id_user: String,
    val prompt: String
)

data class AskResponseWrapper(
    val status: String,
    val data: AskResponseData
)

data class AskResponseData(
    val id: String,
    val id_user: String,
    val prompt: String,
    val response: String,
    val created_at: String,
    val updated_at: String
)