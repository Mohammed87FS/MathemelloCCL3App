package com.cc221045.mathemelloccl3.ui.theme

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.cc221045.mathemelloccl3.data.Post
import com.cc221045.mathemelloccl3.data.PostDao
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainViewModel(private val postDao: PostDao,
                    private val auth: FirebaseAuth) : ViewModel()  {
    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts

    fun reloadPosts() {
        viewModelScope.launch {
            val updatedPosts = postDao.getPosts().first().sortedByDescending { it.timestamp }
            _posts.value = updatedPosts


        }
    }
    fun loginUser(email: String, password: String, onResult: (Boolean) -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            onResult(task.isSuccessful)
        }
    }

    fun registerUser(email: String, password: String, onResult: (Boolean) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            onResult(task.isSuccessful)
        }
    }

    init {
        viewModelScope.launch {
            postDao.getPosts().collect { listOfPosts ->
                _posts.value = listOfPosts
            }
        }
    }

    fun getPostById(postId: String): Flow<Post?> = postDao.getPostById(postId)

    fun addPost(title: String, content: String) {
        viewModelScope.launch {
            val timestamp = System.currentTimeMillis()
            val newPost = Post(title = title, content = content, timestamp = timestamp)
            postDao.insertPost(newPost)
        }
    }

    fun updatePost(post: Post) {
        viewModelScope.launch {
            postDao.updatePost(post)
            val updatedPosts = postDao.getPosts().first().sortedByDescending { it.timestamp }
            _posts.value = updatedPosts

        }
    }

    fun deletePost(post: Post) {
        viewModelScope.launch {
            postDao.deletePost(post)
            val updatedPosts = postDao.getPosts().first().sortedByDescending { it.timestamp }
            _posts.value = updatedPosts
        }
    }

    fun likePost(post: Post) {
        viewModelScope.launch {
            postDao.updatePost(post.copy(isLiked = true))
            val updatedPosts = postDao.getPosts().first().sortedByDescending { it.timestamp }
            _posts.value = updatedPosts
        }
    }

    fun unlikePost(post: Post) {
        viewModelScope.launch {
            postDao.updatePost(post.copy(isLiked = false))
            val updatedPosts = postDao.getPosts().first().sortedByDescending { it.timestamp }
            _posts.value = updatedPosts
        }
    }

    val likedPosts: LiveData<List<Post>> = postDao.getLikedPosts().asLiveData()



}
