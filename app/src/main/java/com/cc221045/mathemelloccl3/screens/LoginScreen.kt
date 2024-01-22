package com.cc221045.mathemelloccl3.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cc221045.mathemelloccl3.Screen
import com.cc221045.mathemelloccl3.viewmodel.MainViewModel

@Composable
fun LoginScreen(viewModel: MainViewModel, navController: NavController) {
    // Define the color palette based on the screenshot provided earlier
    val backgroundColor = Color(0xFF1A2135) // dark background
    val onBackgroundColor = Color(0xFF008377) // text color on the dark background
    val buttonBackgroundColor = Color(0xFF1E263A)// button color
    val textFieldBorderColor = Color.Gray // text field border color
    val errorColor = Color.Red // error message color

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(backgroundColor), // Set the background color
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Login",
                style = MaterialTheme.typography.h4.copy(color = onBackgroundColor)
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email", color = onBackgroundColor) },
                textStyle = TextStyle(color = onBackgroundColor),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = onBackgroundColor,
                    focusedBorderColor = textFieldBorderColor,
                    unfocusedBorderColor = textFieldBorderColor
                )
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password", color = onBackgroundColor) },
                textStyle = TextStyle(color = onBackgroundColor),
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = onBackgroundColor,
                    focusedBorderColor = textFieldBorderColor,
                    unfocusedBorderColor = textFieldBorderColor,

                )
            )
            Spacer(modifier = Modifier.height(16.dp))

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
                colors = ButtonDefaults.buttonColors(backgroundColor = buttonBackgroundColor)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text(text = "Login", color = onBackgroundColor)
                }
            }

            error?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = it, color = errorColor)
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = { navController.navigate(Screen.SignUp.route) }) {
                Text("Don't have an account? Signup", color = onBackgroundColor)
            }
        }
    }
}
