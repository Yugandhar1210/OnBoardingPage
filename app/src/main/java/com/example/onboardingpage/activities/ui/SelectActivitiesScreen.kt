package com.example.onboardingpage.activities.ui

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalAccessibilityManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.SavedStateHandle
import com.example.onboardingpage.activities.data.Category
import com.example.onboardingpage.activities.data.SampleCategories
import com.example.onboardingpage.activities.theme.ActivitiesTheme
import com.example.onboardingpage.activities.theme.LocalActivitiesColors
import com.example.onboardingpage.activities.ui.components.ActivitiesSearchBar
import com.example.onboardingpage.activities.ui.components.ActivitiesTopBar
import com.example.onboardingpage.activities.ui.components.ActivityTile
import com.example.onboardingpage.activities.ui.components.CategoryCard
import com.example.onboardingpage.activities.viewmodel.ActivitiesViewModel

@Composable
fun SelectActivitiesScreen(
    viewModel: ActivitiesViewModel = viewModel(),
    onBack: () -> Unit,
    onDone: (List<String>) -> Unit
) {
    val categories by viewModel.categories.collectAsState()
    val selected by viewModel.selectedIds.collectAsState()
    val query by viewModel.query.collectAsState()
    val expanded by viewModel.expandedCategories.collectAsState()
    SelectActivitiesContent(
        categories = categories,
        selected = selected,
        query = query,
        expanded = expanded,
        onQueryChange = viewModel::setQuery,
        onToggle = viewModel::toggleSelection,
        onReset = viewModel::resetSelections,
        onToggleExpand = viewModel::toggleExpandCategory,
        onBack = onBack,
        onDone = { onDone(selected.toList()) }
    )
}

@Composable
private fun SelectActivitiesContent(
    categories: List<Category>,
    selected: Set<String>,
    query: String,
    expanded: Set<String>,
    onQueryChange: (String) -> Unit,
    onToggle: (String) -> Unit,
    onReset: () -> Unit,
    onToggleExpand: (String) -> Unit,
    onBack: () -> Unit,
    onDone: () -> Unit
) {
    val colors = LocalActivitiesColors.current

    val filtered by remember(categories, query) {
        derivedStateOf {
            if (query.isBlank()) null else {
                val all = categories.flatMap { it.activities }
                val matches = all.filter { it.name.contains(query, ignoreCase = true) }
                Category(id = "results", name = "Results", activities = matches)
            }
        }
    }

    ActivitiesTheme {
        Scaffold(
            containerColor = colors.bgDark1,
            topBar = {
                ActivitiesTopBar(
                    selectedCount = selected.size,
                    onBack = onBack,
                    onDone = onDone
                )
            }
        ) { inner ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colors.bgDark1)
                    .padding(inner)
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(Modifier.height(8.dp))
                ActivitiesSearchBar(query = query, onQueryChange = onQueryChange)

                Spacer(Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Selected pill
                    Box(
                        modifier = Modifier
                            .background(Color(0x331A1A1F), androidx.compose.foundation.shape.RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) { Text("${selected.size}", color = colors.textPrimary) }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(Color(0x221A1A1F), androidx.compose.foundation.shape.RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                            .testTag("reset_button")
                            .clickable { onReset() }
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null, tint = colors.mutedText)
                        Spacer(Modifier.padding(horizontal = 4.dp))
                        Text("Reset", color = colors.textPrimary)
                    }
                }

                Spacer(Modifier.height(12.dp))

                if (filtered != null) {
                    CategoryCard(
                        category = filtered!!,
                        selectedIds = selected,
                        isExpanded = true,
                        onToggle = onToggle,
                        onToggleExpand = { }
                    )
                } else {
                    categories.forEach { cat ->
                        CategoryCard(
                            category = cat,
                            selectedIds = selected,
                            isExpanded = expanded.contains(cat.id),
                            onToggle = onToggle,
                            onToggleExpand = onToggleExpand,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Preview(showBackground = true, name = "Default (no selection)")
@Composable
private fun Preview_SelectActivities_Default() {
    SelectActivitiesContent(
        categories = SampleCategories(),
        selected = emptySet(),
        query = "",
        expanded = emptySet(),
        onQueryChange = {},
        onToggle = {},
        onReset = {},
        onToggleExpand = {},
        onBack = {},
        onDone = {}
    )
}

@Preview(showBackground = true, name = "Partial selection (3)")
@Composable
private fun Preview_SelectActivities_PartialSelection() {
    SelectActivitiesContent(
        categories = SampleCategories(),
        selected = setOf("acting", "basketball", "cricket"),
        query = "",
        expanded = emptySet(),
        onQueryChange = {},
        onToggle = {},
        onReset = {},
        onToggleExpand = {},
        onBack = {},
        onDone = {}
    )
}

@Preview(showBackground = true, name = "Expanded category")
@Composable
private fun Preview_SelectActivities_Expanded() {
    SelectActivitiesContent(
        categories = SampleCategories(),
        selected = emptySet(),
        query = "",
        expanded = setOf("trending"),
        onQueryChange = {},
        onToggle = {},
        onReset = {},
        onToggleExpand = {},
        onBack = {},
        onDone = {}
    )
}

@Preview(showBackground = true, name = "Search results")
@Composable
private fun Preview_SelectActivities_Search() {
    SelectActivitiesContent(
        categories = SampleCategories(),
        selected = emptySet(),
        query = "ball",
        expanded = emptySet(),
        onQueryChange = {},
        onToggle = {},
        onReset = {},
        onToggleExpand = {},
        onBack = {},
        onDone = {}
    )
}

// ViewModel-based previews removed to avoid Preview runtime issues; using stateless content previews above.
