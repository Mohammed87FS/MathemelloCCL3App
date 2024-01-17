package com.cc221045.mathemelloccl3.viewmodel



import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cc221045.mathemelloccl3.data.LikedPostDao
import com.cc221045.mathemelloccl3.data.PostDao
import com.cc221045.mathemelloccl3.data.RequestDao
import com.google.firebase.auth.FirebaseAuth

class MainViewModelFactory(
    private val postDao: PostDao,
    private val requestDao: RequestDao,
    private val auth: FirebaseAuth,
    private val likedPostDao: LikedPostDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(postDao, auth,requestDao, likedPostDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
