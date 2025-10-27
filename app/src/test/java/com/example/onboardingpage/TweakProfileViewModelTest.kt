package com.example.onboardingpage

import com.example.onboardingpage.tweak.TweakProfileViewModel
import org.junit.Assert.assertFalse
import org.junit.Test

class TweakProfileViewModelTest {
    @Test
    fun validate_is_false_initially() {
        val vm = TweakProfileViewModel()
        assertFalse(vm.validate())
    }
}
