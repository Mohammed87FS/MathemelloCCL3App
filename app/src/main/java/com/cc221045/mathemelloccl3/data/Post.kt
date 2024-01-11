package com.cc221045.mathemelloccl3.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class Post(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var title: String,
    var content: String,
    var isLiked: Boolean = false,
    val timestamp: Long

)

