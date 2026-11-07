package com.wordboard.app.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wordboard.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionsSetupScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("App Setup & Permissions", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundDark)
            )
        },
        containerColor = BackgroundDark
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Google Play Prominent Disclosure Card
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Security, contentDescription = null, tint = AccentEmerald)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Play Store Privacy Guarantee", color = AccentEmerald, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "WordBoard uses Android's Accessibility API strictly to detect your custom shortcut triggers (like /addr or omw) and display the 1-tap expansion pop-up above your keyboard.\n\n" +
                                "✓ No keystrokes are stored\n" +
                                "✓ No personal data or passwords collected\n" +
                                "✓ 100% on-device local processing with zero internet tracking",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                }
            }

            // Step 1: Accessibility Service
            PermissionStepItem(
                step = "1",
                title = "Enable Accessibility Service",
                desc = "Allows WordBoard to detect shortcuts inside WhatsApp, Gmail, Chrome, and any keyboard.",
                buttonText = "Open Accessibility Settings",
                onClick = {
                    val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                    context.startActivity(intent)
                }
            )

            // Step 2: Overlay Pop-up Permission
            PermissionStepItem(
                step = "2",
                title = "Enable Keyboard Pop-up Overlay",
                desc = "Required to show the floating 1-tap expansion pill right above your existing keyboard.",
                buttonText = "Grant Overlay Permission",
                onClick = {
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:${context.packageName}")
                    )
                    context.startActivity(intent)
                }
            )

            // Step 3: Optional IME Keyboard
            PermissionStepItem(
                step = "3",
                title = "Enable WordBoard Keyboard (Optional)",
                desc = "Add WordBoard as an active keyboard if you want direct phrase board keys.",
                buttonText = "Open Keyboard Settings",
                onClick = {
                    val intent = Intent(Settings.ACTION_INPUT_METHOD_SETTINGS)
                    context.startActivity(intent)
                }
            )
        }
    }
}

@Composable
fun PermissionStepItem(
    step: String,
    title: String,
    desc: String,
    buttonText: String,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(PrimaryIndigo, shape = RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(step, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(title, color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(desc, color = TextSecondary, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigoDark),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(buttonText, fontSize = 12.sp)
            }
        }
    }
}
