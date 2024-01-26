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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.cc221045.mathemelloccl3.ui.theme.appFontFamily
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




    val buttonColor = Color(0xFF3C3F4A) // Replace with the exact button color from the screenshot
    val errorColor = Color.Red // Or any specific shade of red you want for error messages
    val textFieldColor = Color.Gray // Replace with the exact text field color from the screenshot

    val onBackgroundColor = Color(4284523665)

    val textColor = Color(4284523665)
    val simpleTextColor = Color(0xFF9D9EA5)
    val darkBackground = Color(4279705391)
    val buttonBackgroundColor = Color(0xFF3C3F4A)// button color
    val cornerRadius = 10.dp
    val cornerRadiusBtn = 15.dp

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
                style = TextStyle(
                    fontFamily = appFontFamily, // or any other font family you want
                    fontWeight = FontWeight.ExtraBold, // choose the desired weight
                    fontSize = 32.sp // set the font size as needed
                ),
                modifier = Modifier.padding(bottom = 16.dp),
                color = textColor,
            )

            OutlinedTextField(




                value = title,
                shape = RoundedCornerShape(cornerRadius),
                onValueChange = {
                    title = it

                },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()

                )

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = content,
                shape = RoundedCornerShape(cornerRadius),
                onValueChange = {
                    content = it

                },
                label = { Text("Content") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 150.dp)


            )

            Spacer(modifier = Modifier.height(16.dp))


            Button(
                onClick = {
                    currentPost?.let { post ->
                        val updatedPost = post.copy(title = title, content = content)
                        viewModel.updatePost(updatedPost)
                        onFinishedEditing()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(cornerRadiusBtn),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(4280626236), // Set the button background color
                    contentColor = onBackgroundColor),
            ) {
                Text("Update", fontSize = 24.sp)
            }


        }
    }
}
