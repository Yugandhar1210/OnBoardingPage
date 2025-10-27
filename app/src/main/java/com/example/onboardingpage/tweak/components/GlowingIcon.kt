package com.example.onboardingpage.tweak.components

/**
 * GlowingIcon
 *  - Small utility composable that renders an [Icon] with a soft, pulsating neon glow
 *    behind it. The glow is created using a radial gradient brush whose alpha gently
 *    animates between 0.6–1.0. This matches the visual language of the Tweak Profile
 *    screen (neon accents on a dark glass background).
 *
 * Where it's used
 *  - Intended for small leading icons in section headers or cards inside the
 *    Tweak Profile flow. You can also use it inside buttons or tiles when you
 *    want a subtle “alive” feel without being distracting.
 *
 * Example
 *  GlowingIcon(
 *      imageVector = Icons.Default.CameraAlt,
 *      contentDescription = "Verification"
 *  )
 */

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun GlowingIcon(
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = Color.White
) {
    // Infinite transition drives the glow pulse (alpha up/down)
    val infinite = rememberInfiniteTransition(label = "glow")
    val alpha by infinite.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800),
            repeatMode = RepeatMode.Reverse
        ), label = "pulse"
    )

    // Radial gradient ring that fades to transparent; alpha is animated above
    val ring = Brush.radialGradient(
        listOf(Color(0xFF4C00FF).copy(alpha = 0.7f * alpha), Color.Transparent)
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(36.dp)
            .clip(CircleShape)
            // Draw the glowing ring as the background of the circular container
            .background(ring)
    ) {
        // Foreground icon rendered at the center on top of the glow
        Icon(imageVector, contentDescription, tint = tint)
    }
}
