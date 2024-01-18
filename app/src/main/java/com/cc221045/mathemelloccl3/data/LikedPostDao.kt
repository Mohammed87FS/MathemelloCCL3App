package com.cc221045.mathemelloccl3.data

// LikedPostDao.kt

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LikedPostDao {
    @Insert
    suspend fun likePost(likedPost: LikedPost): Long

    @Query("DELETE FROM LikedPost WHERE postId = :postId AND userEmail = :userEmail")
    suspend fun unlikePost(postId: Int, userEmail: String)

    @Query("SELECT * FROM LikedPost WHERE postId = :postId AND userEmail = :userEmail LIMIT 1")
    suspend fun getLikedPostByPostId(postId: Int, userEmail: String): LikedPost?


    @Query("SELECT * FROM LikedPost WHERE userEmail = :userEmail  ORDER BY timestamp DESC")
    suspend fun getLikedPosts(userEmail: String?): List<LikedPost>
}


