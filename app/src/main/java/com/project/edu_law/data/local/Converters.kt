package com.project.edu_law.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.project.edu_law.data.ScenarioData

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromMetrics(value: ScenarioData.MetricsBaseline): String = gson.toJson(value)

    @TypeConverter
    fun toMetrics(value: String): ScenarioData.MetricsBaseline =
        gson.fromJson(value, ScenarioData.MetricsBaseline::class.java)

    @TypeConverter
    fun fromNodeList(value: List<ScenarioData.Node>): String = gson.toJson(value)

    @TypeConverter
    fun toNodeList(value: String): List<ScenarioData.Node> {
        val listType = object : TypeToken<List<ScenarioData.Node>>() {}.type
        return gson.fromJson(value, listType)
    }
}