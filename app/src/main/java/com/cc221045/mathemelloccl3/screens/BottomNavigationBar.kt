package com.cc221045.mathemelloccl3.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.cc221045.mathemelloccl3.Screen
import com.cc221045.mathemelloccl3.screens
import com.cc221045.mathemelloccl3.viewmodel.MainViewModel
import com.google.firebase.auth.FirebaseAuth


@Composable
fun BottomNavigationBar(navController: NavHostController, viewModel: MainViewModel) {
    val currentRoute = getCurrentRoute(navController)
    val darkBackground = Color(0xFF4A4553) // Replace with the exact color from the screenshot
    val textColor = Color(0xFF60A491)
    val simpleTextColor = Color(0xFF9D9EA5)
    val simpleiconColor = Color(0xFFE9E9E9)
    val buttonColor = Color(0xFF363942) // Replace with the exact button color from the screenshot


    NavigationBar(
        containerColor = buttonColor,
        contentColor = textColor
    ) {



        screens.forEach { screen ->

            val shouldDisplay = when (screen) {
                Screen.CreatePost -> FirebaseAuth.getInstance().currentUser?.email =="admin@admin.com"  // Only for admin
                Screen.LikedPosts -> false // Only for regular users

                Screen.RequestsList -> FirebaseAuth.getInstance().currentUser?.email =="admin@admin.com"
                Screen.PostsList -> FirebaseAuth.getInstance().currentUser?.email !="admin@admin.com"
                Screen.CreateRequest -> FirebaseAuth.getInstance().currentUser?.email !="admin@admin.com"  // Only for regular users
                Screen.Login, Screen.SignUp,Screen.Splash -> false // Exclude these screens
                else -> true // All other screens are displayed for everyone
            }


            if (shouldDisplay) {
                NavigationBarItem(
                    icon = { Icon(screen.icon, contentDescription = null) },
                    label = { Text(screen.label) },
                    selected = currentRoute == screen.route,
                    modifier = Modifier
                      
                        .background(darkBackground),
                            colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = simpleiconColor, // Set unselected item icon color
                        unselectedTextColor = simpleiconColor, // Set unselected item text color
                        selectedIconColor = textColor, // Set selected item icon color
                        selectedTextColor = textColor, // Set selected item text color
                    ),
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
