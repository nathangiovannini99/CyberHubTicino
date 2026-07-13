package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers

class CyberHelpRepository(private val database: AppDatabase) {

    private val userDao = database.userDao()
    private val diaryDao = database.diaryDao()
    private val forumDao = database.forumDao()
    private val savedResourceDao = database.savedResourceDao()

    // Flow for current profile
    val userProfile: Flow<UserProfile?> = userDao.getUserProfile()

    // Flow for all diary entries
    val diaryEntries: Flow<List<DiaryEntry>> = diaryDao.getAllEntries()

    // Flow for forum posts
    val forumPosts: Flow<List<ForumPost>> = forumDao.getAllPosts()

    // Flow for saved resources
    val savedResources: Flow<List<SavedResource>> = savedResourceDao.getAllSavedResources()

    suspend fun saveProfile(profile: UserProfile) = withContext(Dispatchers.IO) {
        userDao.insertOrUpdateProfile(profile)
    }

    suspend fun addDiaryEntry(entry: DiaryEntry) = withContext(Dispatchers.IO) {
        diaryDao.insertEntry(entry)
    }

    suspend fun deleteDiaryEntry(entry: DiaryEntry) = withContext(Dispatchers.IO) {
        diaryDao.deleteEntry(entry)
    }

    suspend fun addForumPost(post: ForumPost) = withContext(Dispatchers.IO) {
        forumDao.insertPost(post)
    }

    suspend fun updateForumPost(post: ForumPost) = withContext(Dispatchers.IO) {
        forumDao.updatePost(post)
    }

    suspend fun deleteForumPost(post: ForumPost) = withContext(Dispatchers.IO) {
        forumDao.deletePost(post)
    }

    suspend fun toggleLikePost(post: ForumPost) = withContext(Dispatchers.IO) {
        val updatedPost = post.copy(
            isLiked = !post.isLiked,
            likes = if (post.isLiked) post.likes - 1 else post.likes + 1
        )
        forumDao.updatePost(updatedPost)
    }

    suspend fun toggleSaveResource(resourceId: String, title: String, category: String) = withContext(Dispatchers.IO) {
        val existing = savedResourceDao.getResourceById(resourceId)
        if (existing == null) {
            savedResourceDao.insertOrUpdateResource(
                SavedResource(id = resourceId, title = title, category = category, isSaved = true)
            )
        } else {
            savedResourceDao.insertOrUpdateResource(
                existing.copy(isSaved = !existing.isSaved)
            )
        }
    }

    fun isResourceSaved(id: String): Flow<Boolean> = savedResourceDao.isResourceSaved(id)

    suspend fun prepopulateDefaultDataIfNeeded() = withContext(Dispatchers.IO) {
        // Ensure default profile exists
        val currentProfile = userDao.getUserProfile().firstOrNull()
        if (currentProfile == null) {
            userDao.insertOrUpdateProfile(UserProfile())
        }

        // Prepopulate default forum posts if the database is empty
        val postCount = forumDao.getPostCount()
        if (postCount == 0) {
            val defaultPosts = listOf(
                ForumPost(
                    author = "Luca S.",
                    school = "Liceo Lugano 2",
                    title = "Come ho smesso di leggere i commenti e ho iniziato a vivere",
                    content = "Tutto è iniziato con un meme stupido condiviso sul gruppo della classe. In poche ore, decine di commenti offensivi hanno invaso i miei profili social. Mi sentivo soffocare, non volevo più andare a scuola. Poi ho deciso di parlarne con Chiara, la Student Ambassador della mia scuola. Mi ha aiutato a capire che il problema non ero io, ma chi commentava. Ho bloccato i bulli, ho disattivato le notifiche dei social e ho ricominciato a fare sport offline. Se stai passando una cosa simile, ti prego di aprirti con qualcuno: parlare libera l'anima.",
                    category = "Bullismo e Social",
                    likes = 14,
                    timestamp = System.currentTimeMillis() - 172800000 // 2 days ago
                ),
                ForumPost(
                    author = "Chiara M.",
                    school = "Liceo di Lugano 1",
                    title = "Benvenuti nella Community di CyberHelp Ticino!",
                    content = "Ciao a tutti! Sono Chiara, Student Ambassador. Questo è il nostro spazio sicuro e moderato per supportarci a vicenda. Qui potete condividere le vostre storie in totale anonimato, fare domande o semplicemente farci sapere come state. Ricordate: non siete soli e in Ticino abbiamo tantissimi partner fantastici pronti ad ascoltarvi gratis e senza giudizio.",
                    category = "Annuncio Ambassador",
                    likes = 29,
                    timestamp = System.currentTimeMillis() - 86400000 // 1 day ago
                ),
                ForumPost(
                    author = "Matteo R.",
                    school = "Scuola Cantonale di Commercio",
                    title = "Insulti e chat nei gruppi della squadra di Calcio",
                    content = "Molti pensano che il bullismo avvenga solo a scuola. Purtroppo a me è successo nello spogliatoio e sul gruppo WhatsApp della squadra. Gli insulti venivano spacciati per 'sfottò' o 'ironia', ma facevano male sul serio. Ne ho parlato con l'allenatore e abbiamo fatto una riunione di squadra chiarificatrice. È importante mettere dei limiti e far rispettare tutti, sia di persona che online.",
                    category = "Sport e Spogliatoio",
                    likes = 8,
                    timestamp = System.currentTimeMillis() - 43200000 // 12 hours ago
                )
            )
            forumDao.insertPosts(defaultPosts)
        }
    }
}
