package com.cc221045.mathemelloccl3.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PostDao {
    @Query("SELECT * FROM posts")
    suspend fun getPosts(): List<Post>

    @Query("SELECT * FROM posts WHERE id = :postId")
    suspend fun getPostById(postId: String): Post?


    @Insert
    suspend fun insertPost(post: Post)

    @Update
    suspend fun updatePost(post: Post)

    @Delete
    suspend fun deletePost(post: Post)
}
