package com.project.edu_law.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface ApiService {
    @Headers("Content-Type: application/json")
    @GET("simulation/get")
    suspend fun getAllScenarios(): Response<ScenarioResponseWrapper>
}