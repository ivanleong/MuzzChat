package com.example.muzzchat.ui.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun MainNavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = NavType.Main.name
    )
    {
        composable(NavType.Main.name) {
            MainScreen(onNavigateToChat = {
                navController.navigate(NavType.Chat.name)
            })
        }
        composable(NavType.Chat.name) {
            ChatScreen(onBackClicked = {
                navController.popBackStack()
            })
        }
    }

}

enum class NavType() {
    Main, Chat
}
