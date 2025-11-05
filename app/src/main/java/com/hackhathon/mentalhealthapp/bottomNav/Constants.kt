package com.hackhathon.mentalhealthapp.bottomNav

import com.hackhathon.features.R

object Constants {
    val BottomNavItems = listOf(
        // Home screen
        BottomNavItem(
            label = "Главная",
            icon = R.drawable.ic_home,
            route = "home"
        ),
        // Search screen
        BottomNavItem(
            label = "Медитации",
            icon = R.drawable.ic_meditation,
            route = "Meditation"
        ),
        // Profile screen
        BottomNavItem(
            label = "Профиль",
            icon = R.drawable.ic_user,
            route = "profile"
        )
    )
}