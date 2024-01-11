package com.cc221045.mathemelloccl3.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {

    @Query("SELECT * FROM posts")
    fun getPosts(): Flow<List<Post>>

    @Query("SELECT * FROM posts WHERE id = :postId")
    fun getPostById(postId: String): Flow<Post>

    @Query("SELECT * FROM posts WHERE isLiked = 1")
    fun getLikedPosts(): Flow<List<Post>>

    @Insert
    suspend fun insertPost(post: Post)

    @Update
    suspend fun updatePost(post: Post)

    @Delete
    suspend fun deletePost(post: Post)
}
