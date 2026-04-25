package com.project.edu_law.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.project.edu_law.data.ScenarioData
import com.project.edu_law.ui.screens.viewmodel.ScenarioViewModel
import com.project.edu_law.ui.theme.*

@Composable
fun QuizScreen(
    scenarioId: String,
    viewModel: ScenarioViewModel,
    navController: NavController
) {
    LaunchedEffect(scenarioId) {
        viewModel.getScenarioById(scenarioId)
    }

    val scenario by viewModel.selectedScenario.collectAsState()

    var currentNode by remember { mutableStateOf<ScenarioData.Node?>(null) }
    var currentMetrics by remember { mutableStateOf<ScenarioData.MetricsBaseline?>(null) }
    var selectedChoice by remember { mutableStateOf<ScenarioData.Node.Choice?>(null) }
    var showEnding by remember { mutableStateOf(false) }

    val mainScrollState = rememberScrollState()
    val insightScrollState = rememberScrollState()

    LaunchedEffect(scenario) {
        scenario?.let { s ->
            currentNode = s.nodes.find { it.is_start_node }
            currentMetrics = s.metrics_baseline
        }
    }

    if (scenario == null || currentNode == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = BluePrimary)
        }
        return
    }

    val node = currentNode!!

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(White)
                .statusBarsPadding()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(mainScrollState)
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Tahap Keputusan", fontSize = 12.sp, color = GrayText)
                    Text("Role: ${scenario?.character}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BluePrimary)
                }
                LinearProgressIndicator(
                    progress = { 0.5f }, // Opsional: Buat dinamis berdasarkan jumlah step jika diperlukan
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .height(6.dp)
                        .clip(CircleShape),
                    color = BluePrimary,
                    trackColor = GrayBorder
                )

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    colors = CardDefaults.cardColors(containerColor = White),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, GrayBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(node.content.title, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Color.Black)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(node.content.body, fontSize = 15.sp, lineHeight = 24.sp, color = GrayText)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                if (!node.is_end_node && node.choices != null) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        node.choices.forEach { choice ->
                            OptionItem(
                                text = choice.text,
                                isSelected = selectedChoice == choice,
                                onClick = { selectedChoice = choice }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            if (!node.is_end_node) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = White,
                    shadowElevation = 8.dp
                ) {
                    Button(
                        onClick = {
                            selectedChoice?.let { choice ->
                                currentMetrics = currentMetrics?.let { prev ->
                                    ScenarioData.MetricsBaseline(
                                        fundamental_rights = prev.fundamental_rights + choice.impact.fundamental_rights,
                                        criminal_justice = prev.criminal_justice + choice.impact.criminal_justice,
                                        civil_justice = prev.civil_justice + choice.impact.civil_justice,
                                        corruption = prev.corruption + choice.impact.corruption
                                    )
                                }

                                val nextNode = scenario?.nodes?.find { it.id == choice.next_node_id }
                                if (nextNode != null) {
                                    currentNode = nextNode
                                    selectedChoice = null // Reset pilihan
                                    if (nextNode.is_end_node) {
                                        showEnding = true // Tampilkan insight jika node terakhir
                                    }
                                }
                            }
                        },
                        enabled = selectedChoice != null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BluePrimary,
                            disabledContainerColor = GrayBorder
                        ),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Text("Konfirmasi Keputusan", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = White)
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = showEnding && node.ending_data != null,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
        ) {
            val endingData = node.ending_data!!

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MintBackground)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(insightScrollState)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(40.dp))

                    Text(
                        "SIMULASI SELESAI",
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp,
                        color = White,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        colors = CardDefaults.cardColors(containerColor = White),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(24.dp)) {

                            Text(node.content.title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "${node.content.body}\n\nKesimpulan: ${endingData.summary}",
                                fontSize = 14.sp,
                                color = GrayText,
                                lineHeight = 22.sp
                            )

                            Spacer(modifier = Modifier.height(24.dp))
                            Divider(color = GrayBorder)
                            Spacer(modifier = Modifier.height(16.dp))

                            Text("Real World Case", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(endingData.real_world_case.detail, fontSize = 13.sp, color = GrayText)

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                InfoTag(Icons.Default.MenuBook, "Tahun", endingData.real_world_case.year, Modifier.weight(1f))
                                InfoTag(Icons.Default.Gavel, "Sumber", endingData.real_world_case.source, Modifier.weight(1f))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(100.dp))
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(Color.Transparent)
                        .padding(24.dp)
                ) {
                    Button(
                        onClick = {
                            navController.popBackStack()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Text("Selesai & Kembali", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = White)
                    }
                }
            }
        }
    }
}

@Composable
fun OptionItem(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) BluePrimary else GrayBorder
        ),
        color = if (isSelected) BlueSecondary else White,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) BluePrimary else White)
                    .border(1.dp, if (isSelected) BluePrimary else GrayBorder, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Box(
                        Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(White)
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = text,
                fontSize = 14.sp,
                color = if (isSelected) BluePrimary else Color.Black,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun InfoTag(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String, modifier: Modifier) {
    Surface(
        modifier = modifier,
        color = LightBlueBg,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, GrayBorder)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, null, modifier = Modifier.size(14.dp), tint = GrayText)
                Spacer(Modifier.width(4.dp))
                Text(label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GrayText)
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(value, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        }
    }
}