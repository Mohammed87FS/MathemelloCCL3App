package com.cc221045.mathemelloccl3.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.cc221045.mathemelloccl3.Screen
import com.cc221045.mathemelloccl3.ui.theme.appFontFamily
import com.cc221045.mathemelloccl3.viewmodel.MainViewModel

@Composable
fun LoginScreen(viewModel: MainViewModel, navController: NavController) {
    // Define the color palette based on the screenshot provided earlier
    val backgroundColor = Color(4279705391) // dark background
    val onBackgroundColor = Color(4284523665) // text color on the dark background
    val buttonBackgroundColor = Color(4280626236)// button color
    val simpleTextColor = Color(4284375919)
    val textFieldBorderColor = Color.Gray // text field border color
    val errorColor = Color.Red // error message color
    val cornerRadius = 10.dp // This can be adjusted to your desired corner radius
    val cornerRadiusBtn = 15.dp



    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    fun isFormValid(): Boolean {
        if (email.isBlank() || !email.contains("@")) {
            error = "Invalid email"
            return false
        }
        if (password.length < 6) {
            error = "Password must be at least 6 characters"
            return false
        }
        return true
    }

    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor) // Apply the background color here
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(backgroundColor), // Set the background color
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(), // Use the full width of the parent  // Add padding if needed
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Login",
                    color = onBackgroundColor,
                    modifier = Modifier.weight(1f), // This pushes the text to the left
                    style = TextStyle(
                        fontFamily = appFontFamily, // or any other font family you want
                        fontWeight = FontWeight.ExtraBold, // choose the desired weight
                        fontSize = 32.sp // set the font size as needed
                    ),                )
                // If there's more content that should be on the right, add it here.
            }
            Spacer(modifier = Modifier.height(5.dp))
            OutlinedTextField(
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth(), // Make sure it fills the available width

                value = email,
                onValueChange = { email = it },
                label = { Text("Email",
                    color = simpleTextColor,
                    fontFamily = appFontFamily,
                    fontWeight = FontWeight.Medium) },
                textStyle = TextStyle(color = simpleTextColor),
                singleLine = true,
                shape = RoundedCornerShape(cornerRadius), // Set the shape of the text field
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Email, // This is a placeholder icon, use the appropriate one
                        contentDescription = "Password",
                        tint = simpleTextColor
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = onBackgroundColor,
                    focusedBorderColor = textFieldBorderColor,
                    unfocusedBorderColor = textFieldBorderColor,
                    backgroundColor = backgroundColor // Set the background color to match the app's theme
                )
            )
            Spacer(modifier = Modifier.height(5.dp))


            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password",
                    color = simpleTextColor,
                    fontFamily = appFontFamily,
                    fontWeight = FontWeight.Medium) },
                textStyle = TextStyle(color = simpleTextColor),
                visualTransformation = PasswordVisualTransformation(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Lock, // This is a placeholder icon, use the appropriate one
                        contentDescription = "Password",
                        tint = simpleTextColor
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = onBackgroundColor,
                    focusedBorderColor = textFieldBorderColor,
                    unfocusedBorderColor = textFieldBorderColor,
                    backgroundColor = Color.Transparent // Set the background color of the TextField to transparent
                ),
                shape = RoundedCornerShape(cornerRadius), // Set the corner shape to have rounded corners
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    if(isFormValid()) {
                        isLoading = true
                        viewModel.loginUser(email, password) { isSuccess, isAdmin ->
                            isLoading = false
                            if (isSuccess) {
                                if (isAdmin) {
                                    navController.navigate(Screen.CreatePost.route)
                                } else {
                                    navController.navigate(Screen.PostsList.route)
                                }
                            } else {
                                error = "Login failed"
                            }
                        }
                    }
                },
                modifier = Modifier
                    .shadow(4.dp, shape = RoundedCornerShape(8.dp))
                    .fillMaxWidth() // Fills the max width of the parent, you can adjust this value as needed
                    .height(50.dp), // Sets the height of the button, adjust this value as needed
                colors = ButtonDefaults.buttonColors(backgroundColor = buttonBackgroundColor),
                shape = RoundedCornerShape(cornerRadiusBtn),

            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text(
                        text = "Login",
                        color = onBackgroundColor,
                        fontSize = 24.sp
                    )
                }
            }

            error?.let {

                Text(text = it, color = errorColor)
            }

            Spacer(modifier = Modifier.height(15.dp))

            TextButton(onClick = { navController.navigate(Screen.SignUp.route) }) {
                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(color = simpleTextColor,
                            fontFamily = appFontFamily,
                            fontWeight = FontWeight.Medium, fontSize = 10.sp)) {
                            append("Don't have an account? ")
                        }
                        withStyle(style = SpanStyle(color = onBackgroundColor,
                            fontFamily = appFontFamily,
                            fontWeight = FontWeight.ExtraBold, fontSize = 10.sp)) {
                            append("Sign Up")
                        }
                    }
                )
            }
        }
    }
}
