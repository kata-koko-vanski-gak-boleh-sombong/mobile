package com.project.edu_law.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.edu_law.ui.theme.BluePrimary
import com.project.edu_law.ui.theme.BlueSecondary
import com.project.edu_law.ui.theme.GrayBorder
import com.project.edu_law.ui.theme.GrayText
import com.project.edu_law.ui.theme.GreenSuccess
import com.project.edu_law.ui.theme.GreenText

@Composable
fun QuizScreen() {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
            .padding(16.dp)
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                Icons.Default.ArrowBackIosNew,
                contentDescription = "Back",
                tint = BluePrimary,
                modifier = Modifier.size(20.dp)
            )
            Text("Legal Scenario #12", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Surface (
                color = BlueSecondary,
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Timer,
                        contentDescription = null,
                        tint = BluePrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("08:45", color = BluePrimary, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Question 1 of 5", fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Text("20% Complete", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = BluePrimary)
        }
        LinearProgressIndicator(
            progress = { 0.2f },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .height(8.dp)
                .clip(CircleShape),
            color = Color(0xFF059669),
            trackColor = GrayBorder
        )

        Spacer(Modifier.height(16.dp))

        Card (
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, GrayBorder),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(color = Color(0xFFFFEDD5), shape = RoundedCornerShape(8.dp)) {
                        Text(
                            "PROPERTY LAW",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF9A3412)
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    Text("• 5 min read", fontSize = 12.sp, color = GrayText)
                }
                Spacer(Modifier.height(12.dp))
                Text("Sengketa Lahan Sentul", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                Spacer(Modifier.height(8.dp))
                Text(
                    "Sebuah pengembang mengklaim lahan 10 hektar berdasarkan SHGB (2015), namun warga lokal telah menggarapnya sejak 1980-an tanpa sertifikat formal.",
                    color = Color.DarkGray,
                    lineHeight = 22.sp,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(Modifier.height(20.dp))
        Text("Manakah langkah hukum paling tepat bagi warga?", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
        Spacer(Modifier.height(12.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            QuizOption("Gugat pembatalan SHGB ke PTUN atas dalil cacat administrasi.", false)
            QuizOption("Ajukan sertifikasi PTSL melalui penguasaan fisik 20 tahun.", true)
            QuizOption("Lakukan aksi demonstrasi sebagai bentuk kedaulatan rakyat.", false)
        }

        Spacer(Modifier.height(24.dp))

        Surface(
            color = GreenSuccess,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
                Surface(
                    color = Color(0xFFBFEFE0),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = GreenText,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("Legal Insight", color = GreenText, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(
                        "Berdasarkan PP No. 24 Tahun 1997, penguasaan fisik tanah selama 20 tahun memberikan hak prioritas permohonan hak milik bagi warga.",
                        fontSize = 13.sp,
                        color = GreenText,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        Spacer(Modifier.weight(1f))

        Row (
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextButton (
                onClick = { /* Handle Save */ },
                modifier = Modifier.weight(1f)
            ) {
                Text("Save Draft", color = BluePrimary, fontWeight = FontWeight.Bold)
            }
            Button (
                onClick = { /* Handle Next */ },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(12.dp)
            ) {
                Text("Next Question", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun QuizOption(text: String, isSelected: Boolean) {
    val borderColor = if (isSelected) BluePrimary else GrayBorder
    val backgroundColor = if (isSelected) BlueSecondary else Color.White
    val borderWidth = if (isSelected) 2.dp else 1.dp

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(borderWidth, borderColor),
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isSelected,
                onClick = null,
                colors = RadioButtonDefaults.colors(selectedColor = BluePrimary)
            )
            Spacer(Modifier.width(8.dp))
            Text(text, fontSize = 14.sp, color = if (isSelected) BluePrimary else Color.Black)
        }
    }
}