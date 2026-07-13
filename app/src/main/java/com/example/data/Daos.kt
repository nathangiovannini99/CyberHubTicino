package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user_profiles WHERE id = 1 LIMIT 1")
    fun getUserProfile(): Flow<UserProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfile)
}

@Dao
interface DiaryDao {
    @Query("SELECT * FROM diary_entries ORDER BY timestamp DESC")
    fun getAllEntries(): Flow<List<DiaryEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: DiaryEntry)

    @Delete
    suspend fun deleteEntry(entry: DiaryEntry)
}

@Dao
interface ForumDao {
    @Query("SELECT * FROM forum_posts ORDER BY timestamp DESC")
    fun getAllPosts(): Flow<List<ForumPost>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: ForumPost)

    @Update
    suspend fun updatePost(post: ForumPost)

    @Query("SELECT COUNT(*) FROM forum_posts")
    suspend fun getPostCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<ForumPost>)

    @Delete
    suspend fun deletePost(post: ForumPost)
}

@Dao
interface SavedResourceDao {
    @Query("SELECT * FROM saved_resources ORDER BY timestamp DESC")
    fun getAllSavedResources(): Flow<List<SavedResource>>

    @Query("SELECT * FROM saved_resources WHERE id = :id LIMIT 1")
    suspend fun getResourceById(id: String): SavedResource?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateResource(resource: SavedResource)

    @Query("SELECT EXISTS(SELECT 1 FROM saved_resources WHERE id = :id AND isSaved = 1)")
    fun isResourceSaved(id: String): Flow<Boolean>
}
