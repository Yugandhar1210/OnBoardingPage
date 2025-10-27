package com.example.onboardingpage.tweak.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RoundedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    singleLine: Boolean = true,
    modifier: Modifier = Modifier
) {
    val placeholderColor = Color(0xFFB8B7C0)
    val textColor = Color.White

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = placeholderColor, fontSize = 13.sp, fontWeight = FontWeight.Medium) },
        textStyle = TextStyle(color = textColor, fontSize = 14.sp),
        singleLine = singleLine,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = textColor,
            unfocusedTextColor = textColor,
            focusedBorderColor = Color(0x338A65FF),
            unfocusedBorderColor = Color(0x22545460),
            cursorColor = Color(0xFF8A65FF),
            focusedContainerColor = Color(0x551A1A1F),
            unfocusedContainerColor = Color(0x401A1A1F),
            focusedPlaceholderColor = placeholderColor,
            unfocusedPlaceholderColor = placeholderColor
        )
    )
}
