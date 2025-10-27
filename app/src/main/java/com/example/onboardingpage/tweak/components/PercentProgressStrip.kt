package com.example.onboardingpage.tweak.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PercentProgressStrip(progress: Float, modifier: Modifier = Modifier) {
    val p by animateFloatAsState(targetValue = progress.coerceIn(0f, 1f), animationSpec = tween(600), label = "progress")
    Box(
        modifier = modifier
            .height(10.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0x332A2A2A))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(p)
                .fillMaxHeight()
                .background(
                    Brush.horizontalGradient(
                        listOf(Color(0xFF8A65FF), Color(0xFFFF3FD8))
                    )
                )
        )
    }
}
