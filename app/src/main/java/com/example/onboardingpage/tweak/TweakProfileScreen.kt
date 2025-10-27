package com.example.onboardingpage.tweak

import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.onboardingpage.GradientPillButton
import com.example.onboardingpage.tweak.components.AddPhotoTile
import com.example.onboardingpage.tweak.components.GlassCard
import com.example.onboardingpage.tweak.components.PercentProgressStrip
import com.example.onboardingpage.tweak.components.RoundedTextField
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TweakProfileScreen(onBack: () -> Unit, vm: TweakProfileViewModel = viewModel()) {
    val context = LocalContext.current

    val gradientBg = Brush.verticalGradient(
        listOf(Color(0xFF0B0D12), Color(0xFF0E0712), Color(0xFF191A24))
    )

    val multiplePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(5)
    ) { uris: List<Uri> ->
        vm.addImages(uris)
    }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bmp ->
        vm.onCameraResult(bmp)
        if (bmp != null) Toast.makeText(context, "Verification added", Toast.LENGTH_SHORT).show()
    }

    var showActivities by remember { mutableStateOf(false) }
    val sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White) }
                },
                title = { Text("Tweak Profile", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.SemiBold) },
                colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = Color.Transparent,
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                GradientPillButton(text = "Save Changes", onClick = {
                    if (vm.validate()) {
                        Toast.makeText(context, "Saved!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Please complete required fields", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBg)
                .padding(inner)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Flex Your Pics
            Box(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.align(Alignment.TopStart)) {
                    SectionHeader("Flex Your Pics", "Min 1, Max 5 vibes.")
                }
                val shown = maxOf(0, vm.images.size)
                CountBadge(text = "$shown/5", modifier = Modifier.align(Alignment.BottomEnd).padding(end = 4.dp))
            }
            GlassCard {
                val canAdd = vm.images.size < 5
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    item(key = "add") {
                        AddPhotoTile(
                            onClick = { multiplePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                            enabled = canAdd,
                        )
                    }
                    items(vm.images, key = { it }) { uri ->
                        com.example.onboardingpage.tweak.components.ImageTile(
                            uri = uri,
                            onRemove = { vm.removeImage(uri) }
                        )
                    }
                }
            }

            // Glow Check
            SectionHeader("Glow Check (Verification)", "Send in your best-lit selfie for quick verification.")
            GlassCard {
                OutlinedButton(onClick = { cameraLauncher.launch(null) }, shape = RoundedCornerShape(16.dp)) {
                    Text("Open Camera")
                }
                Spacer(Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("${(vm.verificationProgress * 100).toInt()}%", color = Color(0xFF8A65FF))
                    Spacer(Modifier.width(8.dp))
                    PercentProgressStrip(progress = vm.verificationProgress, modifier = Modifier.weight(1f))
                }
            }

            // Personal Info
            SectionHeader("Personal Info", "Tell us about yourself")
            GlassCard {
                RoundedTextField(vm.fullName, { vm.fullName = it }, "Enter your full name")
                Spacer(Modifier.height(12.dp))
                RoundedTextField(vm.bio, { vm.bio = it }, "Add your bio", singleLine = false)
                Spacer(Modifier.height(12.dp))
                RoundedTextField(vm.email, { vm.email = it }, "Enter your email")
            }

            // Gender & Birthday row
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    FieldLabel("Gender")
                    GenderDropdownField(current = vm.gender, onSelected = { vm.updateGender(it) })
                }
                Column(modifier = Modifier.weight(1f)) {
                    FieldLabel("Birthday")
                    BirthdayPickerField(currentText = vm.birthday, onSelectedText = { vm.updateBirthday(it) })
                }
            }

            // Activities selector
            SectionHeader("Activities / Hobbies / Interest", "Choose activities you're interested in")
            GlassCard {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0x331A1A1F))
                        .clickable { showActivities = true }
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column { Text("Select Activities", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold); Text("Choose activities you're interested in", color = Color(0xFFB8B7C0)) }
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color(0xFFB8B7C0))
                }
            }

            Spacer(Modifier.height(90.dp))
        }

        if (showActivities) {
            ModalBottomSheet(onDismissRequest = { showActivities = false }, sheetState = sheetState, containerColor = Color(0xFF10131A)) {
                ActivitiesSheet(vm) { showActivities = false }
            }
        }
    }
}

@Composable
private fun ActivitiesSheet(vm: TweakProfileViewModel, onDone: () -> Unit) {
    val all = listOf("Hiking", "Music", "Gaming", "Cooking", "Art", "Movies", "Travel")
    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Select Activities", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        FlowRowWrap(spacing = 8) {
            all.forEach { tag ->
                val selected = vm.selectedActivities.contains(tag)
                val bg = if (selected) Brush.horizontalGradient(listOf(Color(0xFF4C00FF), Color(0xFFFF4D97))) else null
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(18.dp))
                        .background(bg ?: Brush.horizontalGradient(listOf(Color(0x331A1A1F), Color(0x331A1A1F))))
                        .clickable { vm.toggleActivity(tag) }
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) { Text(tag, color = Color.White) }
            }
        }
        Spacer(Modifier.height(12.dp))
        com.example.onboardingpage.GradientPillButton(text = "Done", onClick = { onDone() })
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun SectionHeader(title: String, subtitle: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(title, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
    Text(subtitle, color = Color(0xFFB8B7C0), fontSize = 13.sp)
}

@Composable
private fun CountBadge(text: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                Brush.horizontalGradient(listOf(Color(0xFF4C00FF), Color(0xFFFF4D97)))
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) { Text(text, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold) }
}

@Composable
private fun FieldLabel(text: String) {
    Row { Text(text = text + " *", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold) }
}

@Composable
private fun GenderDropdownField(current: String?, onSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("Male", "Female", "Non-binary", "Prefer not to say")
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0x331A1A1F))
            .clickable { expanded = true }
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Text(current ?: "Gender", color = Color(0xFFB8B7C0))
            Spacer(Modifier.weight(1f))
            Icon(Icons.Outlined.ExpandMore, contentDescription = null, tint = Color(0xFFB8B7C0))
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { opt ->
                DropdownMenuItem(text = { Text(opt) }, onClick = {
                    onSelected(opt)
                    expanded = false
                })
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BirthdayPickerField(currentText: String?, onSelectedText: (String) -> Unit) {
    var show by remember { mutableStateOf(false) }
    val state = rememberDatePickerState()
    val formatter = remember { DateTimeFormatter.ofPattern("MMM d, yyyy") }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0x331A1A1F))
            .clickable { show = true }
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Text(currentText ?: "Select Date of...", color = Color(0xFFB8B7C0))
            Spacer(Modifier.weight(1f))
            Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = Color(0xFFB8B7C0))
        }
    }

    if (show) {
        DatePickerDialog(
            onDismissRequest = { show = false },
            confirmButton = {
                OutlinedButton(onClick = {
                    val millis = state.selectedDateMillis
                    if (millis != null) {
                        val text = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate().format(formatter)
                        onSelectedText(text)
                    }
                    show = false
                }) { Text("OK") }
            },
            dismissButton = {
                OutlinedButton(onClick = { show = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = state)
        }
    }
}

// Simple wrap row for chips
@Composable
private fun FlowRowWrap(spacing: Int = 8, content: @Composable () -> Unit) {
    androidx.compose.foundation.layout.FlowRow(
        horizontalArrangement = Arrangement.spacedBy(spacing.dp),
        verticalArrangement = Arrangement.spacedBy(spacing.dp)
    ) { content() }
}
