package com.example.ui

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.data.DiaryEntry
import com.example.data.ForumPost
import com.example.data.SavedResource
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

// Brand Colors from Design Token - Geometric Balance
val BrandPrimary = Color(0xFF6750A4)
val BrandSecondary = Color(0xFF21005D)
val BrandSecondaryContainer = Color(0xFFEADDFF)
val BrandBackground = Color(0xFFFEF7FF)
val BrandSurfaceDim = Color(0xFFE7E0EC)
val BrandSurfaceLowest = Color(0xFFFFFFFF)
val BrandSurfaceLow = Color(0xFFF7F2FA)
val BrandSurfaceContainer = Color(0xFFF3EDF7)
val BrandSurfaceHigh = Color(0xFFE8DEF8)
val BrandOnSurface = Color(0xFF1D1B20)
val BrandOnSurfaceVariant = Color(0xFF49454F)
val BrandOutline = Color(0xFFCAC4D0)
val BrandOutlineVariant = Color(0xFFE7E0EC)
val BrandTertiary = Color(0xFFB3261E) // Bright red for emergency / EXIT
val BrandTertiaryFixed = Color(0xFFF9DEDC) // Light coral red container

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CyberHelpMainScreen(viewModel: CyberHelpViewModel) {
    val context = LocalContext.current
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()

    // Interactive Dialog / BottomSheet states
    var showQuickExitDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = BrandBackground,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BrandSurfaceLowest,
                    titleContentColor = BrandPrimary
                ),
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = "Logo Security",
                            tint = BrandPrimary,
                            modifier = Modifier.size(28.dp)
                        )
                        Text(
                            text = "CyberHelp Ticino",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrandPrimary
                        )
                    }
                },
                actions = {
                    Button(
                        onClick = { showQuickExitDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = BrandTertiary),
                        shape = RoundedCornerShape(20.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Quick Exit",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "EXIT",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = BrandSurfaceContainer,
                tonalElevation = 4.dp
            ) {
                NavigationBarItem(
                    selected = currentTab == AppTab.HOME,
                    onClick = { viewModel.selectTab(AppTab.HOME) },
                    icon = { Icon(Icons.Default.Dashboard, contentDescription = "Home") },
                    label = { Text("Home", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BrandSecondary,
                        selectedTextColor = BrandSecondary,
                        unselectedIconColor = BrandOnSurfaceVariant,
                        unselectedTextColor = BrandOnSurfaceVariant,
                        indicatorColor = BrandSurfaceHigh
                    )
                )
                NavigationBarItem(
                    selected = currentTab == AppTab.SUPPORT,
                    onClick = { viewModel.selectTab(AppTab.SUPPORT) },
                    icon = { Icon(Icons.Default.MedicalServices, contentDescription = "Support") },
                    label = { Text("Supporto", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BrandSecondary,
                        selectedTextColor = BrandSecondary,
                        unselectedIconColor = BrandOnSurfaceVariant,
                        unselectedTextColor = BrandOnSurfaceVariant,
                        indicatorColor = BrandSurfaceHigh
                    )
                )
                NavigationBarItem(
                    selected = currentTab == AppTab.LEARN,
                    onClick = { viewModel.selectTab(AppTab.LEARN) },
                    icon = { Icon(Icons.Default.LibraryBooks, contentDescription = "Learn") },
                    label = { Text("Impara", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BrandSecondary,
                        selectedTextColor = BrandSecondary,
                        unselectedIconColor = BrandOnSurfaceVariant,
                        unselectedTextColor = BrandOnSurfaceVariant,
                        indicatorColor = BrandSurfaceHigh
                    )
                )
                NavigationBarItem(
                    selected = currentTab == AppTab.CIRCLE,
                    onClick = { viewModel.selectTab(AppTab.CIRCLE) },
                    icon = { Icon(Icons.Default.Groups, contentDescription = "Circle") },
                    label = { Text("Circle", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BrandSecondary,
                        selectedTextColor = BrandSecondary,
                        unselectedIconColor = BrandOnSurfaceVariant,
                        unselectedTextColor = BrandOnSurfaceVariant,
                        indicatorColor = BrandSurfaceHigh
                    )
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = currentTab,
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                },
                label = "TabTransition"
            ) { tab ->
                when (tab) {
                    AppTab.HOME -> HomeTabScreen(viewModel)
                    AppTab.SUPPORT -> SupportTabScreen(viewModel)
                    AppTab.LEARN -> LearnTabScreen(viewModel)
                    AppTab.CIRCLE -> CircleTabScreen(viewModel)
                }
            }
        }
    }

    // Quick Exit Dialog
    if (showQuickExitDialog) {
        AlertDialog(
            onDismissRequest = { showQuickExitDialog = false },
            title = {
                Text(
                    text = "Quick Exit (Uscita Rapida)",
                    fontWeight = FontWeight.Bold,
                    color = BrandTertiary
                )
            },
            text = {
                Text(
                    text = "Questa opzione chiuderà l'applicazione e aprirà immediatamente Google per tutelare la tua privacy o sicurezza. Vuoi procedere?",
                    color = BrandOnSurfaceVariant
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showQuickExitDialog = false
                        // Redirect to browser and close application
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"))
                        context.startActivity(intent)
                        (context as? ComponentActivity)?.finishAndRemoveTask()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandTertiary)
                ) {
                    Text("Esci Ora", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showQuickExitDialog = false }) {
                    Text("Annulla", color = BrandPrimary)
                }
            }
        )
    }
}

// ==========================================
// 1. HOME TAB SCREEN
// ==========================================
@Composable
fun HomeTabScreen(viewModel: CyberHelpViewModel) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Safe Badglet
        Row(
            modifier = Modifier
                .background(BrandSecondaryContainer, RoundedCornerShape(16.dp))
                .border(1.dp, BrandOutline, RoundedCornerShape(16.dp))
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = Icons.Default.WavingHand,
                contentDescription = "Hand",
                tint = BrandSecondary,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = "Ciao, sei al sicuro qui",
                fontSize = 12.sp,
                color = BrandSecondary,
                fontWeight = FontWeight.Bold
            )
        }

        // Hero Statement
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "L'aiuto è qui.\nNon sei solo.",
                fontSize = 32.sp,
                lineHeight = 38.sp,
                fontWeight = FontWeight.ExtraBold,
                color = BrandPrimary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Un rifugio digitale per la comunità ticinese. Risorse, supporto e una community pronta ad ascoltarti per affrontare il cyberbullismo.",
                fontSize = 16.sp,
                lineHeight = 22.sp,
                color = BrandOnSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }

        // Bento Cards
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Find Help Now Card - Geometric Balance Style
            Card(
                onClick = { viewModel.selectTab(AppTab.SUPPORT) },
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = BrandSecondaryContainer),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = BorderStroke(1.dp, BrandOutline)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    // Decorative Overlapping Circular Background (Geometric Balance Theme)
                    Box(
                        modifier = Modifier
                            .size(150.dp)
                            .align(Alignment.BottomEnd)
                            .offset(x = 30.dp, y = 30.dp)
                            .background(BrandPrimary.copy(alpha = 0.12f), CircleShape)
                    )
                    // Decorative Rotated Outline Square (Geometric Balance Theme)
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.TopEnd)
                            .offset(x = 10.dp, y = (-5).dp)
                            .border(4.dp, BrandPrimary.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                    )

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .background(BrandSecondary.copy(alpha = 0.1f), CircleShape)
                                .padding(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ChatBubble,
                                contentDescription = null,
                                tint = BrandSecondary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Text(
                            text = "Trova Aiuto Ora",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrandSecondary
                        )
                        Text(
                            text = "Parla con un esperto, in modo anonimo e sicuro. Siamo qui per ascoltarti.",
                            fontSize = 14.sp,
                            color = BrandSecondary.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Inizia chat",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = BrandSecondary
                              )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = null,
                                tint = BrandSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            // Learning Center Card
            Card(
                onClick = { viewModel.selectTab(AppTab.LEARN) },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BrandSurfaceLow),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                border = BorderStroke(1.dp, BrandOutline)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(BrandPrimary.copy(alpha = 0.1f), CircleShape)
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LibraryBooks,
                            contentDescription = null,
                            tint = BrandPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Text(
                        text = "Centro Apprendimento",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandOnSurface
                    )
                    Text(
                        text = "Guide pratiche, articoli e risorse per riconoscere e prevenire il cyberbullismo.",
                        fontSize = 14.sp,
                        color = BrandOnSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Esplora risorse",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrandPrimary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null,
                            tint = BrandPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // Join Community Card
            Card(
                onClick = { viewModel.selectTab(AppTab.CIRCLE) },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BrandSurfaceLow),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                border = BorderStroke(1.dp, BrandOutline)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(BrandPrimary.copy(alpha = 0.1f), CircleShape)
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Groups,
                            contentDescription = null,
                            tint = BrandPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Text(
                        text = "Unisciti alla Community",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandOnSurface
                    )
                    Text(
                        text = "Connettiti con altri giovani. Condividi esperienze in uno spazio sicuro e moderato.",
                        fontSize = 14.sp,
                        color = BrandOnSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Accedi al Circle",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrandPrimary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null,
                            tint = BrandPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        Divider(color = BrandSurfaceContainer, thickness = 1.dp)

        // Partners Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "CON IL SUPPORTO DEI NOSTRI PARTNER SUL TERRITORIO",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = BrandOutline,
                letterSpacing = 1.sp,
                textAlign = TextAlign.Center
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.clickable {
                        Toast.makeText(context, "Pro Juventute Ticino - Servizio 147", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Icon(Icons.Default.Favorite, contentDescription = null, tint = BrandSecondary, modifier = Modifier.size(18.dp))
                    Text("Pro Juventute", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = BrandOnSurfaceVariant)
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.clickable {
                        Toast.makeText(context, "ASPI - Fondazione della Svizzera Italiana per l'Aiuto, il Sostegno e la Protezione dell'Infanzia", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = BrandPrimary, modifier = Modifier.size(18.dp))
                    Text("ASPI", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = BrandOnSurfaceVariant)
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.clickable {
                    Toast.makeText(context, "Polizia Cantonale del Canton Ticino", Toast.LENGTH_SHORT).show()
                }
            ) {
                Icon(Icons.Default.LocalPolice, contentDescription = null, tint = BrandOutline, modifier = Modifier.size(18.dp))
                Text("Polizia Cantonale", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = BrandOnSurfaceVariant)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

// ==========================================
// 2. SUPPORT / TRIAGE TAB SCREEN
// ==========================================
@Composable
fun SupportTabScreen(viewModel: CyberHelpViewModel) {
    val step by viewModel.triageStep.collectAsStateWithLifecycle()
    val selectedCat by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val selectedUrg by viewModel.selectedUrgency.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header with step description and indicator
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = when (step) {
                        1 -> "PASSO 1 DI 3"
                        2 -> "PASSO 2 DI 3"
                        else -> "Triage completato"
                    }.uppercase(),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandPrimary
                )
                Text(
                    text = when (step) {
                        1 -> "Identificazione"
                        2 -> "Urgenza"
                        else -> "100% Completato"
                    },
                    fontSize = 12.sp,
                    color = BrandOutline
                )
            }
            LinearProgressIndicator(
                progress = when (step) {
                    1 -> 0.33f
                    2 -> 0.66f
                    else -> 1f
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = if (step == 3) Color(0xFF26A69A) else BrandPrimary,
                trackColor = BrandSurfaceHigh
            )
        }

        AnimatedContent(
            targetState = step,
            transitionSpec = {
                slideInHorizontally { width -> if (targetState > initialState) width else -width } + fadeIn() togetherWith
                        slideOutHorizontally { width -> if (targetState > initialState) -width else width } + fadeOut()
            },
            label = "StepAnimation",
            modifier = Modifier.weight(1f)
        ) { currentStep ->
            when (currentStep) {
                1 -> TriageStep1(
                    selectedCategory = selectedCat,
                    onSelect = { viewModel.selectTriageCategory(it) },
                    onImmediateEmergency = { viewModel.startTriageImmediateEmergency() }
                )
                2 -> TriageStep2(
                    selectedUrgency = selectedUrg,
                    onSelect = { viewModel.selectTriageUrgency(it) },
                    onGoBack = { viewModel.prevTriageStep() }
                )
                else -> TriageOutcome(
                    selectedCategory = selectedCat ?: "Consiglio",
                    selectedUrgency = selectedUrg ?: "Consiglio",
                    onReset = { viewModel.resetTriage() }
                )
            }
        }

        // Standard Bottom navigation row for Steps 1 & 2
        if (step < 3) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = {
                        if (step == 1) {
                            viewModel.selectTab(AppTab.HOME)
                        } else {
                            viewModel.prevTriageStep()
                        }
                    }
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Indietro", fontWeight = FontWeight.Bold, color = BrandPrimary)
                }

                val canGoNext = (step == 1 && selectedCat != null) || (step == 2 && selectedUrg != null)
                Button(
                    enabled = canGoNext,
                    onClick = { viewModel.nextTriageStep() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BrandPrimary,
                        disabledContainerColor = BrandOutlineVariant
                    ),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.widthIn(min = 120.dp)
                ) {
                    Text("Avanti")
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

@Composable
fun TriageStep1(
    selectedCategory: String?,
    onSelect: (String) -> Unit,
    onImmediateEmergency: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "Cosa sta succedendo?",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = BrandOnSurface
            )
            Text(
                text = "Seleziona la situazione che meglio descrive quello che ti preoccupa. Ti aiuteremo a trovare la soluzione corretta.",
                fontSize = 14.sp,
                color = BrandOnSurfaceVariant
            )
        }

        // Options
        TriageOptionCard(
            title = "Relazioni a scuola o nello sport",
            description = "Insulti, esclusioni o minacce che avvengono di persona o nei gruppi WhatsApp della classe/squadra.",
            icon = Icons.Default.School,
            isSelected = selectedCategory == "Relazioni a scuola o nello sport",
            onClick = { onSelect("Relazioni a scuola o nello sport") }
        )

        TriageOptionCard(
            title = "Minacce online o foto rubate",
            description = "Qualcuno ti sta ricattando online, o foto/video privati sono stati condivisi senza il tuo permesso.",
            icon = Icons.Default.Smartphone,
            isSelected = selectedCategory == "Minacce online o foto rubate",
            onClick = { onSelect("Minacce online o foto rubate") }
        )

        TriageOptionCard(
            title = "Ho solo bisogno di parlare",
            description = "Mi sento giù, solo o confuso e vorrei confrontarmi con un professionista in modo anonimo.",
            icon = Icons.Default.Forum,
            isSelected = selectedCategory == "Ho solo bisogno di parlare",
            onClick = { onSelect("Ho solo bisogno di parlare") }
        )

        // Emergency Option with high contrast / Coral red color
        Card(
            onClick = onImmediateEmergency,
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = BrandTertiaryFixed),
            border = BorderStroke(1.5.dp, BrandTertiary),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(BrandTertiary, CircleShape)
                        .padding(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Emergenza",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Sono in pericolo ora",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF410003)
                    )
                    Text(
                        text = "C'è una minaccia immediata alla mia sicurezza fisica o a quella di qualcun altro.",
                        fontSize = 13.sp,
                        color = Color(0xFF8C1617)
                    )
                }
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = BrandTertiary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun TriageOptionCard(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) BrandSecondaryContainer else BrandSurfaceLowest
        ),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) BrandPrimary else BrandOutline
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(
                        if (isSelected) BrandPrimary else BrandSurfaceLow,
                        CircleShape
                    )
                    .padding(10.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isSelected) Color.White else BrandPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) BrandSecondary else BrandOnSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    fontSize = 13.sp,
                    color = BrandOnSurfaceVariant
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = if (isSelected) BrandPrimary else BrandOutline
            )
        }
    }
}

