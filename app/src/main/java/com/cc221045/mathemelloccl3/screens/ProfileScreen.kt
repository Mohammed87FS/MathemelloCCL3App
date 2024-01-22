package com.cc221045.mathemelloccl3.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cc221045.mathemelloccl3.Screen
import com.cc221045.mathemelloccl3.viewmodel.MainViewModel
import com.google.firebase.auth.FirebaseAuth


@Composable
fun ProfileScreen(viewModel: MainViewModel, navController: NavController,isAdmin: Boolean) {

    Column(modifier = Modifier.padding(16.dp)) {


        Button(onClick = {
            viewModel.logout()
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.PostsList.route) { inclusive = true }
            }
        }) {
            Text("Logout")
        }
        if (FirebaseAuth.getInstance().currentUser?.email !="admin@admin.com" ){
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {

            navController.navigate(Screen.RequestsList.route) {
                popUpTo(Screen.PostsList.route) { inclusive = true }
            }
        }) {
            Text("Requests")
        }}

        if (FirebaseAuth.getInstance().currentUser?.email =="admin@admin.com" ){
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {

                navController.navigate(Screen.PostsList.route) {
                    popUpTo(Screen.RequestsList.route) { inclusive = true }
                }
            }) {
                Text("Posts")
            }}
        if (FirebaseAuth.getInstance().currentUser?.email !="admin@admin.com" ){
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {

            navController.navigate(Screen.LikedPosts.route) {
                popUpTo(Screen.PostsList.route) { inclusive = true }
            }
        }) {
            Text("Saved Posts")
        }}
    }

}