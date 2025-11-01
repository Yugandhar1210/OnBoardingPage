package com.example.onboardingpage.activities

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTextStartingWith
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.onboardingpage.MainActivity
import com.example.onboardingpage.activities.theme.ActivitiesTheme
import com.example.onboardingpage.activities.ui.SelectActivitiesScreen
import com.example.onboardingpage.activities.viewmodel.ActivitiesViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SelectActivitiesScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    private fun setContent(vm: ActivitiesViewModel = ActivitiesViewModel(androidx.lifecycle.SavedStateHandle())) {
        composeRule.setContent {
            ActivitiesTheme {
                SelectActivitiesScreen(viewModel = vm, onBack = {}, onDone = {})
            }
        }
    }

    @Test
    fun tileToggle_updatesCount() {
        setContent()
        composeRule.onNodeWithTag("tile_baseball").performClick()
        composeRule.onNode(hasText("1 selected")).assertIsDisplayed()
    }

    @Test
    fun reset_clearsSelections() {
        val vm = ActivitiesViewModel(androidx.lifecycle.SavedStateHandle())
        setContent(vm)
        composeRule.onNodeWithTag("tile_baseball").performClick()
        composeRule.onNodeWithTag("reset_button").performClick()
        composeRule.onNode(hasText("0 selected")).assertIsDisplayed()
    }

    @Test
    fun showMore_expands() {
        setContent()
        // There should be a Show More button in the first category (Trending)
    composeRule.onNode(hasTextStartingWith("Show More")).performClick()
        composeRule.onNodeWithText("Show Less").assertIsDisplayed()
    }
}
