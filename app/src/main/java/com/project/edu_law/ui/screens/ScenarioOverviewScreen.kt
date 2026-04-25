package com.project.edu_law.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.edu_law.ui.screens.viewmodel.ScenarioViewModel

@Composable
fun ScenarioOverviewScreen(
    scenarioId: String?,
    viewModel: ScenarioViewModel = viewModel(),
    onStartSimulation: (String) -> Unit
) {
    LaunchedEffect(scenarioId) {
        scenarioId?.let { viewModel.getScenarioById(it) }
    }

    val scenario by viewModel.selectedScenario.collectAsState()

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            if (scenario != null) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shadowElevation = 16.dp,
                    color = Color.White
                ) {
                    Button(
                        onClick = { onStartSimulation(scenario!!.id) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF004080)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "Masuk ke Skenario",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    ) { padding ->
        if (scenario == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF004080))
            }
        } else {
            val data = scenario!!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = data.title,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF111827),
                        lineHeight = 34.sp
                    )
                )

                Text(
                    text = "Briefing Simulasi",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    InfoBadge(
                        label = "Kompleksitas",
                        value = data.difficulty,
                        icon = Icons.Default.BarChart,
                        modifier = Modifier.weight(1f)
                    )
                    InfoBadge(
                        label = "Estimasi",
                        value = "${data.estimated_duration_minutes} Menit",
                        icon = Icons.Default.AccessTime,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEEF2FF)),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(Color.White, RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Person, null, tint = Color(0xFF4338CA))
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                "Perspektif Peran",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color(0xFF4338CA),
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                data.character,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF1E1B4B)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    "Konteks Kasus",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    color = Color(0xFF111827)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = data.context,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        lineHeight = 28.sp,
                        color = Color(0xFF4B5563)
                    )
                )
            }
        }
    }
}

@Composable
fun InfoBadge(
    label: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(20.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF3F4F6))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Icon(icon, contentDescription = null, tint = Color(0xFF004080), modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.height(12.dp))
            Text(label, fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
            Text(value, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
        }
    }
}