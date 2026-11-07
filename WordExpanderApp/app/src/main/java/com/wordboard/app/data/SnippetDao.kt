package com.wordboard.app.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SnippetDao {

    @Query("SELECT * FROM snippets ORDER BY usageCount DESC, createdAt DESC")
    fun getAllSnippets(): Flow<List<SnippetEntity>>

    @Query("SELECT * FROM snippets ORDER BY usageCount DESC, createdAt DESC")
    suspend fun getAllSnippetsList(): List<SnippetEntity>

    @Query("SELECT * FROM snippets WHERE category = :category ORDER BY usageCount DESC")
    fun getSnippetsByCategory(category: String): Flow<List<SnippetEntity>>

    @Query("SELECT * FROM snippets WHERE shortcut = :shortcut LIMIT 1")
    suspend fun findByShortcut(shortcut: String): SnippetEntity?

    @Query("SELECT * FROM snippets WHERE shortcut LIKE :prefix || '%' ORDER BY LENGTH(shortcut) ASC LIMIT 5")
    suspend fun findMatchesStartingWith(prefix: String): List<SnippetEntity>

    @Query("SELECT * FROM snippets WHERE shortcut LIKE '%' || :query || '%' OR expandedText LIKE '%' || :query || '%' OR label LIKE '%' || :query || '%'")
    fun searchSnippets(query: String): Flow<List<SnippetEntity>>

    @Query("SELECT DISTINCT category FROM snippets ORDER BY category ASC")
    fun getAllCategories(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(snippet: SnippetEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(snippets: List<SnippetEntity>)

    @Update
    suspend fun update(snippet: SnippetEntity)

    @Delete
    suspend fun delete(snippet: SnippetEntity)

    @Query("UPDATE snippets SET usageCount = usageCount + 1, lastUsedTimestamp = :timestamp WHERE id = :id")
    suspend fun recordUsage(id: Long, timestamp: Long = System.currentTimeMillis())

    @Query("SELECT COUNT(*) FROM snippets")
    suspend fun getCount(): Int
}
