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
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoCamera
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import com.example.onboardingpage.GradientPillButton
import com.example.onboardingpage.tweak.components.AddPhotoTile
import com.example.onboardingpage.tweak.components.GlassCard
import com.example.onboardingpage.tweak.components.PercentProgressStrip
import com.example.onboardingpage.ui.theme.OnBoardingPageTheme
import androidx.compose.material3.TextButton
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
                GradientOutlineButton(
                    text = "Open Camera",
                    leading = Icons.Filled.PhotoCamera,
                    onClick = { cameraLauncher.launch(null) }
                )
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
                val placeholderColor = Color(0xFFB8B7C0)
                val iconGradient = Brush.horizontalGradient(listOf(Color(0xFF4C00FF), Color(0xFFFF4D97)))

                // --- Full Name ---
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        GradientIcon(
                            icon = Icons.Filled.Person,
                            brush = iconGradient,
                            size = 20.dp
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = "Full Name",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(Modifier.height(6.dp))
                    OutlinedTextField(
                        value = vm.fullName,
                        onValueChange = { vm.fullName = it },
                        placeholder = {
                            Text(
                                "Enter your full name",
                                color = placeholderColor,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        },
                        singleLine = true,
                        textStyle = TextStyle(color = Color.White, fontSize = 14.sp),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        isError = vm.showValidationErrors && vm.fullName.isBlank(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0x338A65FF),
                            unfocusedBorderColor = Color(0x22545460),
                            cursorColor = Color(0xFF8A65FF),
                            focusedContainerColor = Color(0x551A1A1F),
                            unfocusedContainerColor = Color(0x401A1A1F),
                            focusedPlaceholderColor = placeholderColor,
                            unfocusedPlaceholderColor = placeholderColor
                        )
                    )
                }

                Spacer(Modifier.height(12.dp))

                // --- Bio ---
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        GradientIcon(
                            icon = Icons.Filled.Edit,
                            brush = iconGradient,
                            size = 20.dp
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = "Bio",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(Modifier.height(6.dp))
                    OutlinedTextField(
                        value = vm.bio,
                        onValueChange = { vm.bio = it },
                        placeholder = {
                            Text(
                                "Add your bio",
                                color = placeholderColor,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        },
                        singleLine = false,
                        minLines = 3,
                        textStyle = TextStyle(color = Color.White, fontSize = 14.sp),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        isError = vm.showValidationErrors && vm.bio.isBlank(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0x338A65FF),
                            unfocusedBorderColor = Color(0x22545460),
                            cursorColor = Color(0xFF8A65FF),
                            focusedContainerColor = Color(0x551A1A1F),
                            unfocusedContainerColor = Color(0x401A1A1F),
                            focusedPlaceholderColor = placeholderColor,
                            unfocusedPlaceholderColor = placeholderColor
                        )
                    )
                }

                Spacer(Modifier.height(12.dp))

                // --- Email ---
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        GradientIcon(
                            icon = Icons.Filled.Email,
                            brush = iconGradient,
                            size = 20.dp
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = "Email",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(Modifier.height(6.dp))
                    OutlinedTextField(
                        value = vm.email,
                        onValueChange = { vm.email = it },
                        placeholder = {
                            Text(
                                "Enter your email",
                                color = placeholderColor,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        },
                        singleLine = true,
                        textStyle = TextStyle(color = Color.White, fontSize = 14.sp),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        isError = vm.showValidationErrors && vm.email.isBlank(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0x338A65FF),
                            unfocusedBorderColor = Color(0x22545460),
                            cursorColor = Color(0xFF8A65FF),
                            focusedContainerColor = Color(0x551A1A1F),
                            unfocusedContainerColor = Color(0x401A1A1F),
                            focusedPlaceholderColor = placeholderColor,
                            unfocusedPlaceholderColor = placeholderColor
                        )
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

// Gender & Birthday row
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
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
                    Column {
                        Text(
                            "Select Activities",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            "Choose activities you're interested in",
                            fontSize = 12.sp,
                            color = Color(0xFFB8B7C0)
                        )
                    }

                    // Transparent icon with gradient circular ring
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .background(
                                brush = Brush.sweepGradient(
                                    listOf(
                                        Color(0xFF4C00FF),
                                        Color(0xFFFF4D97),
                                        Color(0xFF4C00FF)
                                    )
                                ),
                                shape = CircleShape
                            )
                            .padding(2.dp) // thickness of the gradient ring
                            .background(Color.Transparent, shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ChevronRight,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.8f), // Slightly visible or transparent effect
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }


            Spacer(Modifier.height(90.dp))
        }

        if (showActivities) {
            LaunchedEffect(Unit) {
                sheetState.show()
            }
            ModalBottomSheet(
                onDismissRequest = {
                    scope.launch {
                        sheetState.hide()
                    }.invokeOnCompletion { showActivities = false }
                },
                sheetState = sheetState,
                containerColor = Color(0xFF10131A)
            ) {
                ActivitiesSheet(vm) {
                    scope.launch {
                        sheetState.hide()
                    }.invokeOnCompletion { showActivities = false }
                }
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
            GradientIcon(Icons.Outlined.ExpandMore, brush = Brush.horizontalGradient(listOf(Color(0xFF4C00FF), Color(0xFFFF4D97))))
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
            val gradientText = Brush.horizontalGradient(listOf(Color(0xFF4C00FF), Color(0xFFFF4D97)))
            if (currentText.isNullOrBlank()) {
                Text("Select Date of...", color = Color(0xFFB8B7C0))
            } else {
                val annotated = buildAnnotatedString {
                    withStyle(SpanStyle(brush = gradientText, fontWeight = FontWeight.Medium)) {
                        append(currentText)
                    }
                }
                Text(annotated, color = Color.Unspecified)
            }
            Spacer(Modifier.weight(1f))
            GradientIcon(Icons.Default.CalendarMonth, brush = Brush.horizontalGradient(listOf(Color(0xFF4C00FF), Color(0xFFFF4D97))))
        }
    }

    if (show) {
        val ctaGradient = Brush.horizontalGradient(listOf(Color(0xFF4C00FF), Color(0xFFFF4D97)))
        DatePickerDialog(
            onDismissRequest = { show = false },
            confirmButton = {
                TextButton(onClick = {
                    val millis = state.selectedDateMillis
                    if (millis != null) {
                        val text = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate().format(formatter)
                        onSelectedText(text)
                    }
                    show = false
                }) {
                    val ok = buildAnnotatedString {
                        withStyle(SpanStyle(brush = ctaGradient, fontWeight = FontWeight.SemiBold)) { append("OK") }
                    }
                    Text(ok, color = Color.Unspecified, fontSize = 14.sp)
                }
            },
            dismissButton = {
                TextButton(onClick = { show = false }) {
                    val cancel = buildAnnotatedString {
                        withStyle(SpanStyle(brush = ctaGradient, fontWeight = FontWeight.SemiBold)) { append("Cancel") }
                    }
                    Text(cancel, color = Color.Unspecified, fontSize = 14.sp)
                }
            }
        ) {
            DatePicker(state = state)
        }
    }
}

