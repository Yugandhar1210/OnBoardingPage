package com.example.onboardingpage.activities.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Immutable
data class ActivitiesColors(
    val primaryLeft: Color = Color(0xFF7B3BFF),
    val primaryRight: Color = Color(0xFFFFB86C),
    val bgDark1: Color = Color(0xFF0B0912),
    val bgDark2: Color = Color(0xFF0F0D16),
    val textPrimary: Color = Color(0xFFF2F1F6),
    val mutedText: Color = Color(0xFFB8B7C0),
)

@Immutable
data class ActivitiesShapes(
    val card: RoundedCornerShape = RoundedCornerShape(24.dp),
    val chip: RoundedCornerShape = RoundedCornerShape(14.dp),
)

val LocalActivitiesColors = staticCompositionLocalOf { ActivitiesColors() }
val LocalActivitiesShapes = staticCompositionLocalOf { ActivitiesShapes() }

val activitiesGradient: Brush
    @Composable get() = Brush.horizontalGradient(
        listOf(LocalActivitiesColors.current.primaryLeft, LocalActivitiesColors.current.primaryRight)
    )

@Composable
fun ActivitiesTheme(
    colors: ActivitiesColors = ActivitiesColors(),
    shapes: ActivitiesShapes = ActivitiesShapes(),
    content: @Composable () -> Unit
) {
    androidx.compose.runtime.CompositionLocalProvider(
        LocalActivitiesColors provides colors,
        LocalActivitiesShapes provides shapes,
    ) { content() }
}
