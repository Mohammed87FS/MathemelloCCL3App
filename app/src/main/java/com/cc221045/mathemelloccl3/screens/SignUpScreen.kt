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
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SignUpScreen(viewModel: MainViewModel, navController: NavController, auth: FirebaseAuth) {
    // Custom color definitions based on your design
    val darkBackground = Color(0xFF121212) // Replace with the exact color from the screenshot
    val textColor = Color.White
    val buttonColor = Color(0xFF1EB980) // Replace with the exact button color from the screenshot
    val errorColor = Color.Red // Or any specific shade of red you want for error messages
    val textFieldColor = Color.Gray // Replace with the exact text field color from the screenshot

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    fun isFormValid(): Boolean {
        error = when {
            !email.contains("@") -> "Invalid email format"
            password.length < 6 -> "Password must be at least 6 characters"
            password != confirmPassword -> "Passwords don't match"
            else -> null
        }
        return error == null
    }

    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(darkBackground), // Apply the background color
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Sign Up",
                style = MaterialTheme.typography.h4.copy(color = textColor)
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email", color = textColor) },
                textStyle = TextStyle(color = textColor),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = textColor,
                    focusedBorderColor = textFieldColor,
                    unfocusedBorderColor = textFieldColor
                )
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password", color = textColor) },
                textStyle = TextStyle(color = textColor),
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = textColor,
                    focusedBorderColor = textFieldColor,
                    unfocusedBorderColor = textFieldColor
                )
            )

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password", color = textColor) },
                textStyle = TextStyle(color = textColor),
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = textColor,
                    focusedBorderColor = textFieldColor,
                    unfocusedBorderColor = textFieldColor
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (isFormValid()) {
                        isLoading = true
                        viewModel.registerUser(email, password) { success ->
                            isLoading = false
                            if (success) {
                                auth.signOut()
                                navController.navigate(Screen.Login.route) {
                                    popUpTo(Screen.Login.route) { inclusive = true }
                                }
                            } else {
                                error = "Registration failed"
                            }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = buttonColor)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text(text = "Sign Up", color = textColor)
                }
            }

            error?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = it, color = errorColor)
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = { navController.navigate(Screen.Login.route) }) {
                Text("Already have an account? Login", color = textColor)
            }
        }
    }
}
