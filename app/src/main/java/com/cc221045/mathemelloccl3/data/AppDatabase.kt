package com.cc221045.mathemelloccl3.data

import androidx.room.Database
import androidx.room.RoomDatabase
@Database(entities = [Post::class, Request::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun requestDao(): RequestDao
    // Other database configurations...
}