@Composable
private fun GradientIcon(
    icon: ImageVector,
    brush: Brush,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    size: Dp = 24.dp,
    showBackground: Boolean = false,
    backgroundBrush: Brush = brush,
    backgroundPadding: Dp = 2.dp,
    iconVisible: Boolean = true
) {
    val container = modifier
        .size(size)
        .let { base ->
            if (showBackground) base
                .clip(CircleShape)
                .background(backgroundBrush)
            else base
        }

    Box(modifier = container, contentAlignment = Alignment.Center) {
        if (iconVisible) {
            val innerSize = if (showBackground && size > backgroundPadding * 2) size - (backgroundPadding * 2) else size
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                modifier = Modifier
                    .size(innerSize)
                    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
                    .drawWithCache {
                        onDrawWithContent {
                            // Draw the icon first, then apply gradient masked to its alpha
                            drawContent()
                            drawRect(brush = brush, blendMode = BlendMode.SrcIn)
                        }
                    },
                tint = Color.White
            )
        }
        // else: icon transparent/invisible; only background circle is shown
    }
}
@Composable
private fun GradientOutlineButton(text: String, leading: ImageVector? = null, onClick: () -> Unit) {
    val shape = RoundedCornerShape(16.dp)
    val brush = Brush.horizontalGradient(listOf(Color(0xFF4C00FF), Color(0xFFFF4D97)))
    Box(
        modifier = Modifier
            .clip(shape)
            .border(2.dp, brush, shape)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            if (leading != null) {
                GradientIcon(leading, brush = brush)
            }
            Text(text, color = Color.White, fontWeight = FontWeight.SemiBold)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, name = "Open Camera Button")
@Composable
private fun OpenCameraButtonPreview() {
    OnBoardingPageTheme {
        GlassCard {
            GradientOutlineButton(text = "Open Camera", leading = Icons.Filled.PhotoCamera, onClick = {})
        }
    }
}

@Preview(showBackground = true, name = "Activities Row")
@Composable
private fun ActivitiesRowPreview() {
    OnBoardingPageTheme {
        GlassCard {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x331A1A1F))
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Select Activities", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    Text("Choose activities you're interested in", fontSize = 12.sp, color = Color(0xFFB8B7C0))
                }
                GradientIcon(
                    icon = Icons.Filled.ChevronRight,
                    brush = Brush.horizontalGradient(listOf(Color(0xFF4C00FF), Color(0xFFFF4D97))),
                    size = 32.dp
                )
            }
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

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
private fun TweakProfileScreenPreview() {
    OnBoardingPageTheme {
        // Use a local instance of the ViewModel for preview purposes
        val vm = remember { TweakProfileViewModel() }
        TweakProfileScreen(onBack = {}, vm = vm)
    }
}