@Composable
fun TriageStep2(
    selectedUrgency: String?,
    onSelect: (String) -> Unit,
    onGoBack: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "Quanto è urgente?",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = BrandOnSurface
            )
            Text(
                text = "Questo ci aiuta a capire se hai bisogno di parlare subito con qualcuno o se possiamo fornirti risorse per gestire la situazione.",
                fontSize = 14.sp,
                color = BrandOnSurfaceVariant
            )
        }

        // Urgency options
        TriageOptionCard(
            title = "Ho bisogno di parlare ORA",
            description = "Connessione immediata e confidenziale con Pro Juventute 147 (Chat o Telefono).",
            icon = Icons.Default.PhoneInTalk,
            isSelected = selectedUrgency == "Ho bisogno di parlare ORA",
            onClick = { onSelect("Ho bisogno di parlare ORA") }
        )

        TriageOptionCard(
            title = "Voglio capire meglio cosa fare",
            description = "Guide pratiche, video e risorse per gestire il cyberbullismo a scuola.",
            icon = Icons.Default.LibraryBooks,
            isSelected = selectedUrgency == "Voglio capire meglio cosa fare",
            onClick = { onSelect("Voglio capire meglio cosa fare") }
        )

        TriageOptionCard(
            title = "Voglio segnalare un abuso",
            description = "Informazioni legali e contatti diretti con le autorità di polizia cantonale.",
            icon = Icons.Default.Gavel,
            isSelected = selectedUrgency == "Voglio segnalare un abuso",
            onClick = { onSelect("Voglio segnalare un abuso") }
        )

        // Extra Supportive Callout
        Box(
            modifier = Modifier
                .background(BrandSecondaryContainer.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                .border(1.dp, BrandOutline, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.SafetyCheck,
                    contentDescription = null,
                    tint = BrandPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Column {
                    Text(
                        text = "Non sei solo.",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandPrimary
                    )
                    Text(
                        text = "In Ticino ci sono professionisti pronti ad ascoltarti senza giudizio. La tua sicurezza è la nostra priorità.",
                        fontSize = 13.sp,
                        color = BrandOnSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun TriageOutcome(
    selectedCategory: String,
    selectedUrgency: String,
    onReset: () -> Unit
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    // Decide recommended helper based on triage answers
    val isEmergency = selectedCategory == "Sono in pericolo ora" || selectedUrgency == "Ho bisogno di parlare ORA"
    val isPoliceReport = selectedUrgency == "Voglio segnalare un abuso"

    val partnerName = when {
        isPoliceReport -> "Polizia Cantonale del Canton Ticino"
        isEmergency -> "Pro Juventute (Emergenza 147)"
        else -> "Pro Juventute Ticino"
    }

    val contactDetails = when {
        isPoliceReport -> "Contatta subito il 117 o recati in un posto di polizia per presentare una querela."
        else -> "Servizio 147 attivo 24 ore su 24, totalmente confidenziale e gratuito."
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Sparkle Circle
        Box(
            modifier = Modifier
                .background(Color(0xFFE0F2F1), CircleShape)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Success",
                tint = Color(0xFF26A69A),
                modifier = Modifier.size(48.dp)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Ecco come possiamo aiutarti",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = BrandOnSurface,
                textAlign = TextAlign.Center
            )
            Text(
                text = "In base a quello che ci hai detto, il partner più indicato per supportarti è:",
                fontSize = 14.sp,
                color = BrandOnSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Text(
                text = partnerName,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = BrandPrimary,
                textAlign = TextAlign.Center
            )
        }

        // Partner Logo Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = BrandSurfaceLowest),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Official Partner Logo placeholder
                if (isPoliceReport) {
                    Icon(
                        imageVector = Icons.Default.LocalPolice,
                        contentDescription = "Polizia",
                        tint = BrandOutline,
                        modifier = Modifier.size(64.dp)
                    )
                } else {
                    AsyncImage(
                        model = "https://lh3.googleusercontent.com/aida-public/AB6AXuCrzAsl2Mpbi8y13FYE8_qxQVfbkz9fkPz30dCepJS5qtBUBxmZpPWwbYITt2ycu2JTSVMj3fGcb6DVavnmvUV2uf25LU5S5CkZrN_Cwh6I4Kv6vdKn2o9nGFLzWQLNtNjLpT7tekuZoB2x9aRmKyo96gEKCLtohVOGt8u1ri5mKInZVXVcMK2VvYNft39NhKnpUSAFf634fYD_fo7f2kVgh1JdxKwAiAj7B9VvfzTXdg54_4ll0EhTf4Zbm5ZkPgsq4Me8QfILzA",
                        contentDescription = "Pro Juventute Logo",
                        modifier = Modifier
                            .height(54.dp)
                            .fillMaxWidth(),
                        contentScale = ContentScale.Fit
                    )
                }

                Box(
                    modifier = Modifier
                        .background(BrandSurfaceLow, RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.PrivacyTip,
                            contentDescription = null,
                            tint = Color(0xFF26A69A),
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Sei al sicuro. La conversazione è privata e gratuita. I professionisti sono qui per ascoltarti senza giudicare.",
                            fontSize = 12.sp,
                            color = BrandOnSurfaceVariant,
                            lineHeight = 16.sp
                        )
                    }
                }

                // Interactive Contact buttons
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (isPoliceReport) {
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:117"))
                                context.startActivity(intent)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Phone, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Chiama il 117 (Emergenza Polizia)")
                        }
                    } else {
                        Button(
                            onClick = {
                                Toast.makeText(context, "Avvio Chat Pro Juventute...", Toast.LENGTH_LONG).show()
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.147.ch/it/consulenza-tramite-chat/"))
                                context.startActivity(intent)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Forum, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Inizia Chat Anonima")
                        }

                        OutlinedButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:147"))
                                context.startActivity(intent)
                            },
                            border = BorderStroke(1.5.dp, BrandPrimary),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = BrandPrimary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Phone, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Chiama il 147")
                        }
                    }
                }
            }
        }

        // Return button
        TextButton(onClick = onReset) {
            Icon(Icons.Default.Refresh, contentDescription = null)
            Spacer(modifier = Modifier.width(4.dp))
            Text("Torna alla scelta della categoria", fontWeight = FontWeight.Bold, color = BrandPrimary)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

// ==========================================
// 3. LEARN TAB SCREEN (IMPARA & ESPLORA)
// ==========================================
@Composable
fun LearnTabScreen(viewModel: CyberHelpViewModel) {
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val activeFilter by viewModel.selectedLearnCategory.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Expanded states
    var showLucaStoryDetails by remember { mutableStateOf(false) }
    var showSofiaStoryDetails by remember { mutableStateOf(false) }
    var showMarcoStoryDetails by remember { mutableStateOf(false) }
    var showElenaStoryDetails by remember { mutableStateOf(false) }
    var showGuideDetails by remember { mutableStateOf(false) }
    var showVrDetails by remember { mutableStateOf(false) }

    // Search and filter
    val filters = listOf("Tutto", "Video", "Storie", "Esperienza VR", "Legale")

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Title & Description
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Impara & Esplora",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandPrimary
                )
                Text(
                    text = "Scopri storie di persone come te, impara come difenderti e vivi esperienze immersive per comprendere meglio il cyberbullismo.",
                    fontSize = 15.sp,
                    color = BrandOnSurfaceVariant,
                    lineHeight = 20.sp
                )
            }
        }

        // Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                placeholder = { Text("Cerca risorse, video, guide...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BrandPrimary,
                    unfocusedBorderColor = BrandOutlineVariant
                )
            )
        }

        // Filter chips row
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(filters) { f ->
                    val isSelected = activeFilter == f
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.selectLearnCategory(f) },
                        label = { Text(f, fontWeight = FontWeight.Bold) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BrandPrimary,
                            selectedLabelColor = Color.White
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                }
            }
        }

        // VR Teaser Card (Only shown if "Tutto" or "Esperienza VR" is selected)
        if (activeFilter == "Tutto" || activeFilter == "Esperienza VR") {
            item {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = BrandOnSurface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                        ) {
                            AsyncImage(
                                model = "https://lh3.googleusercontent.com/aida-public/AB6AXuAVQXFwQUZHneXGJu0mFVtli2vNxj4EncEYGNYe_Hn1xm28VBWKEiyiCCLa13MgRzxjQC-xqrsOa2drBftHDUl8oOsrsk_cmFc1xyE4aRS9aBfwW-TqG8YsAGzEQkWAhnk0EtIjwyY4RyNpUamBhf6O9f6Ptt8ZqPP73nwkdwI7ESn9RlzvHe07aLKDymLypu1NVV_JZ3CmRu1WONhoz9JwokUjMf_cZR9wDkVD6V93E3mTMSGTSAtsxNIX4AoA-PGid1WauTxJ9A",
                                contentDescription = "VR Education Teaser",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            // Gradient Overlay
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))
                                        )
                                    )
                            )
                            // "Novità" Badge
                            Box(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .background(BrandSecondary, RoundedCornerShape(12.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                                    .align(Alignment.TopStart)
                            ) {
                                Text("NOVITÀ", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                "Bullying VR: Mettiti nei loro panni",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Un'esperienza immersiva a 360° per comprendere l'impatto reale delle parole online. Prova l'anteprima virtuale adesso.",
                                color = Color.LightGray,
                                fontSize = 13.sp,
                                lineHeight = 18.sp
                            )
                            Button(
                                onClick = { showVrDetails = true },
                                colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Avvia Esperienza VR")
                            }
                        }
                    }
                }
            }
        }

        // Search/Filter Matches (Default Stories and Videos)
        val defaultResources = listOf(
            ResourceItem(
                id = "luca_story",
                title = "Come ho smesso di leggere i commenti e ho iniziato a vivere.",
                desc = "La storia di Luca. Un racconto onesto su come affrontare gli hater sui social media e ritrovare la propria sicurezza offline...",
                category = "Storie",
                author = "La storia di Luca",
                extra = "Testimonianza"
            ),
            ResourceItem(
                id = "sofia_story",
                title = "Le parole feriscono, il silenzio di più.",
                desc = "La storia di Sofia. Come ha trovato il coraggio di confidarsi con gli insegnanti dopo mesi di cyberbullismo in chat...",
                category = "Storie",
                author = "La storia di Sofia",
                extra = "Testimonianza"
            ),
            ResourceItem(
                id = "marco_story",
                title = "Dietro lo schermo dei videogiochi.",
                desc = "La storia di Marco. Superare gli insulti e le minacce nelle chat audio dei giochi online grazie al supporto degli amici...",
                category = "Storie",
                author = "La storia di Marco",
                extra = "Testimonianza"
            ),
            ResourceItem(
                id = "elena_story",
                title = "Il furto d'identità e i profili falsi.",
                desc = "La storia di Elena. Come ha gestito e risolto la creazione di un profilo falso a suo nome con l'aiuto della Polizia Cantonale...",
                category = "Storie",
                author = "La storia di Elena",
                extra = "Testimonianza"
            ),
            ResourceItem(
                id = "5_steps_guide",
                title = "5 passi per segnalare un abuso online",
                desc = "Guida pratica su come bloccare, segnalare e raccogliere prove in modo sicuro sulle principali piattaforme social (Instagram, TikTok, WhatsApp).",
                category = "Legale",
                author = "Guida pratica",
                extra = "Video"
            )
        )

        val filteredResources = defaultResources.filter { res ->
            val matchesCategory = activeFilter == "Tutto" || res.category == activeFilter || (activeFilter == "Video" && res.extra == "Video")
            val matchesSearch = searchQuery.isBlank() || res.title.contains(searchQuery, ignoreCase = true) || res.desc.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
        }

        if (filteredResources.isEmpty() && activeFilter != "Esperienza VR") {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.MenuBook, contentDescription = null, tint = BrandOutline, modifier = Modifier.size(48.dp))
                    Text("Nessuna risorsa trovata", fontWeight = FontWeight.Bold, color = BrandOnSurface)
                    Text("Prova a cercare qualcos'altro o a cambiare filtro.", fontSize = 13.sp, color = BrandOutline)
                }
            }
        } else {
            items(filteredResources) { resource ->
                Card(
                    onClick = {
                        if (resource.id == "luca_story") showLucaStoryDetails = true
                        if (resource.id == "sofia_story") showSofiaStoryDetails = true
                        if (resource.id == "marco_story") showMarcoStoryDetails = true
                        if (resource.id == "elena_story") showElenaStoryDetails = true
                        if (resource.id == "5_steps_guide") showGuideDetails = true
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = BrandSurfaceLowest),
                    border = BorderStroke(1.dp, BrandSurfaceDim.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        if (resource.id == "5_steps_guide") {
                            // Display video placeholder
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(130.dp)
                            ) {
                                AsyncImage(
                                    model = "https://lh3.googleusercontent.com/aida-public/AB6AXuAlvrCpwHIUCOVXMtgr_mqIXNPF_sdllmu39-WjHDlLgO3Aj518pOnsucnu4Ml1MjSZfzRogbvhCHS5d7_llKlLCOmgvc8ocwQ2Cwvuvz7zVC_x_zB2uJ1coNRMB6vz_qv_cWvkDgaR5xEn3QRrCXTBLMXMvGU-l7vaOO4WHpEKxENAzThgK6WUuQDYeD_wDzpWLz4nCUMjmMyDI8IdeCbURzqLjDI7E5EEpuNjh-fnOrIXmUi1Zb9USxStJhF4FBr4knYetVX3cw",
                                    contentDescription = "Video Thumbnail",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Black.copy(alpha = 0.3f))
                                )
                                Icon(
                                    imageVector = Icons.Default.PlayCircle,
                                    contentDescription = "Play",
                                    tint = Color.White,
                                    modifier = Modifier
                                        .size(48.dp)
                                        .align(Alignment.Center)
                                )
                            }
                        }

                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .background(BrandPrimary.copy(alpha = 0.1f), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = resource.author.take(1),
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = BrandPrimary
                                        )
                                    }
                                    Column {
                                        Text(resource.author, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        Text("Condiviso 2 giorni fa", fontSize = 10.sp, color = BrandOutline)
                                    }
                                }

                                IconButton(
                                    onClick = {
                                        viewModel.toggleSavedResource(resource.id, resource.title, resource.category)
                                    }
                                ) {
                                    val isFav = viewModel.savedResources.collectAsStateWithLifecycle().value.any { it.id == resource.id && it.isSaved }
                                    Icon(
                                        imageVector = if (isFav) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
                                        contentDescription = "Salva",
                                        tint = if (isFav) BrandPrimary else BrandOutline
                                    )
                                }
                            }

                            Text(
                                text = resource.title,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = BrandPrimary
                            )
                            Text(
                                text = resource.desc,
                                fontSize = 13.sp,
                                color = BrandOnSurfaceVariant,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(BrandSurfaceLow, RoundedCornerShape(12.dp))
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        "#" + resource.extra,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = BrandOnSurfaceVariant
                                    )
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text("Leggi", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BrandPrimary)
                                    Icon(Icons.Default.ArrowForward, contentDescription = null, tint = BrandPrimary, modifier = Modifier.size(14.dp))
                                }
                            }
                        }
                    }
                }
            }
        }

        // INTERACTIVE TOOL: Generatore di Risposte Sicure (Safe Response Generator)
        item {
            InteractiveResponseGeneratorCard(viewModel)
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    // VR Experience Dialog
    if (showVrDetails) {
        AlertDialog(
            onDismissRequest = { showVrDetails = false },
            title = { Text("Bullying VR Simulator", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Stai avviando un modulo di educazione digitale immersivo Ticinese.", fontWeight = FontWeight.SemiBold)
                    Text("Questo simulatore ti guiderà a metterti nei panni di un adolescente affrontato da critiche e insulti online. Imparerai l'auto-controllo e la comunicazione non violenta.", fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .background(Color.Black, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🔊 [Simulatore Caricato: 100%]", color = Color.Green, fontSize = 12.sp)
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    showVrDetails = false
                    Toast.makeText(context, "Simulatore VR avviato nel browser sicuro!", Toast.LENGTH_SHORT).show()
                }) {
                    Text("Inizia ora")
                }
            },
            dismissButton = {
                TextButton(onClick = { showVrDetails = false }) {
                    Text("Chiudi")
                }
            }
        )
    }

    // Story Dialog Details - EXPLICIT LIGHT THEME STYLING
    if (showLucaStoryDetails) {
        AlertDialog(
            onDismissRequest = { showLucaStoryDetails = false },
            containerColor = Color.White,
            titleContentColor = Color(0xFF1D1B20),
            textContentColor = Color(0xFF49454F),
            title = { 
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.MenuBook, contentDescription = null, tint = BrandPrimary)
                    Text("La storia di Luca", fontWeight = FontWeight.Bold, color = BrandSecondary) 
                }
            },
            text = {
                LazyColumn(modifier = Modifier.heightIn(max = 300.dp)) {
                    item {
                        Text(
                            text = "\"Come ho smesso di leggere i commenti e ho iniziato a vivere.\"\n\nTutto è iniziato con un banale meme modificato sul gruppo WhatsApp della classe. All'inizio sembrava uno scherzo, ma poi gli insulti sono diventati giornalieri e feroci. Mi vergognavo a tal punto che non volevo più andare a scuola.\n\nUna mattina, leggendo sul portale di CyberHelp Ticino, ho scoperto la figura del Student Ambassador e ho scritto in privato a Chiara. Mi ha risposto subito. Abbiamo parlato di persona e mi ha spiegato che la colpa non era mia, ma di chi sputava odio.\n\nMi ha incoraggiato a bloccare i bulli, a fare querela se continuava e ad aprirmi con i miei genitori. Da quel giorno ho ricominciato ad uscire offline e a giocare a basket. Parlare salva la vita.",
                            fontSize = 14.sp,
                            color = Color(0xFF49454F),
                            lineHeight = 22.sp
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showLucaStoryDetails = false },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary, contentColor = Color.White)
                ) {
                    Text("Ho capito")
                }
            }
        )
    }

    if (showSofiaStoryDetails) {
        AlertDialog(
            onDismissRequest = { showSofiaStoryDetails = false },
            containerColor = Color.White,
            titleContentColor = Color(0xFF1D1B20),
            textContentColor = Color(0xFF49454F),
            title = {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.MenuBook, contentDescription = null, tint = BrandPrimary)
                    Text("La storia di Sofia", fontWeight = FontWeight.Bold, color = BrandSecondary)
                }
            },
            text = {
                LazyColumn(modifier = Modifier.heightIn(max = 300.dp)) {
                    item {
                        Text(
                            text = "\"Le parole feriscono, il silenzio di più.\"\n\nTutto è cominciato con un fotomontaggio stupido e uno sticker creato per prendermi in giro. Nel giro di poche ore è finito in decine di chat della scuola. Ogni volta che entravo in aula mi sembrava che tutti ridessero di me. Ho smesso di parlare, mi tenevo tutto dentro.\n\nMia sorella maggiore se n'è accorta e mi ha consigliato di usare la risorsa anonima di CyberHelp. Ho chattato con uno psicologo del portale che mi ha aiutato a capire come non isolarmi e a segnalare la diffusione non autorizzata. La scuola è intervenuta e gli sticker sono stati cancellati. Non restate in silenzio!",
                            fontSize = 14.sp,
                            color = Color(0xFF49454F),
                            lineHeight = 22.sp
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showSofiaStoryDetails = false },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary, contentColor = Color.White)
                ) {
                    Text("Ho capito")
                }
            }
        )
    }

    if (showMarcoStoryDetails) {
        AlertDialog(
            onDismissRequest = { showMarcoStoryDetails = false },
            containerColor = Color.White,
            titleContentColor = Color(0xFF1D1B20),
            textContentColor = Color(0xFF49454F),
            title = {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.MenuBook, contentDescription = null, tint = BrandPrimary)
                    Text("La storia di Marco", fontWeight = FontWeight.Bold, color = BrandSecondary)
                }
            },
            text = {
                LazyColumn(modifier = Modifier.heightIn(max = 300.dp)) {
                    item {
                        Text(
                            text = "\"Dietro lo schermo dei videogiochi.\"\n\nNel server Discord dove giochiamo di solito, un gruppo di player ha iniziato a prendermi di mira. Mi insultavano via audio, mi sabotavano le partite e dicevano che avrebbero trovato il mio indirizzo IP. All'inizio ho provato a ignorarli, ma l'ansia cresceva ogni volta che accendevo il PC.\n\nHo trovato la guida di CyberHelp e ho capito che potevo agire: ho registrato le chat audio, fatto screenshot delle minacce e inviato una segnalazione dettagliata sia a Discord che ai moderatori del gioco. I loro account sono stati bannati permanentemente. Lo schermo non è uno scudo per i bulli.",
                            fontSize = 14.sp,
                            color = Color(0xFF49454F),
                            lineHeight = 22.sp
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showMarcoStoryDetails = false },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary, contentColor = Color.White)
                ) {
                    Text("Ho capito")
                }
            }
        )
    }

    if (showElenaStoryDetails) {
        AlertDialog(
            onDismissRequest = { showElenaStoryDetails = false },
            containerColor = Color.White,
            titleContentColor = Color(0xFF1D1B20),
            textContentColor = Color(0xFF49454F),
            title = {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.MenuBook, contentDescription = null, tint = BrandPrimary)
                    Text("La storia di Elena", fontWeight = FontWeight.Bold, color = BrandSecondary)
                }
            },
            text = {
                LazyColumn(modifier = Modifier.heightIn(max = 300.dp)) {
                    item {
                        Text(
                            text = "\"Il furto d'identità e i profili falsi.\"\n\nQualcuno ha creato un secondo profilo Instagram usando le mie foto e il mio nome, pubblicando post offensivi a mio nome e inviando messaggi volgari ai miei contatti. Ero disperata, i miei amici mi chiedevano spiegazioni.\n\nSu CyberHelp ho trovato subito i passi legali da seguire: ho fatto raccogliere gli screenshot a un testimone (mia madre), ho inviato la segnalazione ufficiale a Instagram per furto d'identità e, seguendo i consigli del portale, abbiamo sporto denuncia formale alla Polizia Cantonale. Il profilo falso è stato rimosso in meno di 24 ore. C'è sempre una via d'uscita legale!",
                            fontSize = 14.sp,
                            color = Color(0xFF49454F),
                            lineHeight = 22.sp
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showElenaStoryDetails = false },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary, contentColor = Color.White)
                ) {
                    Text("Ho capito")
                }
            }
        )
    }

    // Guide Dialog Details
    if (showGuideDetails) {
        AlertDialog(
            onDismissRequest = { showGuideDetails = false },
            title = { Text("5 Passi per Segnalare Abuso", fontWeight = FontWeight.Bold, color = BrandPrimary) },
            text = {
                LazyColumn(modifier = Modifier.heightIn(max = 300.dp)) {
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text("1. Non rispondere mai con rabbia o insulti. Conserva la calma.", fontWeight = FontWeight.Bold)
                            Text("2. Salva le prove: fai screenshot dettagliati dei messaggi, link dei profili e orari completi.", fontSize = 13.sp)
                            Text("3. Blocca subito l'account abusivo per tutelare la tua salute mentale.", fontWeight = FontWeight.Bold)
                            Text("4. Segnala il contenuto direttamente sulla piattaforma social usando gli appositi moduli di abuso.", fontSize = 13.sp)
                            Text("5. Se gli insulti contengono minacce di violenza fisica, ricatti o condivisione di foto intime, contatta subito la Polizia Cantonale o Pro Juventute.", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = { showGuideDetails = false }) {
                    Text("Grazie per la guida")
                }
            }
        )
    }
}

