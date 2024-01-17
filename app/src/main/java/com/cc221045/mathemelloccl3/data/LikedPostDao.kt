package com.cc221045.mathemelloccl3.data

// LikedPostDao.kt

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LikedPostDao {
    @Insert
    suspend fun likePost(likedPost: LikedPost): Long

    @Query("DELETE FROM LikedPost WHERE id = :postId AND userEmail = :userEmail")
    suspend fun unlikePost(postId: Int, userEmail: String)


    @Query("SELECT * FROM LikedPost WHERE userEmail = :userEmail  ORDER BY timestamp DESC")
     fun getLikedPosts(userEmail: String?):Flow<List<LikedPost>>}

