package com.cc221045.mathemelloccl3


import CreateRequestScreen
import LoginScreen
import RequestsListScreen
import SettingsScreen
import SignUpScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import com.cc221045.mathemelloccl3.data.AppDatabase
import com.cc221045.mathemelloccl3.ui.theme.*
import com.google.firebase.auth.FirebaseAuth

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object CreatePost : Screen("createPost", "Create Post", Icons.Filled.Add)
    object PostsList : Screen("postsList", "Posts List", Icons.AutoMirrored.Filled.List)
    object LikedPosts : Screen("likedPosts", "Liked Posts", Icons.Filled.Favorite)
    object Login : Screen("login", "Login", Icons.AutoMirrored.Filled.ExitToApp)
    object Settings : Screen("settings", "Settings", Icons.AutoMirrored.Filled.List)
    object SignUp : Screen("signup", "Sign Up", Icons.Filled.AccountCircle)
    object CreateRequest : Screen("createRequest", "Create Request", Icons.Filled.Add)
    object RequestsList : Screen("requestsList", "Requests List", Icons.AutoMirrored.Filled.List)

}



val screens = listOf(Screen.CreatePost, Screen.RequestsList,Screen.CreateRequest,Screen.PostsList, Screen.LikedPosts,Screen.Settings,Screen.Login,Screen.SignUp) // Include LikedPosts


class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private var navController: NavController? = null
    private lateinit var viewModel: MainViewModel
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "kotlin-db"
        ).fallbackToDestructiveMigration().build()



        val viewModelFactory = MainViewModelFactory(db.postDao(),db.requestDao(), FirebaseAuth.getInstance())
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]


        setContent {
            KotloTheme {
                val navController = rememberNavController()

                Scaffold(bottomBar = {

                    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

                    val hideBottomBarRoutes = listOf(Screen.Login.route, Screen.SignUp.route)


                    if (auth.currentUser != null && currentRoute !in hideBottomBarRoutes) {
                        BottomNavigationBar(navController, viewModel)
                    }
                }) { innerPadding ->
                    NavHost(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                        startDestination = getStartDestination()
                    ) {

                        composable(Screen.Login.route) {
                            LoginScreen(viewModel, navController)
                        }
                        composable(Screen.Settings.route) {
                            SettingsScreen(viewModel, navController)
                        }
                        composable(Screen.SignUp.route) {
                            SignUpScreen(viewModel, navController)
                        }

                        composable(Screen.CreatePost.route) {
                            CreatePostScreen(viewModel, navController)
                        }
                        composable(Screen.CreateRequest.route) {
                            CreateRequestScreen(viewModel, userEmail = viewModel.userEmail) {

                                navController.navigate(Screen.RequestsList.route)
                            }
                        }
                        composable(Screen.RequestsList.route) {

                            RequestsListScreen(viewModel, userEmail = viewModel.userEmail, isAdmin = viewModel.isAdmin)
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
    private fun getStartDestination(): String {
        return when {
            auth.currentUser != null && viewModel.isAdmin -> Screen.CreatePost.route
            auth.currentUser != null -> Screen.PostsList.route
            else -> Screen.Login.route
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

        runOnUiThread {
            if (user != null) {
                navController?.navigate(Screen.CreatePost.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            } else {
                navController?.navigate(Screen.Login.route) {
                    popUpTo(Screen.CreatePost.route) { inclusive = true }
                }
            }
        }
    }



}