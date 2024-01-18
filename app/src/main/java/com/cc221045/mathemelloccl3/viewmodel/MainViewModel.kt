package com.cc221045.mathemelloccl3.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.cc221045.mathemelloccl3.data.LikedPost
import com.cc221045.mathemelloccl3.data.LikedPostDao
import com.cc221045.mathemelloccl3.data.Post
import com.cc221045.mathemelloccl3.data.PostDao
import com.cc221045.mathemelloccl3.data.Request
import com.cc221045.mathemelloccl3.data.RequestDao
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch



class MainViewModel(private val postDao: PostDao,
                     val auth: FirebaseAuth,
                    private val requestDao: RequestDao,
    private val likedPostDao: LikedPostDao) : ViewModel()  {

    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts

    private val adminEmail = "admin@admin.com"
    private val adminPassword = "adminadmin"

    var isAdmin by mutableStateOf(false)





    var userEmail = MutableLiveData<String>()


    fun loginUser(email: String, password: String, onResult: (Boolean, Boolean) -> Unit) {
        if (email == adminEmail && password == adminPassword) {
            // Admin credentials
            isAdmin = true
            userEmail.value = email
            onResult(true, true)
        } else {
            // Regular user login
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    isAdmin = false
                    userEmail.value = auth.currentUser?.email ?: ""
                    onResult(true, false)
                } else {
                    onResult(false, false)
                }
            }
        }
    }
    fun logout() {

        auth.signOut()
        userEmail.value = ""
        isAdmin = false
    }




    fun getUserRequests(userEmail: String?): LiveData<List<Request>> {
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



    fun registerUser(email: String, password: String, onResult: (Boolean) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {


                onResult(true)
        }
    }}

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
    fun toggleLikePost(post: Post, userEmail: String) {
        viewModelScope.launch {
            val existingLikedPost = likedPostDao.getLikedPostByPostId(post.id, userEmail).firstOrNull()
            if (existingLikedPost != null) {
                // Post is already liked, so unlike it
                likedPostDao.unlikePost(post.id, userEmail)
            } else {
                // Post is not liked, so like it
                val newLikedPost = LikedPost(
                    title = post.title,
                    content = post.content,
                    timestamp = System.currentTimeMillis(),
                    userEmail = userEmail,
                    postId = post.id
                )
                likedPostDao.likePost(newLikedPost)
            }
        }
    }
    fun likePost(post: Post,userEmail :String) {

        viewModelScope.launch {


                val likedPost = LikedPost(
                    title = post.title,
                    content = post.content,
                    timestamp = System.currentTimeMillis(),
                    userEmail = userEmail,
                    postId = post.id // Assuming `post` has an `id` field
                )
                likedPostDao.likePost(likedPost)


        }
    }
    fun unlikePost(postId: Int, userEmail: String) {
        viewModelScope.launch {
            likedPostDao.unlikePost(postId, userEmail)

        }}

    fun isPostLiked(postId: Int, userEmail: String): LiveData<Boolean> = liveData {
        emit(likedPostDao.getLikedPostByPostId(postId, userEmail).firstOrNull() != null)
    }


    fun getLikedPostsLiveData(userEmail: String?): LiveData<List<LikedPost>> {
        return likedPostDao.getLikedPosts(userEmail).asLiveData()
    }






}

