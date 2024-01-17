package com.cc221045.mathemelloccl3.data

// LikedPostDao.kt

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LikedPostDao {
    @Insert
    suspend fun likePost(likedPost: LikedPost)

    @Query("DELETE FROM LikedPost WHERE id = :postId AND userEmail = :userEmail")
    suspend fun unlikePost(postId: Int, userEmail: String)


    @Query("SELECT * FROM LikedPost WHERE userEmail = :userEmail")
    fun getLikedPosts(userEmail: String): LiveData<List<LikedPost>>
}