data class ResourceItem(
    val id: String,
    val title: String,
    val desc: String,
    val category: String,
    val author: String,
    val extra: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InteractiveResponseGeneratorCard(viewModel: CyberHelpViewModel) {
    val offensiveInput by viewModel.offensiveInput.collectAsStateWithLifecycle()
    val generatedResponse by viewModel.generatedResponse.collectAsStateWithLifecycle()
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = BrandSurfaceLow),
        border = BorderStroke(1.dp, BrandOutline),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(BrandPrimary, RoundedCornerShape(12.dp))
                        .padding(6.dp)
                ) {
                    Icon(Icons.Default.Shield, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                }
                Text("Generatore di Risposte Sicure", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = BrandSecondary)
            }

            Text(
                text = "Inserisci un insulto o una critica offensiva ricevuta online. Genereremo una risposta assertiva e controllata che blocca il conflitto senza alimentarlo.",
                fontSize = 12.sp,
                color = BrandOnSurfaceVariant,
                lineHeight = 16.sp
            )

            OutlinedTextField(
                value = offensiveInput,
                onValueChange = { viewModel.updateOffensiveInput(it) },
                placeholder = { Text("Esempio: Sei ridicolo, fai finta di essere bravo...") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BrandPrimary,
                    unfocusedBorderColor = BrandOutline,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Button(
                onClick = { viewModel.generateSafeResponse() },
                enabled = offensiveInput.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Genera Risposta Sicura")
            }

            if (generatedResponse != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BrandSecondaryContainer, RoundedCornerShape(16.dp))
                        .border(1.dp, BrandOutline, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("RISPOSTA CONSIGLIATA:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = BrandSecondary)
                            IconButton(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(generatedResponse!!))
                                    Toast.makeText(context, "Copiato negli appunti!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = BrandPrimary, modifier = Modifier.size(16.dp))
                            }
                        }
                        Text(
                            text = generatedResponse!!,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = BrandOnSurface
                        )
                    }
                }
            }
        }
    }
}

