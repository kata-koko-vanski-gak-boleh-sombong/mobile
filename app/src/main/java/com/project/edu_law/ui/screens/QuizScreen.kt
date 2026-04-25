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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.edu_law.data.Question
import com.project.edu_law.ui.theme.*

@Composable
fun QuizScreen() {
    var currentIndex by remember { mutableIntStateOf(0) }
    var selectedOption by remember { mutableStateOf<Int?>(null) }
    var showInsight by remember { mutableStateOf(false) }

    val mainScrollState = rememberScrollState()
    val insightScrollState = rememberScrollState()

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
                    Text("Question ${currentQuestion.id} of 12", fontSize = 12.sp, color = GrayText)
                    Text("33% Complete", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BluePrimary)
                }
                LinearProgressIndicator(
                    progress = { 0.33f },
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
                        Text(currentQuestion.title, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Color.Black)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(currentQuestion.description, fontSize = 15.sp, lineHeight = 24.sp, color = GrayText)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    currentQuestion.options.forEachIndexed { index, text ->
                        OptionItem(
                            text = text,
                            isSelected = selectedOption == index,
                            onClick = { selectedOption = index }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = White,
                shadowElevation = 8.dp
            ) {
                Button(
                    onClick = { showInsight = true },
                    enabled = selectedOption != null,
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
                    Text("Konfirmasi Jawaban", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = White)
                }
            }
        }

        // --- OVERLAY LEGAL INSIGHT ---
        AnimatedVisibility(
            visible = showInsight,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MintBackground) // Menggunakan MintBackground dari Theme
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
                        "Legal Insight",
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

                            Text(currentQuestion.insightTitle, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                currentQuestion.insightDescription,
                                fontSize = 14.sp,
                                color = GrayText,
                                lineHeight = 22.sp
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                InfoTag(Icons.Default.MenuBook, "Sumber", "Common Law Tradition", Modifier.weight(1f))
                                InfoTag(Icons.Default.Gavel, "Application", "Appellate Procedures", Modifier.weight(1f))
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
                            showInsight = false
                            selectedOption = null
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Text("Next Scenario", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = White)
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