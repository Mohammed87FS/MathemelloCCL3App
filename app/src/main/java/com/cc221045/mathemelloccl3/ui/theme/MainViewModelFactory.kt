package com.cc221045.mathemelloccl3.ui.theme



import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cc221045.mathemelloccl3.data.PostDao



import com.google.firebase.auth.FirebaseAuth

class MainViewModelFactory(
    private val postDao: PostDao,
    private val auth: FirebaseAuth
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(postDao, auth) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
