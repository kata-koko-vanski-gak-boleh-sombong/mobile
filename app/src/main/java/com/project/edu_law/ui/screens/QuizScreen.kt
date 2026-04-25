package com.project.edu_law.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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

    // State untuk expand/collapse metrik
    var isMetricsExpanded by remember { mutableStateOf(true) }

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

    val maxStep = 4f
    val currentStepProgress = (node.sequence_order / maxStep).coerceIn(0f, 1f)

    // Animasi mulus untuk Progress Bar Utama
    val animatedMainProgress by animateFloatAsState(
        targetValue = currentStepProgress,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "mainProgressBar"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FA))
                .statusBarsPadding()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(mainScrollState)
                    .padding(20.dp)
            ) {
                // Header dengan tombol Expand/Collapse
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { isMetricsExpanded = !isMetricsExpanded } // Action hide/show
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Tahap ${node.sequence_order} dari 4", fontSize = 12.sp, color = GrayText)
                        Text("${(currentStepProgress * 100).toInt()}% Selesai", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BluePrimary)
                    }

                    // Icon indikator atas/bawah
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (isMetricsExpanded) "Sembunyikan" else "Lihat Metrik",
                            fontSize = 10.sp,
                            color = GrayText,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        Icon(
                            imageVector = if (isMetricsExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = "Toggle Metrics",
                            tint = GrayText,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Progress Bar dengan State Animasi
                LinearProgressIndicator(
                    progress = { animatedMainProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .height(8.dp)
                        .clip(CircleShape),
                    color = BluePrimary,
                    trackColor = GrayBorder
                )

                Spacer(modifier = Modifier.height(4.dp))

                // --- LIVE METRICS ROW (DENGAN ANIMASI HIDE/SHOW) ---
                AnimatedVisibility(
                    visible = isMetricsExpanded,
                    enter = expandVertically(animationSpec = tween(500)) + fadeIn(animationSpec = tween(500)),
                    exit = shrinkVertically(animationSpec = tween(500)) + fadeOut(animationSpec = tween(500))
                ) {
                    currentMetrics?.let { metrics ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(2.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp) // Padding bawah agar rapi saat expand
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp, horizontal = 8.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                LiveMetricItem(Icons.Default.Group, "Fundamental\nRights", metrics.fundamental_rights, Color(0xFF2E7D32))
                                Divider(modifier = Modifier.height(40.dp).width(1.dp), color = GrayBorder)
                                LiveMetricItem(Icons.Default.Gavel, "Criminal\nJustice", metrics.criminal_justice, Color(0xFF1565C0))
                                Divider(modifier = Modifier.height(40.dp).width(1.dp), color = GrayBorder)
                                LiveMetricItem(Icons.Default.AccountBalance, "Civil\nJustice", metrics.civil_justice, Color(0xFF6A1B9A))
                                Divider(modifier = Modifier.height(40.dp).width(1.dp), color = GrayBorder)
                                LiveMetricItem(Icons.Default.Security, "Corruption\nLevel", metrics.corruption, Color(0xFFC62828))
                            }
                        }
                    }
                }

                // Card Skenario/Pertanyaan
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(node.content.title, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color.Black)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(node.content.body, fontSize = 15.sp, lineHeight = 24.sp, color = Color.DarkGray)
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
                    color = Color.White,
                    shadowElevation = 8.dp
                ) {
                    Button(
                        onClick = {
                            selectedChoice?.let { choice ->
                                currentMetrics = currentMetrics?.let { prev ->
                                    ScenarioData.MetricsBaseline(
                                        fundamental_rights = (prev.fundamental_rights + choice.impact.fundamental_rights).coerceIn(0, 100),
                                        criminal_justice = (prev.criminal_justice + choice.impact.criminal_justice).coerceIn(0, 100),
                                        civil_justice = (prev.civil_justice + choice.impact.civil_justice).coerceIn(0, 100),
                                        corruption = (prev.corruption + choice.impact.corruption).coerceIn(0, 100)
                                    )
                                }

                                val nextNode = scenario?.nodes?.find { it.id == choice.next_node_id }
                                if (nextNode != null) {
                                    currentNode = nextNode
                                    selectedChoice = null
                                    isMetricsExpanded = false

                                    // JIKA INI ADALAH NODE TERAKHIR (ENDING)
                                    if (nextNode.is_end_node) {
                                        showEnding = true

                                        // --- TAMBAHKAN KODE INI UNTUK MENYIMPAN HISTORY ---
                                        currentMetrics?.let { finalMetrics ->
                                            val historyItem = com.project.edu_law.data.entity.HistoryEntity(
                                                scenarioId = scenarioId,
                                                scenarioTitle = scenario?.title ?: "Unknown",
                                                fundamentalRights = finalMetrics.fundamental_rights,
                                                criminalJustice = finalMetrics.criminal_justice,
                                                civilJustice = finalMetrics.civil_justice,
                                                corruption = finalMetrics.corruption,
                                                endingType = nextNode.ending_data?.ending_type ?: "unknown"
                                            )
                                            viewModel.saveQuizHistory(historyItem)
                                        }
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
                        Text("Konfirmasi Keputusan", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
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
                    .background(Color(0xFFF0FDF4))
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
                        color = Color(0xFF2E7D32),
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(24.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(24.dp)) {

                            Text(node.content.title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "${node.content.body}\n\nKesimpulan: ${endingData.summary}",
                                fontSize = 14.sp,
                                color = Color.DarkGray,
                                lineHeight = 22.sp
                            )

                            Spacer(modifier = Modifier.height(24.dp))
                            Divider(color = GrayBorder)
                            Spacer(modifier = Modifier.height(16.dp))

                            Text("Skor Rule of Law Akhir:", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            Spacer(modifier = Modifier.height(16.dp))

                            currentMetrics?.let { metrics ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    LiveMetricItem(Icons.Default.Group, "Fundamental\nRights", metrics.fundamental_rights, Color(0xFF2E7D32))
                                    LiveMetricItem(Icons.Default.Gavel, "Criminal\nJustice", metrics.criminal_justice, Color(0xFF1565C0))
                                    LiveMetricItem(Icons.Default.AccountBalance, "Civil\nJustice", metrics.civil_justice, Color(0xFF6A1B9A))
                                    LiveMetricItem(Icons.Default.Security, "Corruption\nLevel", metrics.corruption, Color(0xFFC62828))
                                }
                            }

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

                    Spacer(modifier = Modifier.height(150.dp))
                }

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(Color.Transparent)
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            // TODO: Navigasi ke rute Chat AI Anda
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White,
                            contentColor = BluePrimary
                        ),
                        border = BorderStroke(2.dp, BluePrimary),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Text("Tanya AI Alasan Keputusan Ini", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }

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
                        Text("Selesai & Kembali", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                    }
                }
            }
        }
    }
}

// --- KOMPONEN METRIK DENGAN ANIMASI ---
@Composable
fun LiveMetricItem(icon: ImageVector, label: String, value: Int, color: Color) {
    // Animasi mulus untuk mini progress bar di dalam metrik
    val animatedMetricProgress by animateFloatAsState(
        targetValue = (value / 100f).coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "miniProgressBar"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(76.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(28.dp))
        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            fontSize = 10.sp,
            color = color,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 12.sp,
            modifier = Modifier.height(26.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(verticalAlignment = Alignment.Bottom) {
            Text(text = value.toString(), fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1A1A1A))
            Text(text = "/100", fontSize = 10.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 3.dp))
        }

        Spacer(modifier = Modifier.height(4.dp))

        LinearProgressIndicator(
            progress = { animatedMetricProgress }, // Menggunakan state animasi
            modifier = Modifier
                .height(4.dp)
                .fillMaxWidth(0.8f)
                .clip(RoundedCornerShape(2.dp)),
            color = color,
            trackColor = color.copy(alpha = 0.2f)
        )
    }
}

// --- OptionItem & InfoTag ---
@Composable
fun OptionItem(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) BluePrimary else GrayBorder
        ),
        color = if (isSelected) BlueSecondary else Color.White,
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
                    .background(if (isSelected) BluePrimary else Color.White)
                    .border(1.dp, if (isSelected) BluePrimary else GrayBorder, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Box(Modifier.size(8.dp).clip(CircleShape).background(Color.White))
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
fun InfoTag(icon: ImageVector, label: String, value: String, modifier: Modifier) {
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