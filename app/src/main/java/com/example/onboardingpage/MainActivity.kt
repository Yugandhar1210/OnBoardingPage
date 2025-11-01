package com.example.onboardingpage

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.ExperimentalGraphicsApi
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.clickable
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import com.example.onboardingpage.ui.theme.OnBoardingPageTheme
import com.example.onboardingpage.tweak.TweakProfileScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.onboardingpage.activities.ui.SelectActivitiesScreen
import com.example.onboardingpage.tweak.TweakProfileViewModel

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OnBoardingPageTheme {
                val navController = rememberNavController()
                AppNavHost(navController)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreen(onContinue = { navController.navigate("login") })
        }
        composable("login") {
            com.example.onboardingpage.login.LoginScreen(
                onBack = { navController.popBackStack() },
                onContinue = { phone -> navController.navigate("otp/$phone") }
            )
        }
        composable(
            route = "otp/{phone}",
            arguments = listOf(navArgument("phone") { type = NavType.StringType })
        ) { backStackEntry ->
            val phone = backStackEntry.arguments?.getString("phone") ?: ""
            com.example.onboardingpage.login.OtpScreen(
                phone = phone,
                onBack = { navController.popBackStack() },
                onVerified = {
                    navController.navigate("tweak_profile") {
                        launchSingleTop = true
                        popUpTo("main") { inclusive = false }
                    }
                }
            )
        }
        composable("tweak_profile") { backStackEntry ->
            val vm: TweakProfileViewModel = viewModel()

            // Listen for results from the SelectActivitiesScreen
            val selectedResultFlow = backStackEntry.savedStateHandle.getStateFlow<List<String>?>("selectedActivities", null)
            val selectedResult = selectedResultFlow.collectAsStateWithLifecycle(initialValue = null).value
            if (selectedResult != null) {
                vm.setActivities(selectedResult.toSet())
                backStackEntry.savedStateHandle["selectedActivities"] = null
            }

            TweakProfileScreen(
                onBack = { navController.popBackStack() },
                onOpenActivities = { navController.navigate("select_activities") },
                vm = vm
            )
        }

        composable("select_activities") {
            SelectActivitiesScreen(
                onBack = { navController.popBackStack() },
                onDone = { ids ->
                    navController.previousBackStackEntry?.savedStateHandle?.set("selectedActivities", ids)
                    navController.popBackStack()
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    OnBoardingPageTheme {
        MainScreen({})
    }
}

@Composable
private fun MainScreen(onContinue: () -> Unit) {
    // Simple landing screen with a big CTA that navigates to tweak profile
    val gradient = Brush.verticalGradient(
        listOf(Color(0xFF0B0D12), Color(0xFF0E0712), Color(0xFF191A24))
    )
    Scaffold(
        topBar = {
            Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Text(
                    text = "Main",
                    style = MaterialTheme.typography.titleLarge.copy(color = Color.White)
                )
            }
        },
        containerColor = Color.Transparent
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(inner)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Welcome",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            )
            GradientPillButton(text = "Continue", onClick = onContinue)
        }
    }
}

@Composable
fun GradientPillButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = listOf(Color(0xFF2E3BFF), Color(0xFFFF3FD8))

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(
                brush = Brush.horizontalGradient(colors),
                shape = RoundedCornerShape(34.dp)
            )
            .clip(RoundedCornerShape(34.dp))
            .clickable { onClick() }
            .padding(vertical = 18.dp)
    ) {
        Text(text = text, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
    }
}