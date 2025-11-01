package com.example.onboardingpage.activities

import androidx.lifecycle.SavedStateHandle
import com.example.onboardingpage.activities.viewmodel.ActivitiesViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ActivitiesViewModelTest {

    @Test
    fun toggleSelection_addsAndRemoves() = runTest {
        val vm = ActivitiesViewModel(SavedStateHandle())
        vm.toggleSelection("baseball")
        assertEquals(setOf("baseball"), vm.selectedIds.value)
        vm.toggleSelection("baseball")
        assertEquals(emptySet<String>(), vm.selectedIds.value)
    }

    @Test
    fun maxSelection_isRespected() = runTest {
        val vm = ActivitiesViewModel(SavedStateHandle())
        // Add beyond the max (50)
        (0 until vm.maxSelection + 5).forEach { i -> vm.toggleSelection("id_${'$'}i") }
        assertEquals(vm.maxSelection, vm.selectedIds.value.size)
    }
}
