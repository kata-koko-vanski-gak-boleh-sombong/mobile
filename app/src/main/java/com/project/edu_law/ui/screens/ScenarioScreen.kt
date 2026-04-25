package com.project.edu_law.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun LegalScenarioScreen() {
    Scaffold (
    ) { padding ->
        Column (
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

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp, horizontal = 4.dp)
            ) {
                items(scenarios.size) { index ->
                    ScenarioCard(scenarios[index])
                }
            }
        }
    }
}

@Composable
fun ScenarioCard(scenario: LegalScenario) {
    Card (
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
                Surface (
                    color = scenario.roleColor,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = scenario.role,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = scenario.contentColor
                        )
                    )
                }
                Icon(scenario.icon, contentDescription = null, tint = Color.LightGray)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = scenario.title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )

            Text(
                text = scenario.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button (
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF004080)),
                shape = RoundedCornerShape(12.dp) // Konsisten dengan gaya badge
            ) {
                Text(text = scenario.buttonText, color = Color.White)
            }
        }
    }
}

data class LegalScenario(
    val title: String,
    val description: String,
    val role: String, // Mengganti pov
    val roleColor: Color,
    val contentColor: Color, // Tambahan agar teks di dalam badge kontras
    val buttonText: String,
    val icon: ImageVector
)

val scenarios = listOf(
    LegalScenario(
        "Sengketa Lahan Oakwood",
        "Analisis hak atas tanah dan prosedur pengadaan lahan...",
        "Peran: Pemerintah",
        Color(0xFF002366),
        Color.White,
        "Mulai simulasi",
        Icons.Default.Gavel
    ),
    LegalScenario(
        "Akses Informasi Publik",
        "Mempelajari batasan dan prosedur permohonan informasi...",
        "Peran: Masyarakat",
        Color(0xFFE3F2FD),
        Color(0xFF1976D2),
        "Mulai simulasi",
        Icons.Default.People
    ),
    LegalScenario(
        "Izin Lingkungan AMDAL",
        "Menavigasi kepatuhan hukum lingkungan hidup dalam...",
        "Peran: Korporasi",
        Color(0xFFF5F5F5), // Abu-abu terang
        Color(0xFF424242),
        "Mulai simulasi",
        Icons.Default.BusinessCenter
    ),
    LegalScenario(
        "Kebijakan Pajak Daerah",
        "Implementasi UU HKPD dalam pemungutan retribusi...",
        "Peran: Pemerintah",
        Color(0xFF002366),
        Color.White,
        "Mulai simulasi",
        Icons.Default.AccountBalance
    )
)