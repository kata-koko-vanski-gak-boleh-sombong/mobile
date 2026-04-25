package com.project.edu_law.ui

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.project.edu_law.ui.components.BottomNavigationBar
import com.project.edu_law.ui.navigation.SetupNavGraph

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold (
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        SetupNavGraph(navController = navController, paddingValues = innerPadding)
    }
}