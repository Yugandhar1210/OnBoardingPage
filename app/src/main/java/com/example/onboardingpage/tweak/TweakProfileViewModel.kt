package com.example.onboardingpage.tweak

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class TweakProfileViewModel : ViewModel() {

    var images by mutableStateOf<List<Uri>>(emptyList())
        private set

    var verificationProgress by mutableStateOf(0f)
        private set

    var fullName by mutableStateOf("")
    var bio by mutableStateOf("")
    var email by mutableStateOf("")
    var gender by mutableStateOf<String?>(null)
    var birthday by mutableStateOf<String?>(null)
    var selectedActivities by mutableStateOf<Set<String>>(emptySet())

    var showValidationErrors by mutableStateOf(false)
        private set

    fun addImages(list: List<Uri>) {
        // Prepend new images so the newly added ones appear immediately after the Add Photo tile
        images = (list + images).distinct().take(5)
    }

    fun removeImage(uri: Uri) {
        images = images.filterNot { it == uri }
    }

    fun onCameraResult(bitmap: Bitmap?) {
        if (bitmap != null) {
            verificationProgress = 1f
        }
    }

    fun updateGender(value: String) { gender = value }
    fun updateBirthday(text: String) { birthday = text }
    fun markVerified() { verificationProgress = 1f }

    fun toggleActivity(tag: String) {
        selectedActivities = if (selectedActivities.contains(tag)) selectedActivities - tag else selectedActivities + tag
    }

    fun setActivities(newSet: Set<String>) {
        selectedActivities = newSet
    }

    fun validate(): Boolean {
        val ok = fullName.isNotBlank() && bio.isNotBlank() && email.contains("@") && gender != null && birthday != null && images.isNotEmpty() && verificationProgress >= 1f && selectedActivities.isNotEmpty()
        showValidationErrors = !ok
        return ok
    }
}
