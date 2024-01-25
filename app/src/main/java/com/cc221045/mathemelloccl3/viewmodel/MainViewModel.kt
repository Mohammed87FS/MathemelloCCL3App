package com.cc221045.mathemelloccl3.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cc221045.mathemelloccl3.data.LikedPost
import com.cc221045.mathemelloccl3.data.LikedPostDao
import com.cc221045.mathemelloccl3.data.Post
import com.cc221045.mathemelloccl3.data.PostDao
import com.cc221045.mathemelloccl3.data.Request
import com.cc221045.mathemelloccl3.data.RequestDao
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class MainViewModel(
    private val postDao: PostDao,
    val auth: FirebaseAuth,
    private val requestDao: RequestDao,
    private val likedPostDao: LikedPostDao,
) : ViewModel() {
    private var _posts by mutableStateOf<List<Post>>(emptyList())
    private var _requests by mutableStateOf<List<Request>>(emptyList())
    val posts: List<Post> get() = _posts
    val requests: List<Request> get() = _requests

    private val adminEmail = "admin@admin.com"
    private val adminPassword = "adminadmin"

    var isAdmin by mutableStateOf(false)
    val userEmail: String
        get() {
            val email = auth.currentUser?.email
            Log.d("MainViewModel", "User email: $email")
            return email!!
        }

    fun loginUser(
        email: String,
        password: String,
        onResult: (Boolean, Boolean) -> Unit,
    ) {
        if (email == adminEmail && password == adminPassword) {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    isAdmin = true
                    onResult(true, true)}}
        } else {
            // Regular user login
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    isAdmin = false
                    onResult(true, false)
                } else {
                    onResult(false, false)
                }
            }
        }
    }

    fun logout() {
        auth.signOut()
        isAdmin = false

    }

    suspend fun getUserRequests(userEmail: String): List<Request> {
        return requestDao.getRequestsByUser(userEmail)
    }

    suspend fun getAllRequests(): List<Request> {
        return requestDao.getAllRequests()
    }

    fun reloadPosts() {
        viewModelScope.launch {
            _posts = postDao.getPosts().sortedByDescending { it.timestamp }
        }
    }

    fun reloadRequests() {
        viewModelScope.launch {
            _requests = requestDao.getAllRequests().sortedByDescending { it.timestamp }
        }
    }

    suspend fun getAllPosts(): List<Post> {
        return postDao.getPosts()
    }

    suspend fun getPostById(postId: String): Post? {
        return postDao.getPostById(postId)
    }

    fun registerUser(
        email: String,
        password: String,
        onResult: (Boolean) -> Unit,
    ) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onResult(true)
            }
        }
    }


    fun addPost(
        title: String,
        content: String,
        imageUrl: String // This should be the path to the image
    ) {
        viewModelScope.launch {
            val timestamp = System.currentTimeMillis()
            val newPost = Post(title = title, content = content, timestamp = timestamp, imageUrl = imageUrl)
            postDao.insertPost(newPost)
        }
    }

    private val _selectedImageUri = MutableLiveData<Uri?>()
    val selectedImageUri: MutableLiveData<Uri?> = _selectedImageUri

    fun setImageUri(context: Context, uri: Uri) {
        val newFilePath = copyImageToInternalStorage(context, uri)
        _selectedImageUri.value = Uri.parse(newFilePath)
    }

    private fun copyImageToInternalStorage(context: Context, uri: Uri): String {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val directory = File(context.filesDir, "postImages")
        if (!directory.exists()) directory.mkdirs()

        // Generate a unique filename using timestamp to avoid name conflicts
        val fileName = "image_${System.currentTimeMillis()}.jpg"
        val file = File(directory, fileName)
        val outputStream = FileOutputStream(file)

        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }

        return file.absolutePath
    }
    fun clearImageUri() {
        _selectedImageUri.value = null
    }

    fun updatePost(post: Post) {
        viewModelScope.launch {
            postDao.updatePost(post)
            val updatedPosts = postDao.getPosts().sortedByDescending { it.timestamp }
            _posts = updatedPosts
        }
    }

    fun deletePost(post: Post) {
        viewModelScope.launch {
            postDao.deletePost(post)
            val updatedPosts = postDao.getPosts().sortedByDescending { it.timestamp }
            _posts = updatedPosts
        }
    }

    fun addRequest(
        userEmail: String,
        title: String,
        content: String,imageUrl:String
    ) {
        viewModelScope.launch {
            val newRequest =
                Request(
                    userEmail = userEmail,
                    title = title,
                    content = content,
                    timestamp = System.currentTimeMillis(),
                    imageUrl=imageUrl,
                    isChecked = false
                )
            requestDao.insertRequest(newRequest)
        }
    }





    fun toggleCheckedRequest(
       requestId: Long
    ) {
        viewModelScope.launch {
            val isRequestChecked =       requestDao.isCheckedRequest(requestId)

            if (isRequestChecked == false) {
                requestDao.checkRequest(requestId)

           } else {
                requestDao.unCheckRequest(requestId)


           }
        }
    }



    fun toggleLikePost(
        post: Post,
        userEmail: String,
    ) {
        viewModelScope.launch {
            val existingLikedPost = likedPostDao.getLikedPostByPostId(post.id, userEmail)
            if (existingLikedPost != null) {
                // Post is already liked, so unlike it
                likedPostDao.unlikePost(post.id, userEmail)
            } else {
                // Post is not liked, so like it
                val newLikedPost =
                    LikedPost(
                        title = post.title,
                        content = post.content,
                        timestamp = System.currentTimeMillis(),
                        userEmail = userEmail,
                        postId = post.id,
                    )
                likedPostDao.likePost(newLikedPost)
            }
        }
    }


    suspend fun isPostLiked(
        postId: Int,
        userEmail: String,
    ): Boolean {
        return likedPostDao.getLikedPostByPostId(postId, userEmail) != null
    }

    suspend fun isRequestChecked(requestId:Long):Boolean{
        return requestDao.isCheckedRequest(requestId) != null

    }


    suspend fun getLikedPosts(userEmail: String): List<LikedPost> {
        return likedPostDao.getLikedPosts(userEmail)
    }
}
