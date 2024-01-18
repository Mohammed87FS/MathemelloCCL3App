
package com.cc221045.mathemelloccl3.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update





@Dao
interface RequestDao {
    @Insert
    suspend fun insertRequest(request: Request)

    @Update
    suspend fun updateRequest(request: Request)

    @Delete
    suspend fun deleteRequest(request: Request)

    @Query("SELECT * FROM requests WHERE userEmail = :userEmail ORDER BY timestamp DESC")
    suspend fun getRequestsByUser(userEmail: String): List<Request>

    @Query("SELECT * FROM requests ORDER BY timestamp DESC")
    suspend fun getAllRequests(): List<Request>
}
