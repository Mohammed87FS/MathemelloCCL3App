package com.cc221045.mathemelloccl3.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.cc221045.mathemelloccl3.Screen
import com.cc221045.mathemelloccl3.ui.theme.appFontFamily
import com.cc221045.mathemelloccl3.viewmodel.MainViewModel
import com.google.firebase.auth.FirebaseAuth


@Composable
fun ProfileScreen(viewModel: MainViewModel, navController: NavController,isAdmin: Boolean) {

    val darkBackground = Color(4279705391) // Replace with the exact color from the screenshot
    val onBackgroundColor = Color(4284523665)
    val textColor = Color(0xFF60A491)
    val simpleTextColor = Color(0xFF9D9EA5)
    val buttonColor = Color(0xFF3C3F4A) // Replace with the exact button color from the screenshot


    Scaffold(

    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(darkBackground)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(8.dp))

            // User Email
            Text("${FirebaseAuth.getInstance().currentUser?.email }",
                color = onBackgroundColor,
                fontFamily = appFontFamily, // or any other font family you want
                fontWeight = FontWeight.ExtraBold, // choose the desired weight
                fontSize = 32.sp)

            Spacer(Modifier.height(24.dp))

            // Buttons
            ProfileButton("Logout") {
                viewModel.logout()
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.PostsList.route) { inclusive = true }
                }
            }
            Spacer(Modifier.height(24.dp))
            ProfileButton("My Requests", onClick = { /* Navigate to requests */ })
            Spacer(Modifier.height(24.dp))
            ProfileButton("Liked Posts", onClick = { /* Navigate to liked posts */ })
        }
    }

}



@Composable
fun ProfileButton(text: String, onClick: () -> Unit) {
    val buttonColor = Color(4280626236)
    val textColor = Color(0xFF60A491)
    val cornerRadius = 15.dp
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            ,
        colors = ButtonDefaults.buttonColors(backgroundColor = buttonColor),
        shape = RoundedCornerShape(cornerRadius)
    ) {
        Text(text, color = textColor,
            fontSize = 24.sp)
    }
}