// ==========================================
// 4. CIRCLE / COMMUNITY TAB SCREEN
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CircleTabScreen(viewModel: CyberHelpViewModel) {
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val forumPosts by viewModel.forumPosts.collectAsStateWithLifecycle()
    val diaryEntries by viewModel.diaryEntries.collectAsStateWithLifecycle()

    var showProfileEditor by remember { mutableStateOf(false) }
    var showNewPostDialog by remember { mutableStateOf(false) }
    var showNewDiaryDialog by remember { mutableStateOf(false) }

    // Form inputs
    var newPostTitle by remember { mutableStateOf("") }
    var newPostContent by remember { mutableStateOf("") }
    var newPostCategory by remember { mutableStateOf("Tutto") }

    var diaryCategory by remember { mutableStateOf("Relazioni a scuola o sport") }
    var diaryFeeling by remember { mutableStateOf("Tranquillo") }
    var diaryIntensity by remember { mutableStateOf(3) }
    var diaryNote by remember { mutableStateOf("") }

    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Community & Action Hero Section
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Comunità & Azione",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandOnSurface
                )
                Text(
                    text = "Siamo un ecosistema di supporto reciproco in Ticino. Scopri i nostri Student Ambassador, unisciti alle iniziative e monitora il tuo benessere.",
                    fontSize = 15.sp,
                    color = BrandOnSurfaceVariant,
                    lineHeight = 20.sp
                )
            }
        }

        // Active User profile card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BrandSurfaceLowest),
                border = BorderStroke(1.dp, BrandOutline),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .background(BrandPrimary.copy(alpha = 0.15f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = "Profile", tint = BrandPrimary, modifier = Modifier.size(32.dp))
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = userProfile.alias,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrandOnSurface
                        )
                        Text(
                            text = if (userProfile.anonymousMode) "Modalità Anonima Attiva" else "Profilo Visibile",
                            fontSize = 12.sp,
                            color = if (userProfile.anonymousMode) Color(0xFF26A69A) else BrandOutline,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    OutlinedButton(
                        onClick = { showProfileEditor = true },
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, BrandPrimary),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = BrandPrimary)
                    ) {
                        Text("Modifica")
                    }
                }
            }
        }

        // Student Ambassadors section
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Groups, contentDescription = null, tint = BrandSecondary)
                    Text("Student Ambassadors", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = BrandOnSurface)
                }
                Text(
                    text = "I nostri ambasciatori sono studenti formati in Ticino per offrire primo supporto, mediazione e ascolto contro il cyberbullismo nelle scuole.",
                    fontSize = 13.sp,
                    color = BrandOnSurfaceVariant
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Chiara Card
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                Toast
                                    .makeText(context, "Chiara M: Liceo Lugano 1. Mandale un messaggio nel forum!", Toast.LENGTH_LONG)
                                    .show()
                            },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = BrandSurfaceLowest),
                        border = BorderStroke(1.dp, BrandOutline)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            AsyncImage(
                                model = "https://lh3.googleusercontent.com/aida-public/AB6AXuBbSyws169YfnHTz_9BGBeK6OT-5-1WE_94Cwf6Bd8Bp74wBnHnQbmia6njQbK7Re7d-0FuIX37wWvLBuGGiYbMfg455hDvXClpOHMtUrfkgl8OWSa1HMXMXN3hd9t2PC2yMwWbLgzFkrCWUlMCtY37J5LxVzXwQrT5aMJgRiPjIgWVq2gVDVSGdAizFoNNBv-Jr8MLzYCZCGQzge6mwualAP0u7-fZn6A9SPvSkhf3t9eEvKSNKbH-CHGV_VgovOa74qA9eyT1nw",
                                contentDescription = "Chiara M.",
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            Text("Chiara M.", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text("Liceo di Lugano 1", fontSize = 11.sp, color = BrandOutline)
                            Box(
                                modifier = Modifier
                                    .background(BrandSecondary.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("Supporto pari", fontSize = 10.sp, color = BrandSecondary, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // Matteo Card
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                Toast
                                    .makeText(context, "Matteo R: Scuola Cantonale di Commercio. Scrivigli una domanda nel forum!", Toast.LENGTH_LONG)
                                    .show()
                            },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = BrandSurfaceLowest),
                        border = BorderStroke(1.dp, BrandOutline)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            AsyncImage(
                                model = "https://lh3.googleusercontent.com/aida-public/AB6AXuAEN1GydFUQZMF0eZWEvXzP2oQUUXOSx8W1CWfphkXSTdUQ9-7J99cyzxQQdRszydP3Q8XO63n8PeRSjfD7Z5HTseks6yBQCp5AT6BpXNKGUizDpx5bVA-UzaVS3WedV-W0kjXQGYc4u9jlOtyB_hkV_iYoY9iB9bnm5P6f6m9GuQM0Zz50LTy-TgHyenREd8UjhhMBanuKzOmRjITYP-OThvW5n5ntP9MdFDbSGg2jsmt7KZSf7CCCJly8t62l7KrDPcMc0JC5YQ",
                                contentDescription = "Matteo R.",
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            Text("Matteo R.", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text("SCC Bellinzona", fontSize = 11.sp, color = BrandOutline)
                            Box(
                                modifier = Modifier
                                    .background(BrandSecondary.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("Educazione Digitale", fontSize = 10.sp, color = BrandSecondary, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // DIARY LOGS & FEELINGS TRACKER
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Book, contentDescription = null, tint = BrandPrimary)
                        Text("Il mio Diario Sicuro", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = BrandOnSurface)
                    }

                    TextButton(onClick = { showNewDiaryDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Text("Nuovo Log", fontWeight = FontWeight.Bold)
                    }
                }

                Text(
                    text = "Registra i tuoi stati d'animo o eventi di preoccupazione in modo sicuro e locale sul tuo dispositivo per monitorare l'evoluzione del tuo benessere.",
                    fontSize = 13.sp,
                    color = BrandOnSurfaceVariant
                )

                if (diaryEntries.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(BrandSurfaceLow, RoundedCornerShape(12.dp))
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Ancora nessun diario registrato. Clicca su 'Nuovo Log' per iniziare.", fontSize = 13.sp, color = BrandOutline, textAlign = TextAlign.Center)
                    }
                } else {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(diaryEntries) { log ->
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = BrandSurfaceLowest),
                                border = BorderStroke(1.dp, BrandOutline),
                                modifier = Modifier.width(220.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = SimpleDateFormat("dd MMM, HH:mm", Locale.ITALIAN).format(Date(log.timestamp)),
                                            fontSize = 10.sp,
                                            color = BrandOutline
                                        )
                                        IconButton(
                                            onClick = { viewModel.removeDiary(log) },
                                            modifier = Modifier.size(16.dp)
                                        ) {
                                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = BrandTertiary, modifier = Modifier.size(14.dp))
                                        }
                                    }
                                    Text(log.category, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BrandPrimary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Icon(Icons.Default.EmojiEmotions, contentDescription = null, tint = BrandSecondary, modifier = Modifier.size(16.dp))
                                        Text(log.feeling, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                    }
                                    Text("Intensità disagio: ${log.intensity}/5", fontSize = 12.sp, color = BrandOutline)
                                    if (log.note.isNotBlank()) {
                                        Text(
                                            text = log.note,
                                            fontSize = 12.sp,
                                            color = BrandOnSurfaceVariant,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // LOCAL COMMUNITY FORUM
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Comment, contentDescription = null, tint = BrandSecondary)
                        Text("Forum di Discussione", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = BrandOnSurface)
                    }

                    Button(
                        onClick = { showNewPostDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
                        shape = RoundedCornerShape(16.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Scrivi Post", fontSize = 12.sp)
                    }
                }

                Text(
                    text = "Condividi le tue preoccupazioni, storie o riflessioni in modo anonimo e protetto. Gli Student Ambassador supervisionano questo spazio.",
                    fontSize = 13.sp,
                    color = BrandOnSurfaceVariant
                )
            }
        }

        // Render Forum Posts
        items(forumPosts) { post ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BrandSurfaceLowest),
                border = BorderStroke(1.dp, BrandOutline),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(BrandPrimary.copy(alpha = 0.1f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = post.author.take(1),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BrandPrimary
                                )
                            }
                            Column {
                                Text(post.author, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text(post.school, fontSize = 11.sp, color = BrandOutline)
                            }
                        }

                        if (post.isUserGenerated) {
                            IconButton(onClick = { viewModel.deletePost(post) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = BrandTertiary)
                            }
                        }
                    }

                    Text(
                        text = post.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandPrimary
                    )

                    Text(
                        text = post.content,
                        fontSize = 14.sp,
                        color = BrandOnSurfaceVariant,
                        lineHeight = 19.sp
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFE0F2F1), RoundedCornerShape(12.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                "#" + post.category,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF26A69A)
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.clickable { viewModel.toggleLike(post) }
                        ) {
                            Icon(
                                imageVector = if (post.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Like",
                                tint = if (post.isLiked) BrandTertiary else BrandOutline,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = post.likes.toString(),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (post.isLiked) BrandTertiary else BrandOutline
                            )
                        }
                    }
                }
            }
        }

        // INSPIRATION / PARTNER CORNER: Mozilla Canada Inspiration
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BrandSurfaceLowest),
                border = BorderStroke(1.dp, BrandSurfaceDim),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                    ) {
                        AsyncImage(
                            model = "https://lh3.googleusercontent.com/aida-public/AB6AXuCg0Yy39fX_7cnvZ6qc1iXeUW-IakNJ4FWchjasHjnEZ18YIxHaLxEbIBcrMVEs9f0IzXWJip3Z0maoDf6eIPHNubW7v2yzxrFVV4RXSaYX8vNB5BHOW19voi9VcXqogLvXQHNkke_TUWz6FJ5mGABBFjHMyKLW3-bkLl_NQYulsK3oKddS_hg6aoPM2t7YqWE1T7uK3nWhQGlUcluYLp0dcMyhwiahlZ13oAnoapUk9dpTAJmY66m8xS3G7ZBXgWlcWkjn3AGn5A",
                            contentDescription = "Abstract connections",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.5f))
                        )
                        Text(
                            text = "Ispirazione Globale",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(12.dp)
                                .align(Alignment.BottomStart)
                        )
                    }

                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "L'Iniziativa 'Ca.pture' di Mozilla",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrandPrimary
                        )
                        Text(
                            text = "Ci ispiriamo al progetto Ca.pture di Mozilla Canada, che promuove un approccio empatico e guidato dalla comunità per documentare e contrastare le molestie online. Adottiamo i loro principi di etica dei dati e protezione della privacy nel contesto svizzero.",
                            fontSize = 12.sp,
                            color = BrandOnSurfaceVariant,
                            lineHeight = 17.sp
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    // PROFILE EDITOR DIALOG
    if (showProfileEditor) {
        var tempAlias by remember { mutableStateOf(userProfile.alias) }
        var tempAnon by remember { mutableStateOf(userProfile.anonymousMode) }

        AlertDialog(
            onDismissRequest = { showProfileEditor = false },
            title = { Text("Configura Profilo", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedTextField(
                        value = tempAlias,
                        onValueChange = { tempAlias = it },
                        label = { Text("Alias / Nome Utente") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Pubblica come Anonimo", fontWeight = FontWeight.Bold)
                            Text("I tuoi post saranno marchiati come 'Utente Anonimo'", fontSize = 12.sp, color = BrandOutline)
                        }
                        Switch(
                            checked = tempAnon,
                            onCheckedChange = { tempAnon = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = BrandPrimary)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateProfile(tempAlias, "avatar1", tempAnon)
                        showProfileEditor = false
                        Toast.makeText(context, "Profilo salvato in locale!", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Text("Salva")
                }
            },
            dismissButton = {
                TextButton(onClick = { showProfileEditor = false }) {
                    Text("Chiudi")
                }
            }
        )
    }

    // NEW FORUM POST DIALOG
    if (showNewPostDialog) {
        AlertDialog(
            onDismissRequest = { showNewPostDialog = false },
            title = { Text("Nuovo Post nel Forum", fontWeight = FontWeight.Bold, color = BrandPrimary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = newPostTitle,
                        onValueChange = { newPostTitle = it },
                        label = { Text("Titolo della storia") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = newPostContent,
                        onValueChange = { newPostContent = it },
                        label = { Text("Raccontaci cosa sta succedendo o fai una domanda...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                    )

                    // Category Selector
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Categoria", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BrandOutline)
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.horizontalScroll(rememberScrollState())
                        ) {
                            val categories = listOf("Bullismo", "Social", "Sport", "Consiglio", "Aiuto")
                            categories.forEach { cat ->
                                val selected = newPostCategory == cat
                                FilterChip(
                                    selected = selected,
                                    onClick = { newPostCategory = cat },
                                    label = { Text(cat, fontSize = 11.sp) }
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newPostTitle.isNotBlank() && newPostContent.isNotBlank()) {
                            viewModel.publishForumPost(newPostTitle, newPostContent, newPostCategory)
                            newPostTitle = ""
                            newPostContent = ""
                            showNewPostDialog = false
                            Toast.makeText(context, "Post pubblicato sul forum locale!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Compila tutti i campi!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary)
                ) {
                    Text("Pubblica")
                }
            },
            dismissButton = {
                TextButton(onClick = { showNewPostDialog = false }) {
                    Text("Annulla")
                }
            }
        )
    }

    // NEW DIARY LOG DIALOG
    if (showNewDiaryDialog) {
        val categories = listOf("Relazioni a scuola o sport", "Immagini e Privacy", "Hate Speech e Offese", "Ho solo bisogno di un consiglio")
        val feelings = listOf("Tranquillo", "Speranzoso", "Preoccupato", "Triste", "Arrabbiato", "Ansioso")

        AlertDialog(
            onDismissRequest = { showNewDiaryDialog = false },
            title = { Text("Nuovo Log di Benessere", fontWeight = FontWeight.Bold, color = BrandPrimary) },
            text = {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Cosa descrive la tua situazione?", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BrandOutline)
                            categories.forEach { cat ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { diaryCategory = cat }
                                        .padding(vertical = 4.dp)
                                ) {
                                    RadioButton(selected = diaryCategory == cat, onClick = { diaryCategory = cat })
                                    Text(cat, fontSize = 13.sp)
                                }
                            }
                        }
                    }

                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Come ti senti?", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BrandOutline)
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.horizontalScroll(rememberScrollState())
                            ) {
                                feelings.forEach { f ->
                                    val sel = diaryFeeling == f
                                    FilterChip(
                                        selected = sel,
                                        onClick = { diaryFeeling = f },
                                        label = { Text(f) }
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Livello di disagio / ansia: $diaryIntensity/5", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BrandOutline)
                            Slider(
                                value = diaryIntensity.toFloat(),
                                onValueChange = { diaryIntensity = it.toInt() },
                                valueRange = 1f..5f,
                                steps = 3,
                                colors = SliderDefaults.colors(thumbColor = BrandPrimary, activeTrackColor = BrandPrimary)
                            )
                        }
                    }

                    item {
                        OutlinedTextField(
                            value = diaryNote,
                            onValueChange = { diaryNote = it },
                            label = { Text("Note libere (Cosa è successo?)") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.addDiary(diaryCategory, diaryIntensity, diaryFeeling, diaryNote)
                        diaryNote = ""
                        showNewDiaryDialog = false
                        Toast.makeText(context, "Stato registrato nel diario locale!", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Text("Salva Log")
                }
            },
            dismissButton = {
                TextButton(onClick = { showNewDiaryDialog = false }) {
                    Text("Annulla")
                }
            }
        )
    }
}
