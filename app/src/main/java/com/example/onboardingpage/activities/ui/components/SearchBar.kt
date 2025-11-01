package com.example.onboardingpage.activities.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.onboardingpage.activities.theme.LocalActivitiesColors
import com.example.onboardingpage.activities.theme.LocalActivitiesShapes

@Composable
fun ActivitiesSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalActivitiesColors.current

    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .testTag("search_bar")
            .fillMaxWidth()
            .background(Color(0x221A1A1F), LocalActivitiesShapes.current.card),
        placeholder = {
            Text(
                text = "Search by Activities / Hobbies / Interest",
                color = colors.mutedText,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = colors.mutedText) },
        singleLine = true,
        shape = RoundedCornerShape(24.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0x221A1A1F),
            unfocusedContainerColor = Color(0x221A1A1F),
            focusedTextColor = colors.textPrimary,
            unfocusedTextColor = colors.textPrimary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = colors.primaryLeft
        )
    )
}
