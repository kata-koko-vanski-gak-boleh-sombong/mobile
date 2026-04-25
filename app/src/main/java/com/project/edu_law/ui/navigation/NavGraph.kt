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
import com.project.edu_law.ui.screens.GenerateScreen
import com.project.edu_law.ui.screens.HomeScreen
import com.project.edu_law.ui.screens.LegalScenarioScreen
import com.project.edu_law.ui.screens.QuizScreen
import com.project.edu_law.ui.screens.ScenarioOverviewScreen
import com.project.edu_law.ui.screens.viewmodel.ScenarioViewModel
import android.util.Base64
import com.project.edu_law.ui.screens.ChatScreen

@Composable
fun SetupNavGraph(navController: NavHostController, paddingValues: PaddingValues) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val scope = rememberCoroutineScope()

    val database = remember { AppDatabase.getDatabase(context, scope) }
    val scenarioDao = database.scenarioDao()
    val historyDao = database.historyDao()

    val repository = remember { ScenarioRepository(scenarioDao, historyDao) }

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = Modifier.padding(paddingValues)
    ) {
        composable(Screen.Home.route) { HomeScreen() }

        composable(Screen.Scenario.route) {
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

            val viewModel: ScenarioViewModel = viewModel(
                factory = ScenarioViewModel.provideFactory(repository)
            )

            ScenarioOverviewScreen(
                scenarioId = scenarioId,
                viewModel = viewModel,
                onStartSimulation = { id ->
                    navController.navigate("${Screen.Simulation.route}/$id")
                }
            )
        }

        composable(
            route = Screen.Simulation.route + "/{scenarioId}"
        ) { backStackEntry ->
            val scenarioId = backStackEntry.arguments?.getString("scenarioId") ?: return@composable

            val viewModel: ScenarioViewModel = viewModel(
                factory = ScenarioViewModel.provideFactory(repository)
            )

            QuizScreen(
                scenarioId = scenarioId,
                viewModel = viewModel,
                navController = navController
            )
        }

        composable(Screen.Generate.route) {
            val viewModel: ScenarioViewModel = viewModel(
                factory = ScenarioViewModel.provideFactory(repository)
            )
            GenerateScreen(viewModel = viewModel, navController = navController)
        }

        // ROUTE UNTUK CHAT AI
        composable(
            route = "chat_ai/{encodedContext}"
        ) { backStackEntry ->
            val encodedContext = backStackEntry.arguments?.getString("encodedContext") ?: ""

            // Decode dari Base64 kembali ke teks normal
            val decodedContext = try {
                String(Base64.decode(encodedContext, Base64.URL_SAFE or Base64.NO_WRAP), Charsets.UTF_8)
            } catch (e: Exception) {
                "Konteks tidak ditemukan."
            }

            val viewModel: ScenarioViewModel = viewModel(
                factory = ScenarioViewModel.provideFactory(repository)
            )

            ChatScreen(
                endingContext = decodedContext,
                viewModel = viewModel,
                navController = navController
            )
        }
    }
}