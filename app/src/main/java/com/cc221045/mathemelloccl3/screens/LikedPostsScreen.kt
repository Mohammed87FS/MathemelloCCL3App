package com.cc221045.mathemelloccl3.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.cc221045.mathemelloccl3.data.LikedPost
import com.cc221045.mathemelloccl3.viewmodel.MainViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LikedPostsScreen(viewModel: MainViewModel, navController: NavHostController) {
    val userEmail = viewModel.userEmail
    var likedPosts by remember { mutableStateOf(listOf<LikedPost>()) }
    var searchQuery by remember { mutableStateOf("") }
    var filteredPosts by remember { mutableStateOf(listOf<LikedPost>()) }

    val darkBackground = Color(0xFF2D303B) // Replace with the exact color from the screenshot
    val textColor = Color(0xFF60A491)
    val simpleTextColor = Color(0xFF9D9EA5)
    val buttonColor = Color(0xFF3C3F4A) // Replace with the exact button color from the screenshot
    val cornerRadius = 20.dp

    LaunchedEffect(userEmail) {
        likedPosts = viewModel.getLikedPosts(userEmail)
        filteredPosts = likedPosts
    }

    LaunchedEffect(searchQuery) {
        filteredPosts = if (searchQuery.isEmpty()) {
            likedPosts
        } else {
            likedPosts.filter {
                it.title.contains(searchQuery, ignoreCase = true) ||
                        it.content.contains(searchQuery, ignoreCase = true)
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(darkBackground) // Apply the background color here
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Liked Posts",
            style = TextStyle(
                fontFamily = FontFamily.Monospace, // or any other font family you want
                fontWeight = FontWeight.ExtraBold, // choose the desired weight
                fontSize = 32.sp // set the font size as needed
            ),
            modifier = Modifier.padding(bottom = 16.dp),
            color = textColor
        )

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            shape = RoundedCornerShape(cornerRadius),
            placeholder = { Text("Search posts") },
            trailingIcon = { // This is the change
                Icon(
                    imageVector = Icons.Filled.Search, // Assuming you have a Search icon in your material icons
                    contentDescription = "Search",
                    modifier = Modifier.clickable { /* Icon click logic here */ }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),

            )

        if (filteredPosts.isEmpty()) {
            Text("No posts available", style = MaterialTheme.typography.bodyMedium)
        } else {
            LazyColumn {
                items(filteredPosts) { likedPost ->
                    SimplePostItem(likedPost)
                }
            }
        }
    }
}
