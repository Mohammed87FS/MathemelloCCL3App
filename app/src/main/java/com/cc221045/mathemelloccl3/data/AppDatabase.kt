package com.cc221045.mathemelloccl3.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Post::class], version = 6)
abstract class AppDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
}
