package com.project.edu_law.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.project.edu_law.ui.navigation.Screen
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.edu_law.data.entity.ScenarioEntity
import com.project.edu_law.ui.screens.viewmodel.ScenarioViewModel

@Composable
fun LegalScenarioScreen(
    navController: NavHostController,
    viewModel: ScenarioViewModel = viewModel()
) {
    val scenarioList by viewModel.allScenarios.collectAsState()

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FA))
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "Legal Scenarios",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF004080)
            )
            Text(
                text = "Uji pemahaman hukum Anda melalui simulasi kasus nyata.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (scenarioList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Belum ada skenario tersedia", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp, horizontal = 4.dp)
                ) {
                    items(
                        items = scenarioList,
                        key = { it.id }
                    ) { scenario ->
                        ScenarioCard(
                            scenario = scenario,
                            onStartClick = {
                                navController.navigate("${Screen.ScenarioOverview.route}/${scenario.id}")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ScenarioCard(
    scenario: ScenarioEntity,
    onStartClick: () -> Unit
) {
    val (roleBgColor, roleTextColor) = when {
        scenario.character.contains("Pemerintah", ignoreCase = true) -> Color(0xFF002366) to Color.White
        scenario.character.contains("Masyarakat", ignoreCase = true) -> Color(0xFFE3F2FD) to Color(0xFF1976D2)
        else -> Color(0xFFF5F5F5) to Color(0xFF424242)
    }

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = roleBgColor,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Peran: ${scenario.character}",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = roleTextColor
                        )
                    )
                }
                Icon(Icons.Default.Gavel, contentDescription = null, tint = Color.LightGray)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = scenario.title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )

            Text(
                text = scenario.subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onStartClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF004080)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Lihat simulasi", color = Color.White)
            }
        }
    }
}