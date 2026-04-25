package com.project.edu_law.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.project.edu_law.ui.navigation.Screen
import com.project.edu_law.ui.screens.viewmodel.ScenarioViewModel
import com.project.edu_law.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenerateScreen(
    viewModel: ScenarioViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    var selectedCharacter by remember { mutableStateOf("Orang Tua") }
    var selectedDifficulty by remember { mutableStateOf("Menengah") }

    val isGenerating by viewModel.isGenerating.collectAsState()
    val errorMessage by viewModel.generateError.collectAsState()

    val characters = listOf("Orang Tua", "Kepala Sekolah", "Pejabat Daerah")
    val difficulties = listOf("Mudah", "Menengah", "Sulit")

    Scaffold(
        containerColor = White,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Rancang Simulasi",
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.5).sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = White,
                    titleContentColor = DarkText
                )
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = White,
                shadowElevation = 20.dp
            ) {
                Button(
                    onClick = {
                        Toast.makeText(context, "AI sedang merancang skenario hukum untukmu...", Toast.LENGTH_LONG).show()
                        viewModel.generateScenario(
                            difficulty = selectedDifficulty,
                            character = selectedCharacter,
                            onSuccess = { newScenarioId ->
                                navController.navigate("${Screen.ScenarioOverview.route}/$newScenarioId") {
                                    popUpTo(Screen.Generate.route) { inclusive = true }
                                }
                            }
                        )
                    },
                    enabled = !isGenerating,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BluePrimary,
                        disabledContainerColor = GrayBorder
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    if (isGenerating) {
                        CircularProgressIndicator(color = White, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Membuat Cerita...", fontWeight = FontWeight.Bold, color = White)
                    } else {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(20.dp), tint = White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Generate Simulasi", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = White)
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(White)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Rancang Dilema Hukum",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = DarkText
            )
            Text(
                "AI akan menciptakan simulasi unik berdasarkan parameter yang kamu tentukan.",
                fontSize = 14.sp,
                color = GrayText,
                lineHeight = 20.sp,
                modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
            )

            SectionHeader(title = "Pilih Sudut Pandang")
            Spacer(modifier = Modifier.height(12.dp))
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                characters.forEach { char ->
                    CharacterSelectionCard(
                        text = char,
                        isSelected = selectedCharacter == char,
                        onClick = { selectedCharacter = char }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            SectionHeader(title = "Tingkat Kompleksitas")
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                difficulties.forEach { diff ->
                    DifficultyChip(
                        text = diff,
                        isSelected = selectedDifficulty == diff,
                        onClick = { selectedDifficulty = diff },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(24.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
                    border = BorderStroke(1.dp, Color(0xFFFCA5A5)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = errorMessage!!,
                        color = Color(0xFF991B1B),
                        fontSize = 13.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        color = DarkText
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterSelectionCard(text: String, isSelected: Boolean, onClick: () -> Unit) {
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
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = text,
                color = if (isSelected) BluePrimary else GrayText,
                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                fontSize = 15.sp
            )
            if (isSelected) {
                Icon(Icons.Default.ChevronRight, null, tint = BluePrimary)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DifficultyChip(text: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (isSelected) BluePrimary else GrayBorder
        ),
        color = if (isSelected) BluePrimary else White,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = if (isSelected) White else GrayText,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 14.sp
            )
        }
    }
}