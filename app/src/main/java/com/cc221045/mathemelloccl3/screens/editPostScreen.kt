package com.cc221045.mathemelloccl3.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    val darkBackground = Color(0xFF2D303B) // Replace with the exact color from the screenshot
    val textColor = Color(0xFF60A491)
    val simpleTextColor = Color(0xFF9D9EA5)
    val buttonColor = Color(0xFF3C3F4A) // Replace with the exact button color from the screenshot
    val cornerRadius = 20.dp

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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(darkBackground) // Apply the background color here
        )
        Column(
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(
                text = "Edit Post",
                color = textColor,
                style = TextStyle(
                    fontFamily = FontFamily.Monospace, // or any other font family you want
                    fontWeight = FontWeight.ExtraBold, // choose the desired weight
                    fontSize = 32.sp // set the font size as needed
                ),
                modifier = Modifier.padding(bottom = 16.dp),
            )

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                shape = RoundedCornerShape(cornerRadius),
                modifier = Modifier.fillMaxWidth(),

                )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Content") },
                shape = RoundedCornerShape(cornerRadius),
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
