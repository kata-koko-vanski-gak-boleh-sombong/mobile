package com.project.edu_law.ui.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.edu_law.MainActivity
import com.project.edu_law.R
import com.project.edu_law.ui.theme.EdulawTheme
data class OnboardingPage(
    val imageRes: Int,
    val title: String,
    val description: String
)

class OnboardActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EdulawTheme {
                OnboardingScreen(
                    onFinish = { navigateToMain() }
                )
            }
        }
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}

@Composable
fun OnboardingScreen(onFinish: () -> Unit) {

    val pages = listOf(
        OnboardingPage(
            imageRes = R.drawable.placeholder,
            title = "Democratizing Legal Knowledge",
            description = "Making the complex world of law accessible, understandable, and fair for everyone."
        ),
        OnboardingPage(
            imageRes = R.drawable.placeholder,
            title = "Know Your Rights",
            description = "Understand your legal rights in everyday situations with clear, simple explanations."
        ),
        OnboardingPage(
            imageRes = R.drawable.placeholder,
            title = "Justice For All",
            description = "Access expert legal guidance and resources whenever and wherever you need them."
        )
    )

    var currentPage by remember { mutableIntStateOf(0) }
    val isLastPage = currentPage == pages.lastIndex

    val primaryBlue = Color(0xFF1A56FF)
    val backgroundColor = Color(0xFFFFFFFF)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            OnboardingTopBar(
                onSkip = onFinish,
                primaryBlue = primaryBlue
            )

            OnboardingPageContent(
                page = pages[currentPage],
                modifier = Modifier.weight(1f)
            )

            DotsIndicator(
                totalDots = pages.size,
                selectedIndex = currentPage,
                primaryBlue = primaryBlue,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )

            GetStartedButton(
                isLastPage = isLastPage,
                primaryBlue = primaryBlue,
                onClick = {
                    if (isLastPage) onFinish() else currentPage++
                },
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 24.dp)
            )
        }
    }
}

@Composable
fun OnboardingTopBar(
    onSkip: () -> Unit,
    primaryBlue: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 40.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Justice Academy",
            color = primaryBlue,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        TextButton(onClick = onSkip) {
            Text(
                text = "Skip",
                color = Color(0xFF4A5568),
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun OnboardingPageContent(
    page: OnboardingPage,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = page.imageRes),
            contentDescription = page.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = page.title,
            color = Color(0xFF1A202C),
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            textAlign = TextAlign.Center,
            lineHeight = 36.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = page.description,
            color = Color(0xFF4A5568),
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )
    }
}

@Composable
fun DotsIndicator(
    totalDots: Int,
    selectedIndex: Int,
    primaryBlue: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(totalDots) { index ->
            val isSelected = index == selectedIndex
            val dotWidth by animateDpAsState(
                targetValue = if (isSelected) 28.dp else 10.dp,
                animationSpec = tween(durationMillis = 300),
                label = "dot_width_$index"
            )
            Box(
                modifier = Modifier
                    .width(dotWidth)
                    .height(10.dp)
                    .clip(CircleShape)
                    .background(
                        color = if (isSelected) primaryBlue else Color(0xFFCBD5E0)
                    )
            )
        }
    }
}

@Composable
fun GetStartedButton(
    isLastPage: Boolean,
    primaryBlue: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = primaryBlue)
    ) {
        Text(
            text = if (isLastPage) "Get Started" else "Next",
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OnboardingScreenPreview() {
    EdulawTheme {
        OnboardingScreen(onFinish = {})
    }
}