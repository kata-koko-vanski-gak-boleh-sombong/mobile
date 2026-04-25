package com.project.edu_law.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.project.edu_law.ui.navigation.Screen
import com.project.edu_law.data.entity.ScenarioEntity
import com.project.edu_law.ui.screens.viewmodel.ScenarioViewModel
import kotlin.math.roundToInt

@Composable
fun LegalScenarioScreen(
    navController: NavHostController,
    viewModel: ScenarioViewModel
) {
    val scenarioList by viewModel.allScenarios.collectAsState()
    var parentSize by remember { mutableStateOf(IntSize.Zero) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .onSizeChanged { parentSize = it }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
                    .padding(top = 16.dp)
            ) {
                Text(
                    text = "Ruang Simulasi",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.5).sp
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Uji integritasmu dalam menghadapi dilema hukum dan pilar keadilan.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                if (scenarioList.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Belum ada skenario tersedia", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(bottom = 80.dp)
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

            var offsetX by remember { mutableStateOf(0f) }
            var offsetY by remember { mutableStateOf(0f) }
            var fabSize by remember { mutableStateOf(IntSize.Zero) }

            ExtendedFloatingActionButton(
                onClick = { navController.navigate(Screen.Generate.route) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .onSizeChanged { fabSize = it }
                    .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                    .pointerInput(parentSize, fabSize) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            if (parentSize != IntSize.Zero && fabSize != IntSize.Zero) {
                                val paddingPx = 32.dp.toPx()
                                val minX = -(parentSize.width - fabSize.width - paddingPx)
                                val minY = -(parentSize.height - fabSize.height - paddingPx)
                                offsetX = (offsetX + dragAmount.x).coerceIn(minX, 0f)
                                offsetY = (offsetY + dragAmount.y).coerceIn(minY, 0f)
                            }
                        }
                    }
            ) {
                Text("Buat Skenario", fontWeight = FontWeight.Bold)
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
        scenario.character.contains("Pemerintah", ignoreCase = true) -> MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) to MaterialTheme.colorScheme.primary
        scenario.character.contains("Masyarakat", ignoreCase = true) -> MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f) to MaterialTheme.colorScheme.onSurface
        else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f) to MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
    }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)),
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
                    shape = RoundedCornerShape(100.dp)
                ) {
                    Text(
                        text = scenario.character,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = roleTextColor
                        )
                    )
                }
                Icon(
                    imageVector = Icons.Default.Gavel,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = scenario.title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    lineHeight = 28.sp
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = scenario.subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.padding(vertical = 12.dp),
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onStartClick,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Mulai Simulasi",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}