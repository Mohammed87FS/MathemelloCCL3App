
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cc221045.mathemelloccl3.Screen
import com.cc221045.mathemelloccl3.ui.theme.MainViewModel


@Composable
fun LoginScreen(viewModel: MainViewModel, navController: NavController) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    var isLoading by remember { mutableStateOf(false) }

    // Simple email and password validation
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


    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Login", style = MaterialTheme.typography.h4)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation())
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            viewModel.loginUser(email, password) { isSuccess, isAdmin ->
                if (isSuccess) {
                    if (isAdmin) {
                        // Navigate to admin-specific screen
                        navController.navigate(Screen.CreatePost.route)
                    } else {
                        // Navigate to regular user screen
                        navController.navigate(Screen.PostsList.route)
                    }
                } else {
                        error = "Login failed"
                    }
                }

        }) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else {
                Text(text = "Login")
            }
        }


        error?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = it, color = MaterialTheme.colors.error)
        }

        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = { navController.navigate(Screen.SignUp.route) }) {
            Text("Don't have an account? Sign up")
        }
    }
}

@Composable
fun SettingsScreen(viewModel: MainViewModel, navController: NavController) {
    // Layout for settings
    Column(modifier = Modifier.padding(16.dp)) {
        //... other settings options ...

        Button(onClick = {
            viewModel.logout()
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.PostsList.route) { inclusive = true }
            }
        }) {
            Text("Logout")
        }
    }
}