//@RequiresApi(Build.VERSION_CODES.O)
//@Preview(showBackground = true, name = "TweakProfile - Empty")
//@Composable
//private fun TweakProfileScreenPreviewEmpty() {
//    OnBoardingPageTheme {
//        val vm = remember { TweakProfileViewModel() }
//        // leave defaults: 0 images, not verified
//        TweakProfileScreen(onBack = {}, vm = vm)
//    }
//}
//
//@RequiresApi(Build.VERSION_CODES.O)
//@Preview(showBackground = true, name = "TweakProfile - 3 Photos")
//@Composable
//private fun TweakProfileScreenPreviewThree() {
//    OnBoardingPageTheme {
//        val vm = remember { TweakProfileViewModel() }
//        val sample = listOf(
//            Uri.parse("https://picsum.photos/seed/1/400"),
//            Uri.parse("https://picsum.photos/seed/2/400"),
//            Uri.parse("https://picsum.photos/seed/3/400")
//        )
//        vm.addImages(sample)
//        vm.fullName = "Alex Johnson"
//        vm.bio = "Explorer. Music lover. Weekend hiker."
//        vm.email = "alex@example.com"
//        vm.updateGender("Male")
//        vm.updateBirthday("Aug 21, 1997")
//        vm.toggleActivity("Music")
//        vm.toggleActivity("Hiking")
//        TweakProfileScreen(onBack = {}, vm = vm)
//    }
//}
//
//@RequiresApi(Build.VERSION_CODES.O)
//@Preview(showBackground = true, name = "TweakProfile - Full Verified (5)")
//@Composable
//private fun TweakProfileScreenPreviewFull() {
//    OnBoardingPageTheme {
//        val vm = remember { TweakProfileViewModel() }
//        val sample = listOf(
//            Uri.parse("https://picsum.photos/seed/11/400"),
//            Uri.parse("https://picsum.photos/seed/12/400"),
//            Uri.parse("https://picsum.photos/seed/13/400"),
//            Uri.parse("https://picsum.photos/seed/14/400"),
//            Uri.parse("https://picsum.photos/seed/15/400")
//        )
//        vm.addImages(sample)
//        vm.fullName = "Jamie Rivera"
//        vm.bio = "Designer • Runner • Reader"
//        vm.email = "jamie@rivera.dev"
//        vm.updateGender("Non-binary")
//        vm.updateBirthday("Jan 5, 1994")
//        vm.toggleActivity("Art")
//        vm.toggleActivity("Travel")
//        vm.markVerified()
//        TweakProfileScreen(onBack = {}, vm = vm)
//    }
//}

