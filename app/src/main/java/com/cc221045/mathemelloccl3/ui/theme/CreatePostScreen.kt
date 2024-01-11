package com.cc221045.mathemelloccl3.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.cc221045.mathemelloccl3.screens


import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon



import androidx.compose.foundation.layout.*

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(
    viewModel: MainViewModel,
    navController: NavHostController,
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

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


        if (showError) {
            Text("Please fill in both title and content.", color = Color.Red)
        }

        Spacer(modifier = Modifier.height(16.dp))


        AnimatedButton(
            text = "Post",
            onClick = {
                if (title.isNotBlank() && content.isNotBlank()) {
                    viewModel.addPost(title, content)
                    navController.navigate("postsList")
                } else {
                    showError = true
                }
            }
        )
    }
}




@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val currentRoute = getCurrentRoute(navController)

    NavigationBar {
        screens.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = null) },
                label = { Text(screen.label) },
                selected = currentRoute == screen.route,
                onClick = {
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route)
                    }
                }
            )
        }
    }
}

@Composable
fun getCurrentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}








@Composable
fun SplashScreen(onSplashComplete: () -> Unit) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = "Mathemello",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }

    LaunchedEffect(key1 = true) {
        delay(2000)
        onSplashComplete()
    }
}
