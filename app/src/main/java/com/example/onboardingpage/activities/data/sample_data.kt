package com.example.onboardingpage.activities.data

import android.content.res.Resources
import androidx.annotation.DrawableRes

// Data models

data class ActivityItem(
    val id: String,
    val name: String,
    @DrawableRes val iconRes: Int,
    val categoryId: String,
)

data class Category(
    val id: String,
    val name: String,
    val activities: List<ActivityItem>,
)

// Very small built-in sample using framework icons so no extra drawables required
// You can replace with your vector assets later.

@Suppress("FunctionName")
fun SampleCategories(): List<Category> {
    val trending = Category(
        id = "trending",
        name = "Trending",
        activities = listOf(
            ActivityItem("acting", "Acting", android.R.drawable.ic_menu_myplaces, "trending"),
            ActivityItem("talks", "AddaSessions", android.R.drawable.ic_dialog_email, "trending"),
            ActivityItem("home", "Adhibaas", android.R.drawable.ic_menu_mylocation, "trending"),
            ActivityItem("mail", "Amantron", android.R.drawable.ic_dialog_email, "trending"),
            ActivityItem("mic", "AnkitaInConcert", android.R.drawable.ic_btn_speak_now, "trending"),
            ActivityItem("dance", "AratiNritya", android.R.drawable.ic_media_next, "trending"),
            ActivityItem("art", "ArtExhibition", android.R.drawable.ic_menu_gallery, "trending"),
            ActivityItem("lights", "AshtamiSandhya", android.R.drawable.ic_dialog_info, "trending"),
        )
    )

    val sports = Category(
        id = "team_sports",
        name = "Ball & Team Sports",
        activities = listOf(
            ActivityItem("baseball", "Baseball", android.R.drawable.ic_media_play, "team_sports"),
            ActivityItem("basketball", "Basketball", android.R.drawable.ic_media_play, "team_sports"),
            ActivityItem("cricket", "Cricket", android.R.drawable.ic_media_play, "team_sports"),
            ActivityItem("football", "Football", android.R.drawable.ic_media_play, "team_sports"),
            ActivityItem("handball", "Handball", android.R.drawable.ic_media_play, "team_sports"),
            ActivityItem("hockey", "Hockey", android.R.drawable.ic_media_play, "team_sports"),
            ActivityItem("rugby", "Rugby", android.R.drawable.ic_media_play, "team_sports"),
            ActivityItem("volleyball", "Volleyball", android.R.drawable.ic_media_play, "team_sports"),
            ActivityItem("tennis", "Tennis", android.R.drawable.ic_media_play, "team_sports"),
        )
    )

    return listOf(trending, sports)
}
