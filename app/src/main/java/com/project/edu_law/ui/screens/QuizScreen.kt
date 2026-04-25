package com.project.edu_law.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.edu_law.data.Question
import com.project.edu_law.ui.theme.*

@Composable
fun QuizScreen() {
    val questions = listOf(
        Question(
            id = 1,
            category = "PROPERTY LAW",
            title = "Sengketa Lahan Sentul",
            description = "Sebuah pengembang mengklaim lahan 10 hektar berdasarkan SHGB (2015), namun warga lokal telah menggarapnya sejak 1980-an tanpa sertifikat formal. Pengembang mulai memasang pagar pembatas di area tersebut.",
            options = listOf(
                "Gugat pembatalan SHGB ke PTUN atas dalil cacat administrasi.",
                "Ajukan sertifikasi PTSL melalui penguasaan fisik 20 tahun.",
                "Lakukan aksi demonstrasi sebagai bentuk kedaulatan rakyat."
            ),
            correctAnswerIndex = 1,
            insight = "Berdasarkan PP No. 24 Tahun 1997, penguasaan fisik tanah selama 20 tahun memberikan hak prioritas permohonan hak milik bagi warga."
        ),
        Question(
            id = 2,
            category = "CRIMINAL LAW",
            title = "Kasus Pencemaran Nama Baik",
            description = "A mengkritik pelayanan rumah sakit di media sosial karena merasa tidak dilayani dengan baik, lalu pihak RS melaporkan A dengan UU ITE Pasal 27 ayat 3.",
            options = listOf(
                "A harus langsung dipenjara tanpa sidang.",
                "Kritik untuk kepentingan umum bukan pencemaran nama baik.",
                "Setiap postingan negatif otomatis adalah tindak pidana."
            ),
            correctAnswerIndex = 1,
            insight = "SKB 3 Menteri tentang UU ITE menegaskan bahwa kritik atau penilaian terhadap kenyataan bukan merupakan delik pidana pencemaran nama baik."
        )
    )

    var currentIndex by remember { mutableIntStateOf(0) }
    var selectedOption by remember { mutableStateOf<Int?>(null) }
    var isAnswered by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    val currentQuestion = questions[currentIndex]
    val progress = (currentIndex + 1).toFloat() / questions.size

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(16.dp)
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
                Text("Legal Scenario #${currentQuestion.id}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Surface(color = BlueSecondary, shape = RoundedCornerShape(16.dp)) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Timer, null, tint = BluePrimary, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("08:45", color = BluePrimary, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("Question ${currentIndex + 1} of ${questions.size}", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                Text("${(progress * 100).toInt()}% Complete", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = BluePrimary)
            }
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .height(8.dp)
                    .clip(CircleShape),
                color = Color(0xFF059669),
                trackColor = GrayBorder
            )

            Spacer(Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, GrayBorder),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(color = Color(0xFFFFEDD5), shape = RoundedCornerShape(8.dp)) {
                            Text(
                                currentQuestion.category,
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
                    Text(currentQuestion.title, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        currentQuestion.description,
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
                currentQuestion.options.forEachIndexed { index, optionText ->
                    QuizOption(
                        text = optionText,
                        isSelected = selectedOption == index,
                        onSelect = {
                            if (!isAnswered) {
                                selectedOption = index
                                isAnswered = true
                            }
                        }
                    )
                }
            }

            if (isAnswered) {
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
                            Icon(Icons.Default.Lightbulb, null, tint = GreenText, modifier = Modifier.padding(8.dp))
                        }
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text("Legal Insight", color = GreenText, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(
                                currentQuestion.insight,
                                fontSize = 13.sp,
                                color = GreenText,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }

        Surface(
            color = Color.White,
            shadowElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .navigationBarsPadding(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TextButton(
                    onClick = { /* Save Draft */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Save Draft", color = BluePrimary, fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = {
                        if (currentIndex < questions.size - 1) {
                            currentIndex++
                            selectedOption = null
                            isAnswered = false
                        }
                    },
                    enabled = isAnswered,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(12.dp)
                ) {
                    Text(
                        if (currentIndex == questions.size - 1) "Finish" else "Next Question",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun QuizOption(text: String, isSelected: Boolean, onSelect: () -> Unit) {
    val borderColor = if (isSelected) BluePrimary else GrayBorder
    val backgroundColor = if (isSelected) BlueSecondary else Color.White
    val borderWidth = if (isSelected) 2.dp else 1.dp

    Surface(
        onClick = onSelect,
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
            Text(
                text,
                fontSize = 14.sp,
                color = if (isSelected) BluePrimary else Color.Black,
                lineHeight = 20.sp
            )
        }
    }
}