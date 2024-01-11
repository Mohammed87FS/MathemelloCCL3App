package com.cc221045.mathemelloccl3


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import com.cc221045.mathemelloccl3.data.AppDatabase
import com.cc221045.mathemelloccl3.ui.theme.*
import com.google.firebase.auth.FirebaseAuth

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object CreatePost : Screen("createPost", "Create Post", Icons.Filled.Add)
    object PostsList : Screen("postsList", "Posts List", Icons.Filled.List)
    object LikedPosts : Screen("likedPosts", "Liked Posts", Icons.Filled.Favorite)

}

val screens = listOf(Screen.CreatePost, Screen.PostsList, Screen.LikedPosts) // Include LikedPosts


class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "kotlin-db"
        ).fallbackToDestructiveMigration().build()


        val viewModelFactory = MainViewModelFactory(db.postDao(), auth)
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

    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener(authStateListener)
    }

    override fun onStop() {
        super.onStop()
        auth.removeAuthStateListener(authStateListener)
    }

    private val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        val user = firebaseAuth.currentUser
        if (user != null) {
            // User is signed in
            // TODO: Navigate the user to the main part of your app
        } else {
            // User is signed out
            // TODO: Navigate the user to the login screen
        }
    }

    // Placeholder function for sign-up logic
    private fun signUpUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign up success
                    val user = auth.currentUser
                    // TODO: Update UI with user information
                } else {
                    // Sign up error
                    // TODO: Handle sign up failure
                }
            }
    }

    // Placeholder function for sign-in logic
    private fun signInUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success
                    val user = auth.currentUser
                    // TODO: Update UI with user information
                } else {
                    // Sign in error
                    // TODO: Handle sign in failure
                }
            }
    }
}







