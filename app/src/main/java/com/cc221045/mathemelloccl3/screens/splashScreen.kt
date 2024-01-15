package com.cc221045.mathemelloccl3.screens


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.cc221045.mathemelloccl3.Screen
import com.cc221045.mathemelloccl3.viewmodel.MainViewModel
import kotlinx.coroutines.delay





@Composable
fun SplashScreen(navController: NavController, viewModel: MainViewModel) {
    LaunchedEffect(Unit) {


        delay(2000) // Display splash screen for 2 seconds

        val nextRoute = when {
            viewModel.auth.currentUser != null && viewModel.isAdmin -> Screen.CreatePost.route
            viewModel.auth.currentUser != null -> Screen.PostsList.route
            else -> Screen.Login.route
        }

        navController.navigate(nextRoute) {
            popUpTo(Screen.Splash.route) { inclusive = true } // Clear splash screen from back stack
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = "Mathemello",
                style = MaterialTheme.typography.h2,
                color = MaterialTheme.colors.onBackground
            )
        }
    }
}



