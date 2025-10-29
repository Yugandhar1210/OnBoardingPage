package com.example.onboardingpage.tweak.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Rounded frosted glass panel with subtle inner gradient border and shadow.
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    corner: Int = 20,
    frosted: Boolean = false, // when true, applies a content blur (not backdrop) — default off to keep text crisp
    content: @Composable ColumnScope.() -> Unit,
    
) {
    val shape = RoundedCornerShape(corner.dp)
    val glass = Color(0x731A1A1F) // #1A1A1F with ~0.45 alpha
    val borderBrush = Brush.linearGradient(
        listOf(Color(0x207B3BFF), Color(0x00000000))
    )

    val base = modifier
        .fillMaxWidth()
        .clip(shape)
        .background(glass)
        .border(width = 1.dp, brush = borderBrush, shape = shape)
        .padding(18.dp)

    Box(
        modifier = if (frosted) {
            // Note: Compose's blur() blurs the content inside this Box, not the background behind it.
            // Kept as an opt-in, default OFF so your text & icons stay sharp.
            base.blur(12.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
        } else base
    ) {
        Column(content = content)
    }
}
