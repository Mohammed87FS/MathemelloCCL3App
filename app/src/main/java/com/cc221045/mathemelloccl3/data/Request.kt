package com.cc221045.mathemelloccl3.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "requests")
data class Request(
    @PrimaryKey(autoGenerate = true) val requestId: Long = 0,
    val userEmail: String,
    val title: String,
    val content: String,
    val timestamp: Long,
    val imageUrl: String,
    var isChecked: Boolean = false

)
