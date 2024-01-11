package com.cc221045.mathemelloccl3

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.cc221045.mathemelloccl3.data.AppDatabase
import com.cc221045.mathemelloccl3.ui.theme.CreatePostScreen
import com.cc221045.mathemelloccl3.ui.theme.PostsListScreen
import com.cc221045.mathemelloccl3.ui.theme.KotloTheme

import com.cc221045.mathemelloccl3.ui.theme.MainViewModel
import com.cc221045.mathemelloccl3.ui.theme.MainViewModelFactory
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector
import com.cc221045.mathemelloccl3.ui.theme.BottomNavigationBar


import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.cc221045.mathemelloccl3.ui.theme.EditPostScreen
import com.cc221045.mathemelloccl3.ui.theme.LikedPostsScreen

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier

import com.cc221045.mathemelloccl3.ui.theme.*

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object CreatePost : Screen("createPost", "Create Post", Icons.Filled.Add)
    object PostsList : Screen("postsList", "Posts List", Icons.Filled.List)
    object LikedPosts : Screen("likedPosts", "Liked Posts", Icons.Filled.Favorite)

}

val screens = listOf(Screen.CreatePost, Screen.PostsList, Screen.LikedPosts) // Include LikedPosts


class MainActivity : ComponentActivity() {


    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "kotlin-db"
        ).fallbackToDestructiveMigration().build()


        val viewModelFactory = MainViewModelFactory(db.postDao())
        val viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]



        setContent {
            KotloTheme {
                val navController = rememberNavController()
                Scaffold(bottomBar = { BottomNavigationBar(navController) }) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "splashScreen",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("splashScreen") {
                            SplashScreen(onSplashComplete = {
                                navController.navigate(Screen.CreatePost.route) {
                                    popUpTo("splashScreen") { inclusive = true }
                                }
                            })
                        }
                        composable(Screen.CreatePost.route) {
                            CreatePostScreen(viewModel, navController)
                        }
                        composable(Screen.PostsList.route) {
                            PostsListScreen(viewModel, navController)
                        }
                        composable(Screen.LikedPosts.route) {
                            LikedPostsScreen(viewModel, navController)
                        }
                        composable(
                            "editPost/{postId}",
                            arguments = listOf(navArgument("postId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val postId =
                                backStackEntry.arguments?.getString("postId") ?: return@composable
                            EditPostScreen(postId, viewModel) {
                                navController.popBackStack()
                            }
                        }

                    }
                }
            }
        }
    }
}








