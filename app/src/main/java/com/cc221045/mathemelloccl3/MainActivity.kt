package com.cc221045.mathemelloccl3


import LoginScreen
import RequestsListScreen
import SettingsScreen
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
import androidx.compose.material.icons.filled.Person
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
import com.cc221045.mathemelloccl3.screens.BottomNavigationBar
import com.cc221045.mathemelloccl3.screens.CreatePostScreen
import com.cc221045.mathemelloccl3.screens.CreateRequestScreen
import com.cc221045.mathemelloccl3.screens.EditPostScreen
import com.cc221045.mathemelloccl3.screens.LikedPostsScreen
import com.cc221045.mathemelloccl3.screens.PostsListScreen
import com.cc221045.mathemelloccl3.screens.SignUpScreen
import com.cc221045.mathemelloccl3.ui.theme.KotloTheme
import com.cc221045.mathemelloccl3.viewmodel.MainViewModel
import com.cc221045.mathemelloccl3.viewmodel.MainViewModelFactory
import com.google.firebase.auth.FirebaseAuth

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    data object CreatePost : Screen("createPost", "", Icons.Filled.Add)
    data object PostsList : Screen("postsList", "", Icons.AutoMirrored.Filled.List)
    data object LikedPosts : Screen("likedPosts", "", Icons.Filled.Favorite)
    data object Login : Screen("login", "", Icons.AutoMirrored.Filled.ExitToApp)
    data object Settings : Screen("settings", "", Icons.Filled.Person)
    data object SignUp : Screen("signup", "", Icons.Filled.AccountCircle)
    data object CreateRequest : Screen("createRequest", "", Icons.Filled.Add)
    data object RequestsList : Screen("requestsList", "", Icons.AutoMirrored.Filled.List)

}



val screens = listOf(Screen.CreatePost, Screen.RequestsList,Screen.CreateRequest,Screen.PostsList, Screen.LikedPosts,Screen.Settings,Screen.Login,Screen.SignUp)


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


                    if (auth.currentUser != null || viewModel.isAdmin && currentRoute !in hideBottomBarRoutes) {
                        BottomNavigationBar(navController, viewModel)
                    }
                }) { innerPadding ->
                    NavHost(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                        startDestination = getStartDestination()
                    ) {
                        composable(Screen.Settings.route) {
                            SettingsScreen(viewModel, navController)
                        }
                        composable(Screen.Login.route) {
                            LoginScreen(viewModel, navController)
                        }

                        composable(Screen.SignUp.route) {
                            SignUpScreen(viewModel, navController,auth)
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

}