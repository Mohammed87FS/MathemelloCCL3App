package com.cc221045.mathemelloccl3.data

// LikedPost.kt

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "LikedPost")
data class LikedPost(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String,
    val timestamp: Long,
    val userEmail: String,
    val postId : Int
)

