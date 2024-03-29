package com.cc221045.mathemelloccl3.data

import androidx.room.Database
import androidx.room.RoomDatabase
@Database(entities = [Post::class, Request::class,LikedPost::class], version = 9, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun requestDao(): RequestDao
    abstract fun likedPostDao(): LikedPostDao
}