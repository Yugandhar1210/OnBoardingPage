package com.example.onboardingpage.activities.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.onboardingpage.activities.data.ActivityItem
import com.example.onboardingpage.activities.theme.LocalActivitiesColors
import com.example.onboardingpage.activities.theme.LocalActivitiesShapes
import com.example.onboardingpage.activities.theme.activitiesGradient

@Composable
fun ActivityTile(
    activity: ActivityItem,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalActivitiesColors.current
    val shapes = LocalActivitiesShapes.current
    val haptics = LocalHapticFeedback.current

    val scale by animateFloatAsState(
        targetValue = if (selected) 0.98f else 1f,
        animationSpec = tween(durationMillis = 160),
        label = "tile-scale"
    )

    val borderColor = if (selected) Color.Transparent else Color(0x331A1A1F)
    val shape = RoundedCornerShape(18.dp)

    Box(
        modifier = modifier
            .testTag("tile_${'$'}{activity.id}")
            .clip(shape)
            .then(
                if (selected) Modifier.background(activitiesGradient, shape) else Modifier.background(Color(0x331A1A1F), shape)
            )
            .border(width = 1.dp, color = borderColor, shape = shape)
            .clickable(
                role = Role.Button,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                haptics.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove)
                onClick()
            }
            .scale(scale)
            .padding(horizontal = 14.dp, vertical = 12.dp)
            .semantics(true) {
                role = Role.Button
                contentDescription = activity.name
                stateDescription = if (selected) "selected" else "not selected"
            },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Icon(
                painter = painterResource(id = activity.iconRes),
                contentDescription = null,
                tint = if (selected) Color.White else colors.primaryLeft,
                modifier = Modifier.size(22.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = activity.name,
                color = if (selected) Color.White else colors.textPrimary,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
        }
    }
}
