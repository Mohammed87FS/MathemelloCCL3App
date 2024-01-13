package com.cc221045.mathemelloccl3.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.cc221045.mathemelloccl3.data.Post
import com.cc221045.mathemelloccl3.data.PostDao
import com.cc221045.mathemelloccl3.data.Request
import com.cc221045.mathemelloccl3.data.RequestDao
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch



class MainViewModel(private val postDao: PostDao,
                    private val auth: FirebaseAuth,
                    private val requestDao: RequestDao) : ViewModel()  {

    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts

    private val adminEmail = "admin@admin.com"
    private val adminPassword = "adminadmin"

    var isAdmin by mutableStateOf(false)
    val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: ""

    fun addRequest(userEmail: String, title: String, content: String) {
        viewModelScope.launch {
            val newRequest = Request(
                userEmail = userEmail,
                title = title,
                content = content,
                timestamp = System.currentTimeMillis()
            )
            requestDao.insertRequest(newRequest)
        }
    }

    fun getUserRequests(userEmail: String): LiveData<List<Request>> {
        return requestDao.getRequestsByUser(userEmail).asLiveData()
    }

    fun getAllRequests(): LiveData<List<Request>> {
        return requestDao.getAllRequests().asLiveData()
    }


    fun reloadPosts() {
        viewModelScope.launch {
            val updatedPosts = postDao.getPosts().first().sortedByDescending { it.timestamp }
            _posts.value = updatedPosts


        }
    }
    fun loginUser(email: String, password: String, onResult: (Boolean, Boolean) -> Unit) {
        if (email == adminEmail && password == adminPassword) {
            // Credentials match the admin's
            isAdmin = true
            onResult(true, true) // Successful login and is admin
        } else {
            // Proceed with regular login
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    isAdmin = false
                    onResult(task.isSuccessful, false) // True if login is successful, false for isAdmin
                }
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
