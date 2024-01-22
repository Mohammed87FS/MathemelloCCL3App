package com.cc221045.mathemelloccl3.screens



import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.cc221045.mathemelloccl3.ui.theme.AnimatedButton
import com.cc221045.mathemelloccl3.ui.theme.ImagePickerButton
import com.cc221045.mathemelloccl3.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(
    viewModel: MainViewModel,
    navController: NavHostController,
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    val imageUri by viewModel.selectedImageUri.observeAsState()
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Create Post",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Title TextField
        OutlinedTextField(
            value = title,
            onValueChange = {
                title = it
                showError = it.isBlank() || content.isBlank()
            },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))


        OutlinedTextField(
            value = content,
            onValueChange = {
                content = it
                showError = title.isBlank() || it.isBlank()
            },
            label = { Text("Content") },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 150.dp)


        )
        Spacer(modifier = Modifier.height(16.dp))

        ImagePickerButton(
            text = "Pick Image",
            onImagePicked = { uri ->
                if (uri != null) {

                    viewModel.setImageUri(context, uri)
                }
            }
        )


        if (showError) {
            Text("Please fill in both title and content.", color = Color.Red)
        }

        Spacer(modifier = Modifier.height(16.dp))

        AnimatedButton(
            text = "Post",
            onClick = {
                if (title.isNotBlank() && content.isNotBlank()) {
                    val imageUriString = imageUri?.toString() // Convert URI to String safely
                    viewModel.addPost(title, content, imageUriString ?: "") // Use empty string if null
                    viewModel.clearImageUri()

                    navController.navigate("postsList")
                } else {
                    showError = true
                }
            }
        )

        imageUri?.let { uri ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = "Selected Image",
                    modifier = Modifier
                        .height(150.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .aspectRatio(1f),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}




