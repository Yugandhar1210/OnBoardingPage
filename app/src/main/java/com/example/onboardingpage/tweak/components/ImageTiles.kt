package com.example.onboardingpage.tweak.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun AddPhotoTile(onClick: () -> Unit, modifier: Modifier = Modifier, enabled: Boolean = true, badgeText: String? = null) {
    val shape = RoundedCornerShape(18.dp)

    // Neon gradient used across border + plus icon
    val gradStartEnabled = Color(0xFF4C00FF)
    val gradEndEnabled = Color(0xFFFF4D97)
    val gradStartDisabled = Color(0x334C00FF)
    val gradEndDisabled = Color(0x33FF4D97)

    val borderBrush = Brush.linearGradient(
        listOf(if (enabled) gradStartEnabled else gradStartDisabled, if (enabled) gradEndEnabled else gradEndDisabled)
    )

    // Subtle inner glow (kept minimal; no animation)
    val glowBrush = Brush.radialGradient(
        listOf(
            if (enabled) Color(0x224C00FF) else Color(0x114C00FF),
            if (enabled) Color(0x22FF4D97) else Color(0x11FF4D97),
            Color.Transparent
        )
    )

    Box(
        modifier = modifier
            .size(110.dp)
            .alpha(if (enabled) 1f else 0.6f)
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {

        // Foreground glass surface + gradient border
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(shape)
                .background(if (enabled) Color(0x40101216) else Color(0x26101216))
                .border(2.dp, borderBrush, shape)
        )

        // Center content: circular ring + gradient-tinted plus icon
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            val circleBrush = borderBrush
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0x26FFFFFF))
                    .border(2.dp, circleBrush, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                // Draw gradient over the icon using SrcAtop to tint the glyph
                val iconBrush = borderBrush
                Text(
                    text = "+",
                    style = TextStyle(
                        fontSize = 22.sp,
                        brush = Brush.linearGradient(
                            colors = listOf(gradStartEnabled,gradEndEnabled),
                        )
                    ),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Spacer(Modifier.height(6.dp))
            Text("Add Photo", color = if (enabled) Color(0xFFB8B7C0) else Color(0x66B8B7C0), fontSize = 12.sp)
        }

        if (!enabled && badgeText != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(6.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Brush.horizontalGradient(listOf(gradStartEnabled, gradEndEnabled)))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(badgeText, color = Color.White, fontSize = 10.sp)
            }
        }
    }
}

@Composable
fun ImageTile(uri: Uri, onRemove: () -> Unit, modifier: Modifier = Modifier) {
    Box(modifier = modifier.size(110.dp).clip(RoundedCornerShape(18.dp))) {
        AsyncImage(
            model = uri,
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(6.dp)
                .size(24.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xAA000000))
                .clickable { onRemove() },
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Close, contentDescription = "Remove", tint = Color.White)
        }
    }
}
@Preview
@Composable
fun AddPhotoTilePreview() {
    AddPhotoTile(onClick = {})
}

//@Preview
//@Composable
//fun AddPhotoTileDisabledPreview() {
//    AddPhotoTile(
//        onClick = {},
//        enabled = false,
//        badgeText = "SOON"
//    )
//}

