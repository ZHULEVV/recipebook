package com.recipebook.android.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.recipebook.android.data.local.database.entity.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryDao {

    @Query("SELECT * FROM search_history ORDER BY timestamp DESC LIMIT 10")
    fun getHistory(): Flow<List<SearchHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: SearchHistoryEntity): Long

    @Query("SELECT COUNT(*) FROM search_history")
    suspend fun getCount(): Int

    @Query("DELETE FROM search_history WHERE id = (SELECT id FROM search_history ORDER BY timestamp ASC LIMIT 1)")
    suspend fun deleteOldest(): Int

    @Query("DELETE FROM search_history")
    suspend fun clearAll(): Int
}
