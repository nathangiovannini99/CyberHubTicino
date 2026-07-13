package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profiles")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val alias: String = "Anonimo",
    val avatarSeed: String = "avatar1",
    val anonymousMode: Boolean = true,
    val securityPinEnabled: Boolean = false,
    val pin: String = ""
)

@Entity(tableName = "diary_entries")
data class DiaryEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: String,
    val intensity: Int, // 1 to 5
    val feeling: String,
    val note: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "forum_posts")
data class ForumPost(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val author: String,
    val school: String,
    val title: String,
    val content: String,
    val category: String,
    val likes: Int = 0,
    val isLiked: Boolean = false,
    val timestamp: Long = System.currentTimeMillis(),
    val isUserGenerated: Boolean = false
)

@Entity(tableName = "saved_resources")
data class SavedResource(
    @PrimaryKey val id: String,
    val title: String,
    val category: String,
    val isSaved: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)
