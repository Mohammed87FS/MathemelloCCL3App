package com.cc221045.mathemelloccl3.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel



import androidx.compose.foundation.layout.*
import com.cc221045.mathemelloccl3.ui.theme.AnimatedButton
import com.cc221045.mathemelloccl3.viewmodel.MainViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPostScreen(postId: String, viewModel: MainViewModel = viewModel(), onFinishedEditing: () -> Unit) {
    val post by viewModel.getPostById(postId).collectAsState(initial = null)

    var title by remember { mutableStateOf(post?.title ?: "") }
    var content by remember { mutableStateOf(post?.content ?: "") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Edit Post",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Content") },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 150.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        AnimatedButton(
            text = "Save Changes",
            onClick = {
                if (post != null) {
                    viewModel.updatePost(post!!.copy(title = title, content = content))
                    onFinishedEditing()
                }
            }
        )
    }
}
