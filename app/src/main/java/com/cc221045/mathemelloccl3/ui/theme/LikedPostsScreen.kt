package com.cc221045.mathemelloccl3.ui.theme

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

@Composable
fun LikedPostsScreen(viewModel: MainViewModel, navController: NavHostController) {
    val likedPosts by viewModel.likedPosts.observeAsState(listOf())



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
                items(likedPosts) { post ->
                    SimplePostItem(post)
                }
            }
        }
    }
}
