package com.cc221045.mathemelloccl3.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cc221045.mathemelloccl3.data.LikedPost
import com.cc221045.mathemelloccl3.data.Post
import com.cc221045.mathemelloccl3.ui.theme.AnimatedButton
import com.cc221045.mathemelloccl3.viewmodel.MainViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun PostsListScreen(
    userEmail: String,
    viewModel: MainViewModel,
    navController: NavHostController
) {

    LaunchedEffect(key1 = true) {
        viewModel.reloadPosts()
    }
    val posts by viewModel.posts.collectAsState()

    val userEmail = viewModel.userEmail

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Posts",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (posts.isEmpty()) {
            Text("No posts available", style = MaterialTheme.typography.bodyMedium)
        } else {
            if (viewModel.isAdmin) {
                LazyColumn {
                    items(posts) { post ->
                        AdminPostItem(post, viewModel, navController)
                    }
                }
            } else {
                LazyColumn {
                    items(posts) { post ->
                        UserPostItem(post, viewModel, navController)
                    }
                }
            }
        }
    }
}

@Composable
fun UserPostItem(post: Post, viewModel: MainViewModel, navController: NavHostController) {
    val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: ""
    var isLiked by remember { mutableStateOf(false) }

    LaunchedEffect(post.id, userEmail) {
       isLiked = viewModel.isPostLiked(post.id, userEmail)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.extraSmall
    ) {


        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = post.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = post.content,
                style = MaterialTheme.typography.bodyMedium
            )




            Spacer(modifier = Modifier.width(16.dp))


            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (!viewModel.isAdmin) {
                    IconButton(
                        onClick = {
                            viewModel.toggleLikePost(post, userEmail)
                        }
                    ) {
                        Icon(
                            imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = if (isLiked) "Unlike" else "Like"
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun AdminPostItem(post: Post, viewModel: MainViewModel, navController: NavHostController) {


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.extraSmall
    ) {


        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = post.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = post.content,
                style = MaterialTheme.typography.bodyMedium
            )




            Spacer(modifier = Modifier.width(16.dp))


            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {

                if (viewModel.isAdmin) {
                    AnimatedButton(
                        text = "Edit",
                        onClick = { navController.navigate("editPost/${post.id}") })
                    AnimatedButton(text = "Delete", onClick = { viewModel.deletePost(post) })
                }
            }
        }
    }
}

@Composable
fun SimplePostItem(likedPost: LikedPost) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = likedPost.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = likedPost.content,
                style = MaterialTheme.typography.bodyMedium
            )

        }
    }
}
