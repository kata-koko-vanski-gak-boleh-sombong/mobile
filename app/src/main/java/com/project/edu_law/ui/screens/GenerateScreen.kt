package com.project.edu_law.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
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
import com.project.edu_law.ui.theme.BluePrimary
import com.project.edu_law.ui.theme.BlueSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenerateScreen(
    viewModel: ScenarioViewModel,
    navController: NavController
) {
    val context = LocalContext.current

    var selectedCharacter by remember { mutableStateOf("Orang Tua") }
    var selectedDifficulty by remember { mutableStateOf("Medium") }

    val isGenerating by viewModel.isGenerating.collectAsState()
    val errorMessage by viewModel.generateError.collectAsState()

    val characters = listOf("Orang Tua", "Kepala Sekolah", "Pejabat Daerah")
    val difficulties = listOf("Easy", "Medium", "Hard")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Generate Kasus Baru", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = BluePrimary
                )
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 16.dp
            ) {
                Button(
                    onClick = {
                        Toast.makeText(context, "AI sedang menulis cerita. Ini bisa memakan waktu hingga 5 menit, Anda boleh kembali ke menu utama.", Toast.LENGTH_LONG).show()

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
                    colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    if (isGenerating) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("AI Sedang Menulis...", color = Color.White, fontWeight = FontWeight.Bold)
                    } else {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Generate Sekarang", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FA))
                .padding(padding)
                .padding(24.dp)
        ) {
            Text(
                "Buat Skenario Hukum Baru",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black
            )
            Text(
                "Pilih peran dan tingkat kesulitan untuk kasus yang akan di-generate oleh AI.",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
            )

            Text("Pilih Peran Anda", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                characters.forEach { char ->
                    SelectionCard(
                        text = char,
                        isSelected = selectedCharacter == char,
                        onClick = { selectedCharacter = char }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text("Tingkat Kesulitan", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                difficulties.forEach { diff ->
                    SelectionCard(
                        text = diff,
                        isSelected = selectedDifficulty == diff,
                        onClick = { selectedDifficulty = diff },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = errorMessage!!,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFFEBEE), RoundedCornerShape(8.dp))
                        .padding(12.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectionCard(text: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) BluePrimary else Color.LightGray
        ),
        color = if (isSelected) BlueSecondary else Color.White,
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = if (isSelected) BluePrimary else Color.DarkGray,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                fontSize = 14.sp
            )
        }
    }
}