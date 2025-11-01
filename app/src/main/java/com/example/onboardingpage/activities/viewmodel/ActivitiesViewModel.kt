package com.example.onboardingpage.activities.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onboardingpage.activities.data.Category
import com.example.onboardingpage.activities.data.SampleCategories
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ActivitiesViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Config
    val maxSelection: Int = 50

    // Backing state
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _selectedIds = MutableStateFlow(savedStateHandle.get<Set<String>>(KEY_SELECTED) ?: emptySet())
    val selectedIds: StateFlow<Set<String>> = _selectedIds.asStateFlow()

    private val _expandedCategories = MutableStateFlow(setOf<String>())
    val expandedCategories: StateFlow<Set<String>> = _expandedCategories.asStateFlow()

    // Derived
    val selectedCount: StateFlow<Int> = _selectedIds
        .map { it.size }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    init {
        // Load sample data
        _categories.value = SampleCategories()
        // Persist selections on change
        viewModelScope.launch {
            _selectedIds.collect { savedStateHandle[KEY_SELECTED] = it }
        }
    }

    fun toggleSelection(id: String) {
        val current = _selectedIds.value
        _selectedIds.value = if (current.contains(id)) {
            current - id
        } else {
            if (current.size >= maxSelection) current else current + id
        }
    }

    fun setQuery(q: String) { _query.value = q }

    fun resetSelections() { _selectedIds.value = emptySet() }

    fun toggleExpandCategory(catId: String) {
        val current = _expandedCategories.value
        _expandedCategories.value = if (current.contains(catId)) current - catId else current + catId
    }

    companion object {
        private const val KEY_SELECTED = "activities_selected_ids"
    }
}
