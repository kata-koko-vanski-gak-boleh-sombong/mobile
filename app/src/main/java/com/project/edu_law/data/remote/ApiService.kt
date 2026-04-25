package com.project.edu_law.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @Headers("Content-Type: application/json")
    @GET("simulation/get")
    suspend fun getAllScenarios(): Response<ScenarioResponseWrapper>

    @Headers("Content-Type: application/json")
    @POST("simulation/generate")
    suspend fun generateSimulation(
        @Body request: GenerateRequest
    ): Response<SingleScenarioResponseWrapper>

    @Headers("Content-Type: application/json")
    @POST("simulation/ask")
    suspend fun askAi(
        @Body request: AskRequest
    ): retrofit2.Response<AskResponseWrapper>
}