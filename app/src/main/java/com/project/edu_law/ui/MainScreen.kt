package com.project.edu_law.ui

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.project.edu_law.ui.components.BottomNavigationBar
import com.project.edu_law.ui.navigation.Screen
import com.project.edu_law.ui.navigation.SetupNavGraph

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val routesWithBottomBar = listOf(
        Screen.Home.route,
        Screen.Scenario.route,
        Screen.Simulation.route
    )

    Scaffold(
        bottomBar = {
            if (currentRoute in routesWithBottomBar) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { innerPadding ->
        SetupNavGraph(navController = navController, paddingValues = innerPadding)
    }
}