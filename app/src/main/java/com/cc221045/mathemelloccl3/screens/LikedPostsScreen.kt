package com.cc221045.mathemelloccl3.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cc221045.mathemelloccl3.viewmodel.MainViewModel

@Composable
 fun LikedPostsScreen(viewModel: MainViewModel, navController: NavHostController) {
    val userEmail by viewModel.userEmail.observeAsState()
    val likedPosts = viewModel.getLikedPostsLiveData(userEmail ?: "").observeAsState(initial = listOf()).value

    LaunchedEffect(likedPosts) {
        Log.d("LikedPostsScreen", "Liked posts updated: $likedPosts")
    }


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
                    SimplePostItem(likedPost)
                }
            }
        }
    }
}
