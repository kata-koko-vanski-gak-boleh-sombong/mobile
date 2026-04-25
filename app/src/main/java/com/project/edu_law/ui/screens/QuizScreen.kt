package com.project.edu_law.ui.screens

import android.util.Base64
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
import java.nio.charset.StandardCharsets

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
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
        return
    }

    val node = currentNode!!
    val maxStep = 4f
    val currentStepProgress = (node.sequence_order / maxStep).coerceIn(0f, 1f)
    val animatedMainProgress by animateFloatAsState(
        targetValue = currentStepProgress,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "mainProgressBar"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = !showEnding,
            exit = fadeOut(animationSpec = tween(300))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .statusBarsPadding()
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(mainScrollState)
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { isMetricsExpanded = !isMetricsExpanded }
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Tahap ${node.sequence_order} dari 4", fontSize = 12.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
                            Text("${(currentStepProgress * 100).toInt()}% Selesai", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (isMetricsExpanded) "Sembunyikan" else "Lihat Metrik",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                                modifier = Modifier.padding(end = 4.dp)
                            )
                            Icon(
                                imageVector = if (isMetricsExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    LinearProgressIndicator(
                        progress = { animatedMainProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .height(8.dp)
                            .clip(CircleShape),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                    )

                    AnimatedVisibility(
                        visible = isMetricsExpanded,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        currentMetrics?.let { metrics ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.cardElevation(2.dp),
                                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    LiveMetricItem(Icons.Default.Group, "Rights", metrics.fundamental_rights, Color(0xFF2E7D32))
                                    LiveMetricItem(Icons.Default.Gavel, "Criminal", metrics.criminal_justice, Color(0xFF1565C0))
                                    LiveMetricItem(Icons.Default.AccountBalance, "Civil", metrics.civil_justice, Color(0xFF6A1B9A))
                                    LiveMetricItem(Icons.Default.Security, "Corruption", metrics.corruption, Color(0xFFC62828))
                                }
                            }
                        }
                    }

                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(24.dp)) {
                            Text(node.content.title, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onSurface)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(node.content.body, fontSize = 15.sp, lineHeight = 24.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    val safeChoices = node.getSafeChoices()
                    if (!node.is_end_node && safeChoices != null) {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            safeChoices.forEach { choice ->
                                OptionItem(
                                    choice = choice,
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
                        color = MaterialTheme.colorScheme.surface,
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
                                        if (nextNode.is_end_node) {
                                            showEnding = true
                                            currentMetrics?.let { finalMetrics ->
                                                val safeNextEnding = nextNode.getSafeEndingData()
                                                val historyItem = com.project.edu_law.data.entity.HistoryEntity(
                                                    scenarioId = scenarioId,
                                                    scenarioTitle = scenario?.title ?: "Unknown",
                                                    fundamentalRights = finalMetrics.fundamental_rights,
                                                    criminalJustice = finalMetrics.criminal_justice,
                                                    civilJustice = finalMetrics.civil_justice,
                                                    corruption = finalMetrics.corruption,
                                                    endingType = safeNextEnding?.ending_type ?: "unknown"
                                                )
                                                viewModel.saveQuizHistory(historyItem)
                                            }
                                        }
                                    }
                                }
                            },
                            enabled = selectedChoice != null,
                            modifier = Modifier.fillMaxWidth().padding(24.dp).height(58.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Konfirmasi Keputusan", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                        }
                    }
                }
            }
        }

        val safeEndingData = node.getSafeEndingData()
        AnimatedVisibility(
            visible = showEnding && safeEndingData != null,
            enter = slideInVertically(initialOffsetY = { it / 2 }) + fadeIn(),
            exit = fadeOut()
        ) {
            if (safeEndingData != null) {
                Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).statusBarsPadding()) {
                    Column(
                        modifier = Modifier.fillMaxSize().verticalScroll(insightScrollState).padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("SIMULASI SELESAI", fontWeight = FontWeight.Black, letterSpacing = 2.sp, color = Color(0xFF2E7D32), fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            shape = RoundedCornerShape(24.dp),
                            elevation = CardDefaults.cardElevation(4.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(24.dp)) {
                                Text(node.content.title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(text = "${node.content.body}\n\nKesimpulan: ${safeEndingData.summary}", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f), lineHeight = 22.sp)
                                Spacer(modifier = Modifier.height(24.dp))
                                Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("Skor Rule of Law Akhir:", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                Spacer(modifier = Modifier.height(16.dp))
                                currentMetrics?.let { metrics ->
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                                        LiveMetricItem(Icons.Default.Group, "Rights", metrics.fundamental_rights, Color(0xFF2E7D32))
                                        LiveMetricItem(Icons.Default.Gavel, "Criminal", metrics.criminal_justice, Color(0xFF1565C0))
                                        LiveMetricItem(Icons.Default.AccountBalance, "Civil", metrics.civil_justice, Color(0xFF6A1B9A))
                                        LiveMetricItem(Icons.Default.Security, "Corruption", metrics.corruption, Color(0xFFC62828))
                                    }
                                }
                                Spacer(modifier = Modifier.height(24.dp))
                                Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("Real World Case", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(safeEndingData.real_world_case.detail, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                                Spacer(modifier = Modifier.height(16.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    InfoTag(Icons.Default.MenuBook, "Tahun", safeEndingData.real_world_case.year, Modifier.weight(1f))
                                    InfoTag(Icons.Default.Gavel, "Sumber", safeEndingData.real_world_case.source, Modifier.weight(1f))
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(150.dp))
                    }

                    Column(
                        modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().background(MaterialTheme.colorScheme.surface).padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                val bytes = safeEndingData.summary.toByteArray(StandardCharsets.UTF_8)
                                val safeContext = Base64.encodeToString(bytes, Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING)
                                navController.navigate("chat_ai/$safeContext")
                            },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Icon(Icons.Default.AutoAwesome, null, modifier = Modifier.size(18.dp), tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Tanya Pak Hukum (AI)", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        }

                        Button(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Simpan & Selesai", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LiveMetricItem(icon: ImageVector, label: String, value: Int, color: Color) {
    val animatedMetricProgress by animateFloatAsState(
        targetValue = (value / 100f).coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "miniProgressBar"
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(76.dp)) {
        Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(28.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, fontSize = 10.sp, color = color, textAlign = TextAlign.Center, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.Bottom) {
            Text(text = value.toString(), fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onSurface)
            Text(text = "/100", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), modifier = Modifier.padding(bottom = 3.dp))
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { animatedMetricProgress },
            modifier = Modifier.height(4.dp).fillMaxWidth(0.8f).clip(CircleShape),
            color = color,
            trackColor = color.copy(alpha = 0.2f)
        )
    }
}

@Composable
fun OptionItem(choice: ScenarioData.Node.Choice, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(width = if (isSelected) 2.dp else 1.dp, color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)),
        color = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(24.dp).clip(CircleShape).background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface).border(1.dp, if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) Box(Modifier.size(8.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surface))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = choice.text, fontSize = 14.sp, color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                val impact = choice.impact
                if (impact.fundamental_rights != 0 || impact.criminal_justice != 0 || impact.civil_justice != 0 || impact.corruption != 0) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        if (impact.fundamental_rights != 0) MiniImpactBadge(Icons.Default.Group, impact.fundamental_rights, Color(0xFF2E7D32))
                        if (impact.criminal_justice != 0) MiniImpactBadge(Icons.Default.Gavel, impact.criminal_justice, Color(0xFF1565C0))
                        if (impact.civil_justice != 0) MiniImpactBadge(Icons.Default.AccountBalance, impact.civil_justice, Color(0xFF6A1B9A))
                        if (impact.corruption != 0) MiniImpactBadge(Icons.Default.Security, impact.corruption, Color(0xFFC62828))
                    }
                }
            }
        }
    }
}

@Composable
fun MiniImpactBadge(icon: ImageVector, value: Int, color: Color) {
    val displayValue = if (value > 0) "+$value" else value.toString()
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clip(RoundedCornerShape(6.dp)).background(color.copy(alpha = 0.1f)).padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Icon(icon, null, tint = color, modifier = Modifier.size(10.dp))
        Spacer(modifier = Modifier.width(3.dp))
        Text(text = displayValue, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, color = color)
    }
}

@Composable
fun InfoTag(icon: ImageVector, label: String, value: String, modifier: Modifier) {
    Surface(modifier = modifier, color = MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(12.dp), border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                Spacer(Modifier.width(4.dp))
                Text(label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(value, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}