package com.example.onboardingpage.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.onboardingpage.GradientPillButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpScreen(
    phone: String,
    onBack: () -> Unit,
    onVerified: () -> Unit
) {
    val context = LocalContext.current
    var otp by remember { mutableStateOf("") }

    val gradientBg = Brush.verticalGradient(
        listOf(Color(0xFF0B0D12), Color(0xFF0E0712), Color(0xFF191A24))
    )

    fun verify() {
        if (otp == "111111") {
            onVerified()
        } else {
            Toast.makeText(context, "Invalid OTP", Toast.LENGTH_SHORT).show()
        }
    }

    // Auto verify when 6 digits entered
    LaunchedEffect(otp) {
        if (otp.length == 6) verify()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                title = { Text("Verify Your Mobile Number") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = Color.Transparent
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBg)
                .padding(inner)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Verify Your Mobile Number", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                Text("OTP sent to", color = Color(0xFFB8B7C0))
                Text(phone, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Medium)

                // Segmented 6-digit OTP using a single BasicTextField with decorated boxes
                BasicTextField(
                    value = otp,
                    onValueChange = { input ->
                        val filtered = input.filter { it.isDigit() }.take(6)
                        otp = filtered
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    singleLine = true,
                    textStyle = TextStyle(color = Color.Transparent), // hide raw text
                    cursorBrush = SolidColor(Color.Transparent),
                    decorationBox = {
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                            val colors = listOf(Color(0xFF4C00FF), Color(0xFFFF4D97))
                            repeat(6) { i ->
                                val char = otp.getOrNull(i)?.toString() ?: ""
                                val isActive = (i == otp.length.coerceAtMost(5)) && otp.length < 6
                                val borderBrush = if (isActive) Brush.horizontalGradient(colors) else Brush.horizontalGradient(listOf(Color(0x22545460), Color(0x22545460)))
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(58.dp)
                                        .background(Color(0x401A1A1F), RoundedCornerShape(14.dp))
                                        .border(2.dp, borderBrush, RoundedCornerShape(14.dp))
                                ) {
                                    Text(
                                        text = char,
                                        color = Color.White,
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                )
            }

            GradientPillButton(
                text = "Verify",
                onClick = { verify() }
            )
        }
    }
}
