package com.cc221045.mathemelloccl3


import com.cc221045.mathemelloccl3.screens.LoginScreen
import RequestsListScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
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
import com.cc221045.mathemelloccl3.screens.ProfileScreen
import com.cc221045.mathemelloccl3.screens.SignUpScreen
import com.cc221045.mathemelloccl3.screens.SplashScreen
import com.cc221045.mathemelloccl3.ui.theme.KotloTheme
import com.cc221045.mathemelloccl3.viewmodel.MainViewModel
import com.cc221045.mathemelloccl3.viewmodel.MainViewModelFactory
import com.google.firebase.auth.FirebaseAuth

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    data object PostsList : Screen("postsList", "Posts", Icons.Filled.List)
    data object CreatePost : Screen("createPost", "Add Post", Icons.Filled.Add)

    data object LikedPosts : Screen("likedPosts", "likedPosts", Icons.Filled.Favorite)
    data object Login : Screen("login", "login", Icons.Filled.ExitToApp)
    data object ProfileScreen : Screen("profile", "Profile", Icons.Filled.Person)
    data object SignUp : Screen("signup", "signup", Icons.Filled.AccountCircle)
    data object CreateRequest : Screen("createRequest", "Add Request", Icons.Filled.Add)
    data object RequestsList : Screen("requestsList", "Requests", Icons.Filled.List)
    data object Splash : Screen("splashScreen", "splashScreen", Icons.Filled.DateRange)

}



val screens = listOf( Screen.RequestsList,Screen.Splash,Screen.CreateRequest,Screen.PostsList,Screen.CreatePost, Screen.LikedPosts,Screen.ProfileScreen,Screen.Login,Screen.SignUp)


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



        val viewModelFactory = MainViewModelFactory(db.postDao(),db.requestDao(), FirebaseAuth.getInstance(),db.likedPostDao())
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]


        setContent {
            KotloTheme {
                val navController = rememberNavController()

                Scaffold(bottomBar = {

                    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

                    val hideBottomBarRoutes = listOf(Screen.Login.route, Screen.SignUp.route,Screen.Splash.route)


                    if (!hideBottomBarRoutes.contains(currentRoute)) {
                        BottomNavigationBar(navController, viewModel)
                    }
                }) { innerPadding ->
                    NavHost(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                        startDestination = Screen.Splash.route
                    ) {
                       composable(Screen.Splash.route) {
                           SplashScreen(navController,viewModel)
                        }


                        composable(Screen.ProfileScreen.route) {
                            ProfileScreen(viewModel, navController,viewModel.isAdmin)
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
                            PostsListScreen(userEmail = viewModel.userEmail,viewModel, navController)
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