package com.example.onboardingpage.activities.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.onboardingpage.activities.theme.LocalActivitiesColors
import com.example.onboardingpage.activities.theme.activitiesGradient

@Composable
fun ActivitiesTopBar(
    selectedCount: Int,
    onBack: () -> Unit,
    onDone: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalActivitiesColors.current

    Row(
        modifier = modifier
            .testTag("top_bar")
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Back glass button
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(Color(0x221A1A1F), CircleShape)
                .clickable { onBack() },
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = colors.textPrimary)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Activities", color = colors.textPrimary, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .background(Color(0x331A1A1F), RoundedCornerShape(50))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) { Text("$selectedCount selected", color = colors.mutedText, style = MaterialTheme.typography.labelMedium) }
        }

        // Done pill
        val doneBase = if (selectedCount > 0) {
            Modifier.background(activitiesGradient, RoundedCornerShape(50))
        } else {
            Modifier.background(Color(0x331A1A1F), RoundedCornerShape(50))
        }
        Box(
            modifier = doneBase
                .padding(horizontal = 14.dp, vertical = 8.dp)
                .let { base -> if (selectedCount > 0) base.clickable { onDone() } else base },
            contentAlignment = Alignment.Center
        ) {
            Text("Done", color = if (selectedCount > 0) Color.White else colors.mutedText, fontWeight = FontWeight.SemiBold)
        }
    }
}
