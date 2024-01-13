
package com.cc221045.mathemelloccl3.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow





@Dao
interface RequestDao {
    @Insert
    suspend fun insertRequest(request: Request)

    @Update
    suspend fun updateRequest(request: Request)

    @Delete
    suspend fun deleteRequest(request: Request)

    @Query("SELECT * FROM requests WHERE userEmail = :userEmail ORDER BY timestamp DESC")
    fun getRequestsByUser(userEmail: String?): Flow<List<Request>>

    @Query("SELECT * FROM requests ORDER BY timestamp DESC")
    fun getAllRequests(): Flow<List<Request>>
}
