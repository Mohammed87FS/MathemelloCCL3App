package com.cc221045.mathemelloccl3.screens
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.cc221045.mathemelloccl3.ui.theme.ImagePickerButton
import com.cc221045.mathemelloccl3.viewmodel.MainViewModel
import com.google.firebase.auth.FirebaseAuth


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateRequestScreen(viewModel: MainViewModel, userEmail: String, onSubmissionComplete: () -> Unit) {

    val onBackgroundColor = Color(0xFF2F6FA2)
    val simpleTextColor = Color(0xFF9D9EA5)
    val backgroundColor = Color(0xFF172041)
    val buttonBackgroundColor = Color(0xFF3C3F4A)// button color
    val cornerRadius = 20.dp

    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: ""
    val imageUri by viewModel.selectedImageUri.observeAsState()
    val context = LocalContext.current

    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor) // Apply the background color here
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(backgroundColor)
        ) {
            Text(
                "Create Request",
                color = onBackgroundColor,
                style = TextStyle(
                    fontFamily = FontFamily.Monospace, // or any other font family you want
                    fontWeight = FontWeight.ExtraBold, // choose the desired weight
                    fontSize = 32.sp // set the font size as needed
                ),
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title", color = simpleTextColor) },
                shape = RoundedCornerShape(cornerRadius),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))


            OutlinedTextField(
                value = content,
                onValueChange = {
                    content = it
                    showError = title.isBlank() || it.isBlank()
                },
                label = { Text("Content", color = simpleTextColor) },
                shape = RoundedCornerShape(cornerRadius),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 150.dp),

                )
            Spacer(modifier = Modifier.height(8.dp))


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

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (title.isNotBlank() && content.isNotBlank()) {
                        val imageUriString = imageUri?.toString()
                        viewModel.addRequest(userEmail, title, content, imageUriString ?: "")
                        viewModel.clearImageUri()
                        onSubmissionComplete()
                    } else {
                        showError = true
                    }
                },


                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3C3F4A), // Set the button background color
                    contentColor = onBackgroundColor),
            ) {
                Text("Submit", fontSize = 18.sp)
            }
            imageUri?.let { uri ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    shape = RoundedCornerShape(8.dp), // Define the shape of the card
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(uri),
                        contentDescription = "Selected Image",
                        modifier = Modifier
                            .height(150.dp) // Set a fixed height
                            .fillMaxWidth() // Ensure it fills the width of the card
                            .clip(RoundedCornerShape(8.dp)) // Clip the image to fit the card shape
                            .aspectRatio(1f), // Maintain aspect ratio
                        contentScale = ContentScale.Crop // Crop the image to fit the dimensions
                    )
                }
            }

            if (showError) {
                Text("Please fill in all fields", color = Color.Red)
            }
        }
    }
}

