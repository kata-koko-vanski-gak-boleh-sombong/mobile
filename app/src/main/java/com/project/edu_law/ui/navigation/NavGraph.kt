package com.project.edu_law.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.project.edu_law.ui.screens.HomeScreen
import com.project.edu_law.ui.screens.LegalScenarioScreen
import com.project.edu_law.ui.screens.QuizScreen

@Composable
fun SetupNavGraph(navController: NavHostController, paddingValues: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = Modifier.padding(paddingValues)
    ) {
        composable(Screen.Home.route) { HomeScreen() }
        composable(Screen.Scenario.route) {
            LegalScenarioScreen(navController = navController)
        }
        composable(Screen.Simulation.route) { QuizScreen() }
    }
}