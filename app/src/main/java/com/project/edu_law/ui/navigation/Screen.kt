package com.project.edu_law.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Balance
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Beranda", Icons.Default.Home)
    object Scenario : Screen("scenario", "Scenario", Icons.Default.Balance)
    object ScenarioOverview : Screen("scenario-overview", "Scenario Overview", Icons.Default.Balance)
    object Simulation : Screen("simulation", "Simulation", Icons.Default.Balance)
    object Generate : Screen("generate", "Generate", Icons.Default.Add)
}