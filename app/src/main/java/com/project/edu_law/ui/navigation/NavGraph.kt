package com.project.edu_law.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.project.edu_law.data.local.AppDatabase
import com.project.edu_law.data.repository.ScenarioRepository
import com.project.edu_law.ui.screens.HomeScreen
import com.project.edu_law.ui.screens.LegalScenarioScreen
import com.project.edu_law.ui.screens.QuizScreen
import com.project.edu_law.ui.screens.ScenarioOverviewScreen
import com.project.edu_law.ui.screens.viewmodel.ScenarioViewModel

// com/project/edu_law/ui/navigation/SetupNavGraph.kt

@Composable
fun SetupNavGraph(navController: NavHostController, paddingValues: PaddingValues) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val scope = rememberCoroutineScope()

    // 1. Ambil instance database
    val database = remember { AppDatabase.getDatabase(context, scope) }
    val scenarioDao = database.scenarioDao()

    // 2. Inisialisasi Repository (Inilah yang tadi kurang)
    val repository = remember { ScenarioRepository(scenarioDao) }

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = Modifier.padding(paddingValues)
    ) {
        composable(Screen.Home.route) { HomeScreen() }

        composable(Screen.Scenario.route) {
            // 3. Sekarang 'repository' sudah tersedia di sini
            val viewModel: ScenarioViewModel = viewModel(
                factory = ScenarioViewModel.provideFactory(repository)
            )

            LegalScenarioScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable(
            route = Screen.ScenarioOverview.route + "/{scenarioId}"
        ) { backStackEntry ->
            val scenarioId = backStackEntry.arguments?.getString("scenarioId")

            // Pakai repository yang sama
            val viewModel: ScenarioViewModel = viewModel(
                factory = ScenarioViewModel.provideFactory(repository)
            )

            ScenarioOverviewScreen(
                scenarioId = scenarioId,
                viewModel = viewModel,
                onStartSimulation = {
                    navController.navigate(Screen.Simulation.route)
                }
            )
        }

        composable(Screen.Simulation.route) { QuizScreen() }
    }
}