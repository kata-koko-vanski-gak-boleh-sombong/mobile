package com.project.edu_law.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.project.edu_law.ui.theme.BluePrimary
import com.project.edu_law.ui.theme.BlueSecondary

@Composable
fun QuizScreen() {
    var currentIndex by remember { mutableIntStateOf(0) }
    var selectedOption by remember { mutableStateOf<Int?>(null) }
    var showInsight by remember { mutableStateOf(false) }

    val questions = listOf(
        Question(
            id = 4,
            title = "The Boundary Conflict of Oakwood Estate",
            description = "A homeowner, Mr. Aris, discovers that his neighbor’s newly constructed fence encroaches three feet onto his registered property. The neighbor, Mrs. Thorne, claims that a previous oral agreement with the former owner allowed this boundary shift twenty years ago. No written record of this easement exists in the land registry.",
            options = listOf(
                "The oral agreement is binding under the principle of equitable estoppel, regardless of land registry status.",
                "The Mirror Principle of the Land Registry implies that only registered interests are binding on a purchaser for value.",
                "Mrs. Thorne has a claim for Adverse Possession if she can prove continuous, exclusive control for the statutory period.",
                "The boundary dispute must be settled through a mandatory local mediation before any legal action can be filed."
            ),
            insightTitle = "The Principle of Stare Decisis",
            insightDescription = "This doctrine obligates courts to look to past, similar issues to guide their decisions. These past decisions are known as precedent. Precedent is a legal principle or rule that is created by a higher court which other courts must follow when a similar case with similar facts arises."
        )
    )

    val currentQuestion = questions[currentIndex]

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FAFC))
                .statusBarsPadding()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Question ${currentQuestion.id} of 12", fontSize = 12.sp, color = Color.Gray)
                Text("33% Complete", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF059669))
            }
            LinearProgressIndicator(
                progress = { 0.33f },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .height(6.dp)
                    .clip(CircleShape),
                color = Color(0xFF059669),
                trackColor = Color(0xFFE2E8F0)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(currentQuestion.title, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1E293B))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(currentQuestion.description, fontSize = 15.sp, lineHeight = 24.sp, color = Color(0xFF475569))
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                currentQuestion.options.forEachIndexed { index, text ->
                    OptionItem(
                        text = text,
                        isSelected = selectedOption == index,
                        onClick = { selectedOption = index }
                    )
                }
            }

            Button(
                onClick = { showInsight = true },
                enabled = selectedOption != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text("Konfirmasi Jawaban", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }

        AnimatedVisibility(
            visible = showInsight,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Text(
                        "LEGAL INSIGHT",
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp,
                        color = Color(0xFF00C4B4),
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(24.dp)) {

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(currentQuestion.insightTitle, fontSize = 20.sp, fontWeight = FontWeight.Bold)

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                currentQuestion.insightDescription,
                                fontSize = 14.sp,
                                color = Color.DarkGray,
                                lineHeight = 20.sp
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                InfoTag(Icons.Default.MenuBook, "Sumber", "Common Law Tradition", Modifier.weight(1f))
                                InfoTag(Icons.Default.Gavel, "Application", "Appellate Procedures", Modifier.weight(1f))
                            }
                        }
                    }
                }

                Button(
                    onClick = {
                        showInsight = false
                        selectedOption = null
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(bottom = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text("Continue to Next Question", fontWeight = FontWeight.Bold)
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
        border = BorderStroke(if (isSelected) 2.dp else 1.dp, if (isSelected) BluePrimary else Color(0xFFE2E8F0)),
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
                    .background(if (isSelected) BlueSecondary else Color.White)
                    .border(1.dp, if (isSelected) BlueSecondary else Color.LightGray, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Box(Modifier.size(8.dp).clip(CircleShape).background(Color.White))
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(text, fontSize = 14.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
        }
    }
}

@Composable
fun InfoTag(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String, modifier: Modifier) {
    Surface(
        modifier = modifier,
        color = Color(0xFFF8FAFC),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color(0xFFF1F5F9))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, null, modifier = Modifier.size(14.dp), tint = Color.Gray)
                Spacer(Modifier.width(4.dp))
                Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
            }
            Text(value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
        }
    }
}

data class Question(
    val id: Int,
    val title: String,
    val description: String,
    val options: List<String>,
    val insightTitle: String,
    val insightDescription: String
)