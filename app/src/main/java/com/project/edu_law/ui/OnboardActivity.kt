package com.project.edu_law.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.edu_law.MainActivity
import com.project.edu_law.R
import com.project.edu_law.ui.theme.*
import kotlinx.coroutines.launch

data class OnboardingPage(
    val imageRes: Int,
    val title: String,
    val description: String
)

class OnboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("EduLawPrefs", MODE_PRIVATE)
        val isFirstTime = sharedPreferences.getBoolean("is_first_time", true)

        if (!isFirstTime) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        enableEdgeToEdge()
        setContent {
            EdulawTheme {
                OnboardingScreen(
                    onFinish = {
                        sharedPreferences.edit().putBoolean("is_first_time", false).apply()

                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                )
            }
        }
    }
}

@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    val pages = listOf(
        OnboardingPage(
            imageRes = R.drawable.placeholder,
            title = "Sistem Hukum,\nAkar Pendidikan",
            description = "Ketimpangan pendidikan bukan sekadar kurangnya guru, tapi gejala dari sistem hukum yang perlu diperbaiki secara menyeluruh."
        ),
        OnboardingPage(
            imageRes = R.drawable.placeholder,
            title = "Simulasi Dilema\nKeadilan Nyata",
            description = "Hadapi skenario nyata dari data WJP 2025. Setiap keputusanmu akan menguji integritas, transparansi, dan kesetaraan hak."
        ),
        OnboardingPage(
            imageRes = R.drawable.placeholder,
            title = "Bangun Masa Depan\nIndonesia Emas",
            description = "Pahami bahwa perlindungan hak asasi dan ketiadaan korupsi adalah kurikulum utama untuk mencapai keadilan bagi semua anak bangsa."
        )
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()
    val isLastPage = pagerState.currentPage == pages.size - 1

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            OnboardingTopBar(
                onSkip = onFinish,
                showSkip = !isLastPage
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.Top
            ) { index ->
                OnboardingPageContent(page = pages[index])
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 42.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DotsIndicator(
                    totalDots = pages.size,
                    selectedIndex = pagerState.currentPage,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                Button(
                    onClick = {
                        if (isLastPage) onFinish()
                        else scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BluePrimary)
                ) {
                    Text(
                        text = if (isLastPage) "Mulai Beraksi" else "Lanjut",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun OnboardingTopBar(onSkip: () -> Unit, showSkip: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(64.dp)
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Edu Justice",
            color = BluePrimary,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-0.5).sp
            )
        )

        if (showSkip) {
            TextButton(onClick = onSkip) {
                Text(
                    text = "Lewati",
                    color = GrayText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        } else {
            Spacer(modifier = Modifier.width(48.dp))
        }
    }
}

@Composable
fun OnboardingPageContent(page: OnboardingPage) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = page.imageRes),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = page.title,
            color = Color(0xFF111827),
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                lineHeight = 36.sp
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = page.description,
            color = GrayText,
            style = MaterialTheme.typography.bodyLarge.copy(
                textAlign = TextAlign.Center,
                lineHeight = 26.sp
            )
        )
    }
}

@Composable
fun DotsIndicator(totalDots: Int, selectedIndex: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(totalDots) { index ->
            val isSelected = index == selectedIndex
            val width by animateDpAsState(
                targetValue = if (isSelected) 32.dp else 10.dp,
                label = "width"
            )

            Box(
                modifier = Modifier
                    .height(8.dp)
                    .width(width)
                    .clip(CircleShape)
                    .background(if (isSelected) BluePrimary else GrayBorder)
            )
        }
    }
}