package com.cc221045.mathemelloccl3.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cc221045.mathemelloccl3.data.Post
import com.cc221045.mathemelloccl3.ui.theme.AnimatedButton
import com.cc221045.mathemelloccl3.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPostScreen(
    postId: String,
    viewModel: MainViewModel = viewModel(),
    onFinishedEditing: () -> Unit,
) {
    var currentPost by remember { mutableStateOf<Post?>(null) }
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    var posts by remember { mutableStateOf(listOf<Post>()) }

    LaunchedEffect(postId) {
        currentPost = viewModel.getPostById(postId)
        currentPost?.let {
            title = it.title
            content = it.content
        }
        isLoading = false
    }

    LaunchedEffect(Unit) {
        // Fetch and observe the latest posts
        posts = viewModel.getAllPosts() // Assuming `getAllPosts` is a suspend function in your ViewModel
    }

    if (isLoading) {
        CircularProgressIndicator()
    } else {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
        ) {
            Text(
                text = "Edit Post",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp),
            )

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Content") },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .heightIn(min = 150.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            AnimatedButton(
                text = "Save Changes",
                onClick = {
                    currentPost?.let { post ->
                        val updatedPost = post.copy(title = title, content = content)
                        viewModel.updatePost(updatedPost)
                        onFinishedEditing()
                    }
                },
            )
        }
    }
}
