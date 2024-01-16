package com.cc221045.mathemelloccl3.screens

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.cc221045.mathemelloccl3.Screen
import com.cc221045.mathemelloccl3.screens
import com.cc221045.mathemelloccl3.viewmodel.MainViewModel






@Composable
fun BottomNavigationBar(navController: NavHostController, viewModel: MainViewModel) {
    val currentRoute = getCurrentRoute(navController)

    NavigationBar {
       screens.forEach { screen ->

            val shouldDisplay = when (screen) {
                Screen.CreatePost -> viewModel.isAdmin // Only for admin
                Screen.LikedPosts -> false // Only for regular users

                Screen.RequestsList -> viewModel.isAdmin // Only for regular users
                Screen.CreateRequest -> !viewModel.isAdmin // Only for regular users
                Screen.Login, Screen.SignUp,Screen.Splash -> false // Exclude these screens
                else -> true // All other screens are displayed for everyone
            }


            if (shouldDisplay) {
                NavigationBarItem(
                    icon = { Icon(screen.icon, contentDescription = null) },
                    label = { Text(screen.label) },
                    selected = currentRoute == screen.route,
                    onClick = {
                        if (currentRoute != screen.route) {
                            navController.navigate(screen.route)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun getCurrentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}
