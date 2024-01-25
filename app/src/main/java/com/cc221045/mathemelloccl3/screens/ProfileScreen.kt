package com.cc221045.mathemelloccl3.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
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
     // Replace with the exact button color from the screenshot color from the screenshot
    val buttonColor = Color(4280626236)
    val cornerRadius = 15.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(darkBackground) // Apply the background color here
    )
    Column(modifier = Modifier
        .padding(16.dp)

        .fillMaxSize()

        .background(darkBackground)
        .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        if ( FirebaseAuth.getInstance().currentUser?.email !=null  ){

            Text("${FirebaseAuth.getInstance().currentUser?.email }",color = onBackgroundColor,
                fontFamily = appFontFamily, // or any other font family you want
                fontWeight = FontWeight.ExtraBold, // choose the desired weight
                fontSize = 32.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = buttonColor),
                shape = RoundedCornerShape(cornerRadius),
                onClick = {

                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.PostsList.route) { inclusive = true }
                    }
                    viewModel.logout()
                }) {
                Text("Logout", color = textColor, fontSize = 18.sp)}


        }
        if (FirebaseAuth.getInstance().currentUser?.email !="admin@admin.com"&& FirebaseAuth.getInstance().currentUser?.email !=null  ){
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = buttonColor),
                shape = RoundedCornerShape(cornerRadius),
                onClick = {

                    navController.navigate(Screen.RequestsList.route) {
                        popUpTo(Screen.PostsList.route) { inclusive = true }
                    }
                }) {
                Text("My requests", color = textColor, fontSize = 18.sp)
            }}

        if (FirebaseAuth.getInstance().currentUser?.email =="admin@admin.com" ){
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = buttonColor),
                shape = RoundedCornerShape(cornerRadius),
                onClick = {navController.navigate(Screen.PostsList.route) {
                    popUpTo(Screen.RequestsList.route) { inclusive = true }
                }
                }) {
                Text("Posts", color = textColor, fontSize = 18.sp)
            }}
        if (FirebaseAuth.getInstance().currentUser?.email !="admin@admin.com"&& FirebaseAuth.getInstance().currentUser?.email !=null  ){
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                colors = ButtonDefaults.buttonColors(backgroundColor = buttonColor),
                shape = RoundedCornerShape(cornerRadius),
                onClick = {

                    navController.navigate(Screen.LikedPosts.route) {
                        popUpTo(Screen.PostsList.route) { inclusive = true }
                    }
                }) {
                Text("Liked Posts", color = textColor, fontSize = 18.sp)
            }}


    }

}