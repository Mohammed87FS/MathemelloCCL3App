package com.cc221045.mathemelloccl3.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cc221045.mathemelloccl3.viewmodel.MainViewModel

@Composable
fun LikedPostsScreen(viewModel: MainViewModel, navController: NavHostController,userEmail:String) {
    val likedPosts by viewModel.getLikedPosts(userEmail).observeAsState(listOf())



    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Liked Posts",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (likedPosts.isEmpty()) {
            Text("No posts available", style = MaterialTheme.typography.bodyMedium)
        } else {
            LazyColumn {
                items(likedPosts) { likedPost ->
                    SimplePostItem(likedPost) // Update this composable to display LikedPost
                }
            }
        }
    }
}
