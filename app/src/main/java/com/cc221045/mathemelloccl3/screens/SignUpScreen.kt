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
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SignUpScreen(viewModel: MainViewModel, navController: NavController, auth: FirebaseAuth) {
    // Custom color definitions based on your design
    val darkBackground = Color(4279705391) // Replace with the exact color from the screenshot
    val textColor = Color(0xFF60A491)
    val simpleTextColor = Color(4284375919)
    val buttonColor = Color(4280626236) // Replace with the exact button color from the screenshot
    val errorColor = Color.Red // Or any specific shade of red you want for error messages
    val textFieldColor = Color.Gray // Replace with the exact text field color from the screenshot
    val cornerRadius = 10.dp
    val cornerRadiusBtn = 15.dp

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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(darkBackground) // Apply the background color here
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(darkBackground), // Apply the background color
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(), // Use the full width of the parent  // Add padding if needed
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Sign Up",
                    color = textColor,
                    fontSize = 32.sp,
                    style = TextStyle(
                        fontFamily = appFontFamily, // or any other font family you want
                        fontWeight = FontWeight.ExtraBold, // choose the desired weight
                        fontSize = 32.sp // set the font size as needed
                    ),
                    modifier = Modifier.weight(1f) // This pushes the text to the left
                ) }
            Spacer(modifier = Modifier.height(5.dp))

            OutlinedTextField(
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth(),
                value = email,
                onValueChange = { email = it },
                label = { Text("Email", color = simpleTextColor,
                    fontFamily = appFontFamily,
                    fontWeight = FontWeight.Medium) },
                textStyle = TextStyle(color = simpleTextColor),
                shape = RoundedCornerShape(cornerRadius),
                leadingIcon = {
                    Icon(Icons.Filled.Email, contentDescription = "Email Icon", tint = simpleTextColor)
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = textColor,
                    focusedBorderColor = textFieldColor,
                    unfocusedBorderColor = textFieldColor,

                    )
            )

            OutlinedTextField(
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth(),
                value = password,
                onValueChange = { password = it },
                label = { Text("Password", color = simpleTextColor,
                    fontFamily = appFontFamily,
                    fontWeight = FontWeight.Medium) },
                textStyle = TextStyle(color = simpleTextColor),
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(cornerRadius),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Lock, // This is a placeholder icon, use the appropriate one
                        contentDescription = "Password",
                        tint = simpleTextColor
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = textColor,
                    focusedBorderColor = textFieldColor,
                    unfocusedBorderColor = textFieldColor
                )
            )

            OutlinedTextField(
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth(),
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password", color = simpleTextColor,
                    fontFamily = appFontFamily,
                    fontWeight = FontWeight.Medium) },
                textStyle = TextStyle(color = simpleTextColor),
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(cornerRadius),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Lock, // This is a placeholder icon, use the appropriate one
                        contentDescription = "Password",
                        tint = simpleTextColor
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = textColor,
                    focusedBorderColor = textFieldColor,
                    unfocusedBorderColor = textFieldColor
                )
            )

            Spacer(modifier = Modifier.height(30.dp))

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
                modifier = Modifier
                    .fillMaxWidth() // Fills the max width of the parent, you can adjust this value as needed
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = buttonColor),
                shape = RoundedCornerShape(cornerRadiusBtn)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text(text = "Sign Up", color = textColor, fontSize = 24.sp)
                }
            }

            error?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = it, color = errorColor)
            }

            Spacer(modifier = Modifier.height(15.dp))

            TextButton(onClick = { navController.navigate(Screen.Login.route) }) {
                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color.Gray, fontSize = 14.sp)) {
                            append("Already have an account? ")
                        }
                        withStyle(style = SpanStyle(color = textColor, fontFamily = appFontFamily, // or any other font family you want
                            fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)) {
                            append("Login")
                        }
                    }
                )
            }
        }
    }
}
