package com.example.onboardingpage.activities.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.onboardingpage.activities.data.ActivityItem
import com.example.onboardingpage.activities.data.Category
import com.example.onboardingpage.activities.theme.LocalActivitiesColors
import com.example.onboardingpage.activities.theme.LocalActivitiesShapes
import com.example.onboardingpage.activities.theme.activitiesGradient

@Composable
fun CategoryCard(
    category: Category,
    selectedIds: Set<String>,
    isExpanded: Boolean,
    onToggle: (String) -> Unit,
    onToggleExpand: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalActivitiesColors.current
    val shapes = LocalActivitiesShapes.current

    Column(
        modifier = modifier
            .testTag("category_${category.id}")
            .background(Color(0x221A1A1F), shapes.card)
            .padding(16.dp)
    ) {
        // Header
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.weight(1f)) {
                Text(category.name, color = colors.textPrimary, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text("${category.activities.size} activities available", color = colors.mutedText, style = MaterialTheme.typography.bodySmall)
            }
            // Selected count chip
            if (selectedIds.any { id -> category.activities.any { it.id == id } }) {
                val count = selectedIds.count { id -> category.activities.any { it.id == id } }
                Box(
                    modifier = Modifier
                        .background(activitiesGradient, RoundedCornerShape(50))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) { Text("$count", color = Color.White, style = MaterialTheme.typography.labelMedium) }
            }
        }

        Spacer(Modifier.height(12.dp))

        val visibleItems = if (isExpanded) category.activities else category.activities.take(6)

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            userScrollEnabled = false,
        ) {
            items(visibleItems, key = { it.id }) { item: ActivityItem ->
                ActivityTile(activity = item, selected = selectedIds.contains(item.id), onClick = { onToggle(item.id) })
            }
        }

        // Show more
        if (category.activities.size > 6) {
            Spacer(Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Box(
                    modifier = Modifier
                        .background(Color(0x221A1A1F), RoundedCornerShape(50))
                        .padding(horizontal = 14.dp, vertical = 8.dp)
            .clickableNoIndication { onToggleExpand(category.id) }
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(text = if (isExpanded) "Show Less" else "Show More (${category.activities.size - 6})", color = colors.textPrimary)
                        Icon(imageVector = Icons.Default.ExpandMore, contentDescription = null, tint = colors.textPrimary, modifier = Modifier.rotate(if (isExpanded) 180f else 0f))
                    }
                }
            }
        }
    }
}

private fun Modifier.clickableNoIndication(onClick: () -> Unit): Modifier = composed {
    val interaction = remember { MutableInteractionSource() }
    this.clickable(indication = null, interactionSource = interaction, onClick = onClick)
}
