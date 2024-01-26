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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.cc221045.mathemelloccl3.ui.theme.AnimatedButton
import com.cc221045.mathemelloccl3.ui.theme.ImagePickerButton
import com.cc221045.mathemelloccl3.ui.theme.appFontFamily
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
            text = "Create Post",
            style = TextStyle(
                fontFamily = appFontFamily, // or any other font family you want
                fontWeight = FontWeight.ExtraBold, // choose the desired weight
                fontSize = 32.sp // set the font size as needed
            ),
            modifier = Modifier.padding(bottom = 16.dp),
            color = textColor,
        )

        // Title TextField
        OutlinedTextField(
            value = title,
            shape = RoundedCornerShape(cornerRadius),
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
            shape = RoundedCornerShape(cornerRadius),
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



        Button(
            onClick = {
                if (title.isNotBlank() && content.isNotBlank()) {
                    val imageUriString = imageUri?.toString() // Convert URI to String safely
                    viewModel.addPost(title, content, imageUriString ?: "") // Use empty string if null
                    viewModel.clearImageUri()

                    navController.navigate("postsList")
                } else {
                    showError = true
                }
            },
            modifier = Modifier

                .shadow(4.dp, shape = RoundedCornerShape(8.dp))
                .fillMaxWidth()
                .height(50.dp),

            shape = RoundedCornerShape(cornerRadiusBtn),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(4280626236), // Set the button background color
                contentColor = onBackgroundColor),
        ) {
            Text("Submit", fontSize = 24.sp)
        }

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




