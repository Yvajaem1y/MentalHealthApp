package com.hackhathon.mentalhealthapp.bottomNav

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hackhathon.features.R
import com.hackhathon.mentalhealthapp.addNoteScreen.NoteViewModel
import com.hackhathon.mentalhealthapp.breathingScreen.BreathingViewModel
import com.hackhathon.mentalhealthapp.techniqueSelectionScreen.TechniqueSelectionScreen
import com.hackhathon.mentalhealthapp.techniqueSelectionScreen.TechniqueSelectionScreenViewModel
import com.hackhathon.mentalhealthapp.chatGptScreen.GptViewModel
import com.hackhathon.mentalhealthapp.mainScreen.MainScreen
import com.hackhathon.mentalhealthapp.mainScreen.MainScreenViewModel
import com.hackhathon.mentalhealthapp.profileScreen.ProfileScreen
import com.hackhathon.mentalhealthapp.profileScreen.ProfileViewModel

@Composable
fun ScreenWithBottomNav(
    onNavigateToChat: () -> Unit,
    onNavigateToBreathing: () -> Unit,
    gptViewModel: GptViewModel,
    breathingViewModel: BreathingViewModel,
    noteViewModel: NoteViewModel,
    mainScreenViewModel: MainScreenViewModel,
    bottomNavViewModel: BottomNavViewModel,
    techniqueSelectionScreenViewModel : TechniqueSelectionScreenViewModel,
    profileViewModel: ProfileViewModel
) {
    val navController = rememberNavController()

    LaunchedEffect(navController) {
        bottomNavViewModel.setNavController(navController)
    }

    val isInitialized by bottomNavViewModel.isInitialized.collectAsState()

    if (isInitialized) {
        Scaffold(
            bottomBar = {
                BottomNavigationBar(bottomNavViewModel = bottomNavViewModel)
            },
            content = { padding ->
                NavHostContainer(
                    navController = navController,
                    padding = padding,
                    onNavigateToChat = onNavigateToChat,
                    onNavigateToBreathing = onNavigateToBreathing,
                    gptViewModel = gptViewModel,
                    noteViewModel = noteViewModel,
                    breathingViewModel = breathingViewModel,
                    mainScreenViewModel = mainScreenViewModel,
                    bottomNavViewModel = bottomNavViewModel,
                    techniqueSelectionScreenViewModel = techniqueSelectionScreenViewModel,
                    profileViewModel  = profileViewModel
                )
            }
        )
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun NavHostContainer(
    navController: NavHostController,
    padding: PaddingValues,
    onNavigateToChat: () -> Unit,
    onNavigateToBreathing: () -> Unit,
    gptViewModel: GptViewModel,
    breathingViewModel: BreathingViewModel,
    noteViewModel: NoteViewModel,
    mainScreenViewModel: MainScreenViewModel,
    bottomNavViewModel: BottomNavViewModel,
    techniqueSelectionScreenViewModel : TechniqueSelectionScreenViewModel,
    profileViewModel: ProfileViewModel
) {
    val currentRoute by bottomNavViewModel.currentRoute.collectAsState()

    NavHost(
        navController = navController,
        startDestination = currentRoute,
    ) {
        composable("home") {
            MainScreen(
                onNavigateToChat = onNavigateToChat,
                messageViewModel = gptViewModel,
                noteViewModel = noteViewModel,
                mainScreenViewModel = mainScreenViewModel,
                profileViewModel = profileViewModel
            )
        }

        composable("meditation") {
            TechniqueSelectionScreen(
                onNavigateToBreathing = onNavigateToBreathing,
                viewModel = breathingViewModel,
                techniqueSelectionScreenViewModel = techniqueSelectionScreenViewModel

            )
        }

        composable("profile") {
            ProfileScreen(viewModel = profileViewModel)
        }
    }
}

@Composable
fun BottomNavigationBar(
    bottomNavViewModel: BottomNavViewModel
) {
    val currentRoute by bottomNavViewModel.currentRoute.collectAsState()
    val systemNavigationBarPadding = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(bottom = systemNavigationBarPadding, top = 6.dp)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Constants.BottomNavItems.forEach { navItem ->
                BottomNavItem(
                    item = navItem,
                    isSelected = currentRoute == navItem.route,
                    onItemClick = { bottomNavViewModel.navigateTo(navItem.route) }
                )
            }
        }
    }
}

@Composable
fun BottomNavItem(
    item: BottomNavItem,
    isSelected: Boolean,
    onItemClick: () -> Unit
) {
    val contentColor = if (isSelected) Color.Black else Color.Gray

    Column(
        modifier = Modifier
            .width(80.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onItemClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(item.icon),
            contentDescription = item.label,
            modifier = Modifier.size(24.dp),
            tint = contentColor
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = item.label,
            fontFamily = FontFamily(Font(resId = R.font.inter_semi_bold, weight = FontWeight.Medium)),
            fontSize = 12.sp,
            color = contentColor,
            maxLines = 1
        )
    }
}