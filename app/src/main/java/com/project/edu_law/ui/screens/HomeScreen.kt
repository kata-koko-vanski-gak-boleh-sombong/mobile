package com.project.edu_law.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val PrimaryBlue = Color(0xFF004080)
val BackgroundGray = Color(0xFFF8F9FA)

@Composable
fun HomeScreen() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
            .verticalScroll(scrollState)
            .padding(20.dp)
            .statusBarsPadding()
    ) {
        Text(
            text = "EduJustice",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = PrimaryBlue
        )
        Text(
            text = "Pahami Hukum, Tegakkan Keadilan.",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = PrimaryBlue)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Icon(
                    imageVector = Icons.Default.Balance,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Selamat Datang di Simulasi Rule of Law",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    lineHeight = 28.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Pelajari bagaimana setiap keputusan berdampak pada indeks keadilan dunia.",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        Text("Pusat Informasi", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
        Spacer(modifier = Modifier.height(16.dp))

        ExpandableInfoCard(
            title = "Apa itu WJP (World Justice Project)?",
            icon = Icons.Default.Public
        ) {
            Text(
                text = "World Justice Project (WJP) adalah organisasi independen yang mengukur penerapan supremasi hukum (Rule of Law) di seluruh dunia.\n\nMelalui Indeks WJP, kita dapat melihat gambaran nyata tentang seberapa adil, transparan, dan akuntabel suatu negara atau institusi dalam menjalankan aturan hukum di kehidupan sehari-hari.",
                fontSize = 14.sp,
                color = Color.DarkGray,
                lineHeight = 22.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        ExpandableInfoCard(
            title = "4 Pilar Indikator Simulasi",
            icon = Icons.Default.AccountBalance
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                PillarItem(
                    icon = Icons.Default.Security,
                    color = Color(0xFFC62828),
                    title = "Ketiadaan Korupsi (Absence of Corruption)",
                    desc = "Mengukur sejauh mana pejabat atau pemegang wewenang tidak menyalahgunakan kekuasaan untuk keuntungan pribadi."
                )
                PillarItem(
                    icon = Icons.Default.Group,
                    color = Color(0xFF2E7D32),
                    title = "Hak Fundamental (Fundamental Rights)",
                    desc = "Perlindungan terhadap hak asasi manusia, kebebasan berpendapat, dan perlakuan yang setara tanpa diskriminasi."
                )
                PillarItem(
                    icon = Icons.Default.AccountBalanceWallet,
                    color = Color(0xFF6A1B9A),
                    title = "Keadilan Perdata (Civil Justice)",
                    desc = "Sistem penyelesaian masalah yang mudah diakses, tidak memihak, dan bebas dari pengaruh buruk pihak luar."
                )
                PillarItem(
                    icon = Icons.Default.Gavel,
                    color = Color(0xFF1565C0),
                    title = "Keadilan Pidana (Criminal Justice)",
                    desc = "Sistem investigasi dan pemberian sanksi yang efektif, adil, serta menjunjung asas praduga tak bersalah."
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        ExpandableInfoCard(
            title = "Kaitan Pilar dengan Kebijakan Hukum",
            icon = Icons.Default.Timeline
        ) {
            Text(
                text = "Setiap keputusan dalam simulasi ini akan langsung memengaruhi 4 pilar di atas secara sistemik.\n\nContoh: Membiarkan penyalahgunaan kuota sekolah demi relasi mungkin terlihat sebagai kompromi kecil, namun secara hukum ini menurunkan skor 'Ketiadaan Korupsi' sekaligus mencederai 'Hak Fundamental' warga lain yang kehilangan akses pendidikan yang adil.",
                fontSize = 14.sp,
                color = Color.DarkGray,
                lineHeight = 22.sp
            )
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun ExpandableInfoCard(
    title: String,
    icon: ImageVector,
    content: @Composable () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = tween(300),
        label = "ArrowRotation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { expanded = !expanded }
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            ),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(PrimaryBlue.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(20.dp))
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.Black,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Expand",
                    tint = Color.Gray,
                    modifier = Modifier.rotate(rotation)
                )
            }

            if (expanded) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = Color(0xFFF0F0F0))
                    Spacer(modifier = Modifier.height(16.dp))
                    content()
                }
            }
        }
    }
}

@Composable
fun PillarItem(icon: ImageVector, color: Color, title: String, desc: String) {
    Row(verticalAlignment = Alignment.Top) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(4.dp))
            Text(desc, fontSize = 13.sp, color = Color.DarkGray, lineHeight = 20.sp)
        }
    }
}