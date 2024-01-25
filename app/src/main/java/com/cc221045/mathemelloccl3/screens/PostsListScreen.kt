package com.cc221045.mathemelloccl3.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.cc221045.mathemelloccl3.data.LikedPost
import com.cc221045.mathemelloccl3.data.Post
import com.cc221045.mathemelloccl3.ui.theme.AnimatedButton
import com.cc221045.mathemelloccl3.viewmodel.MainViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostsListScreen(
    userEmail: String,
    viewModel: MainViewModel,
    navController: NavHostController,
) {




    LaunchedEffect(Unit) {
        viewModel.reloadPosts()
    }
    val posts = viewModel.posts
    var searchQuery by remember { mutableStateOf("") }

    val darkBackground = Color(0xFF2D303B) // Replace with the exact color from the screenshot
    val textColor = Color(0xFF60A491)
    val simpleTextColor = Color(0xFF9D9EA5)
    val cornerRadius = 20.dp



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(darkBackground) // Apply the background color here
    )
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Posts",
            color = textColor,
            style = TextStyle(
                fontFamily = FontFamily.Monospace, // or any other font family you want
                fontWeight = FontWeight.ExtraBold, // choose the desired weight
                fontSize = 32.sp // set the font size as needed
            ),
            modifier = Modifier.padding(bottom = 16.dp),
        )

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            shape = RoundedCornerShape(cornerRadius),
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search posts") },
            trailingIcon = { // This is the change
                Icon(
                    imageVector = Icons.Filled.Search, // Assuming you have a Search icon in your material icons
                    contentDescription = "Search",
                    modifier = Modifier.clickable { /* Icon click logic here */ }
                )
            },
            modifier = Modifier
                .fillMaxWidth(),
        )

        val filteredPosts = posts.filter {
            searchQuery.isEmpty() || it.title.contains(searchQuery, ignoreCase = true) || it.content.contains(searchQuery, ignoreCase = true)
        }

        Column(modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)) {


            if (filteredPosts.isEmpty()) {
                Text("No posts available", style = MaterialTheme.typography.bodyMedium)
            } else {

                LazyColumn {
                    items(filteredPosts) { post ->

                        if (FirebaseAuth.getInstance().currentUser?.email =="admin@admin.com" ) {
                            AdminPostItem(post, viewModel, navController)
                        } else {

                            UserPostItem(post, viewModel, navController,
                                onLikeClicked = { viewModel.reloadPosts() } )

                        }
                    }
                }
            }}}}

@Composable
fun AdminPostItem(
    post: Post,
    viewModel: MainViewModel,
    navController: NavHostController,
) {
    val imagePainter = rememberAsyncImagePainter(model = post.imageUrl)
    val buttonColor = Color(0xFF3C3F4A)
    Card(
        modifier =
        Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.extraSmall,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = post.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = post.content,
                style = MaterialTheme.typography.bodyMedium,
            )


            Spacer(modifier = Modifier.width(16.dp))
// Image
            post.imageUrl?.let { imageUrl ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    shape = RoundedCornerShape(8.dp), // Define the shape of the card
                    colors = CardDefaults.cardColors(containerColor = buttonColor,
                        contentColor = buttonColor), // Set the card background color here
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Image(
                        painter = imagePainter,
                        contentDescription = "Selected Image",
                        modifier = Modifier
                            .height(150.dp) // Set a fixed height
                            .fillMaxWidth() // Ensure it fills the width of the card
                            .clip(RoundedCornerShape(8.dp)) // Clip the image to fit the card shape
                            .aspectRatio(1f), // Maintain aspect ratio
                        contentScale = ContentScale.Crop // Crop the image to fit the dimensions
                    )

                    if (imagePainter.state is AsyncImagePainter.State.Error) {
                        val errorState = imagePainter.state as AsyncImagePainter.State.Error
                        val throwable = errorState.result.throwable
                        Log.e("PostItem", "Error loading image: ${throwable.message}")
                    }
                    Spacer(modifier = Modifier.width(16.dp)) // Add space between image and text

                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        if (viewModel.isAdmin) {
                            AnimatedButton(
                                text = "Edit",
                                onClick = { navController.navigate("editPost/${post.id}") },
                            )
                            AnimatedButton(text = "Delete", onClick = { viewModel.deletePost(post) })
                        }
                    }
                }
            }
        }}}




@Composable
fun SimplePostItem(likedPost: LikedPost) {
    Card(
        modifier =
        Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF3C3F4A)) // Set the card background color here
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = likedPost.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = likedPost.content,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}


@Composable
fun UserPostItem(
    post: Post,
    viewModel: MainViewModel,
    navController: NavHostController,
    onLikeClicked: () -> Unit
) {
    // This painter will handle the image loading
    val imagePainter = post.imageUrl?.let { imageUrl ->
        rememberAsyncImagePainter(model = imageUrl)
    }


    val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: ""

    var isLiked by remember { mutableStateOf(false) }


    LaunchedEffect(post.id, userEmail) {
        isLiked = viewModel.isPostLiked(post.id, userEmail)
        Log.d("AdminRequestItempp", "LaunchedEffect triggered for requestId: ${post.id}, isChecked: $isLiked")
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.extraSmall,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = post.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = post.content,
                style = MaterialTheme.typography.bodyMedium,
            )

            // Conditionally display the image if the URL is available
            imagePainter?.let {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Image(
                        painter = it,
                        contentDescription = "Post Image",
                        modifier = Modifier
                            .height(150.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .aspectRatio(1f),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            // Like button and functionality
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (!viewModel.isAdmin) {
                    IconButton(
                        onClick = {
                            isLiked = !isLiked
                            viewModel.toggleLikePost(post, userEmail)
                            onLikeClicked()
                        },
                    ) {
                        Icon(
                            imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = if (isLiked) "Unlike" else "Like",
                            tint = if (isLiked) Color(0xFF60A491) else Color.Gray
                        )
                    }
                }
            }
        }
    }
}



