package com.project.edu_law.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.project.edu_law.ui.screens.viewmodel.ChatMessage
import com.project.edu_law.ui.screens.viewmodel.ScenarioViewModel
import com.project.edu_law.ui.theme.BluePrimary
import com.project.edu_law.ui.theme.BlueSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    endingContext: String,
    viewModel: ScenarioViewModel,
    navController: NavController
) {
    val messages by viewModel.chatMessages.collectAsState()
    val isChatLoading by viewModel.isChatLoading.collectAsState()
    var inputText by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.clearChat()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tanya AI Hukum", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = BluePrimary,
                    navigationIconContentColor = BluePrimary
                )
            )
        },
        bottomBar = {
            Surface(
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        placeholder = { Text("Tanya alasan hukumnya...") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BluePrimary,
                            unfocusedBorderColor = Color.LightGray
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    FloatingActionButton(
                        onClick = {
                            if (inputText.isNotBlank()) {
                                viewModel.sendChatMessage(endingContext, inputText)
                                inputText = ""
                            }
                        },
                        containerColor = BluePrimary,
                        shape = CircleShape,
                        modifier = Modifier.size(50.dp)
                    ) {
                        Icon(Icons.Default.Send, contentDescription = "Kirim", tint = Color.White)
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
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                reverseLayout = false
            ) {
                item {
                    ChatBubble(
                        message = ChatMessage("Halo! Silakan tanya apa saja terkait simulasi yang baru Anda selesaikan. Saya sudah membaca konteks keputusan Anda.", false)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(messages) { msg ->
                    ChatBubble(message = msg)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                if (isChatLoading) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = BluePrimary,
                                strokeWidth = 2.dp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .background(
                    color = if (message.isUser) BluePrimary else Color.White,
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (message.isUser) 16.dp else 4.dp,
                        bottomEnd = if (message.isUser) 4.dp else 16.dp
                    )
                )
                .border(
                    width = 1.dp,
                    color = if (message.isUser) Color.Transparent else Color.LightGray.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(
                        topStart = 16.dp, topEnd = 16.dp,
                        bottomStart = if (message.isUser) 16.dp else 4.dp,
                        bottomEnd = if (message.isUser) 4.dp else 16.dp
                    )
                )
                .padding(12.dp)
        ) {
            Text(
                text = message.text,
                color = if (message.isUser) Color.White else Color.Black,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
    }
}