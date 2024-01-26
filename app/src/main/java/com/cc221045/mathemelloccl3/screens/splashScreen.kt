package com.cc221045.mathemelloccl3.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.cc221045.mathemelloccl3.Screen
import com.cc221045.mathemelloccl3.ui.theme.appFontFamily
import com.cc221045.mathemelloccl3.viewmodel.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay





@Composable
fun SplashScreen(navController: NavController, viewModel: MainViewModel) {

    val darkBackground = Color(4279705391)
    val textColor = Color(4284523665)
    LaunchedEffect(Unit) {


        delay(2000) // Display splash screen for 2 seconds

        val nextRoute = when {

            viewModel.auth.currentUser != null && FirebaseAuth.getInstance().currentUser?.email =="admin@admin.com" -> Screen.CreatePost.route
            viewModel.auth.currentUser != null -> Screen.PostsList.route
            else -> Screen.Login.route
        }

        navController.navigate(nextRoute) {
            popUpTo(Screen.Splash.route) { inclusive = true } // Clear splash screen from back stack
        }
    }

    Surface(modifier = Modifier
        .fillMaxSize()
        .background(darkBackground) ) {
        Box(contentAlignment = Alignment.Center,
                modifier = Modifier
                .fillMaxSize()
            .background(darkBackground)
        ) {
            Text(
                text = "Mathemello",

                style = TextStyle(
                    fontFamily = appFontFamily, // or any other font family you want
                    fontWeight = FontWeight.ExtraBold, // choose the desired weight
                    fontSize = 38.sp // // set the font size as needed
                ),
                modifier = Modifier.padding(bottom = 16.dp),
                color = textColor,
            )
        }
    }
}



