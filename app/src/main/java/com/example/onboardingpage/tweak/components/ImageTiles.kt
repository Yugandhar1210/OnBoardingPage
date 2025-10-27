package com.example.onboardingpage.tweak.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun AddPhotoTile(onClick: () -> Unit, modifier: Modifier = Modifier, enabled: Boolean = true, badgeText: String? = null) {
    Box(
        modifier = modifier
            .size(110.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(if (enabled) Color(0x40101216) else Color(0x26101216))
            .border(
                1.dp,
                Brush.linearGradient(listOf(if (enabled) Color(0x337B3BFF) else Color(0x1A7B3BFF), Color.Transparent)),
                RoundedCornerShape(18.dp)
            )
            .alpha(if (enabled) 1f else 0.6f)
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Add, contentDescription = "Add photo", tint = if (enabled) Color(0xFFB8B7C0) else Color(0x66B8B7C0))
            Spacer(Modifier.height(6.dp))
            Text("Add Photo", color = if (enabled) Color(0xFFB8B7C0) else Color(0x66B8B7C0), fontSize = 12.sp)
        }
        if (!enabled && badgeText != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(6.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        Brush.horizontalGradient(listOf(Color(0xFF4C00FF), Color(0xFFFF4D97)))
                    )
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
