package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class AppTab {
    HOME, SUPPORT, LEARN, CIRCLE
}

class CyberHelpViewModel(private val repository: CyberHelpRepository) : ViewModel() {

    init {
        viewModelScope.launch {
            repository.prepopulateDefaultDataIfNeeded()
        }
    }

    // Tab Navigation
    private val _currentTab = MutableStateFlow(AppTab.HOME)
    val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

    fun selectTab(tab: AppTab) {
        _currentTab.value = tab
    }

    // Triage Flow State
    private val _triageStep = MutableStateFlow(1) // 1, 2, or 3 (Outcome)
    val triageStep: StateFlow<Int> = _triageStep.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    private val _selectedUrgency = MutableStateFlow<String?>(null)
    val selectedUrgency: StateFlow<String?> = _selectedUrgency.asStateFlow()

    fun selectTriageCategory(category: String) {
        _selectedCategory.value = category
    }

    fun selectTriageUrgency(urgency: String) {
        _selectedUrgency.value = urgency
    }

    fun nextTriageStep() {
        if (_triageStep.value < 3) {
            _triageStep.value += 1
        }
    }

    fun prevTriageStep() {
        if (_triageStep.value > 1) {
            _triageStep.value -= 1
        }
    }

    fun resetTriage() {
        _triageStep.value = 1
        _selectedCategory.value = null
        _selectedUrgency.value = null
    }

    fun startTriageWithCategory(category: String) {
        _selectedCategory.value = category
        _triageStep.value = 2
        _currentTab.value = AppTab.SUPPORT
    }

    fun startTriageImmediateEmergency() {
        _selectedCategory.value = "Sono in pericolo ora"
        _selectedUrgency.value = "Ho bisogno di parlare ORA"
        _triageStep.value = 3
        _currentTab.value = AppTab.SUPPORT
    }

    // User Profile
    val userProfile: StateFlow<UserProfile> = repository.userProfile
        .filterNotNull()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserProfile()
        )

    fun updateProfile(alias: String, avatarSeed: String, anonymousMode: Boolean) {
        viewModelScope.launch {
            repository.saveProfile(
                UserProfile(
                    alias = alias,
                    avatarSeed = avatarSeed,
                    anonymousMode = anonymousMode
                )
            )
        }
    }

    // Diary Entries
    val diaryEntries: StateFlow<List<DiaryEntry>> = repository.diaryEntries
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addDiary(category: String, intensity: Int, feeling: String, note: String) {
        viewModelScope.launch {
            repository.addDiaryEntry(
                DiaryEntry(
                    category = category,
                    intensity = intensity,
                    feeling = feeling,
                    note = note
                )
            )
        }
    }

    fun removeDiary(entry: DiaryEntry) {
        viewModelScope.launch {
            repository.deleteDiaryEntry(entry)
        }
    }

    // Forum Posts
    val forumPosts: StateFlow<List<ForumPost>> = repository.forumPosts
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun publishForumPost(title: String, content: String, category: String) {
        val currentAlias = userProfile.value.alias
        val isAnon = userProfile.value.anonymousMode
        val authorName = if (isAnon) "Utente Anonimo" else currentAlias
        viewModelScope.launch {
            repository.addForumPost(
                ForumPost(
                    author = authorName,
                    school = "Ticino Community",
                    title = title,
                    content = content,
                    category = category,
                    isUserGenerated = true
                )
            )
        }
    }

    fun toggleLike(post: ForumPost) {
        viewModelScope.launch {
            repository.toggleLikePost(post)
        }
    }

    fun deletePost(post: ForumPost) {
        viewModelScope.launch {
            repository.deleteForumPost(post)
        }
    }

    // Learn Screen Filtering & Search
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedLearnCategory = MutableStateFlow("Tutto")
    val selectedLearnCategory: StateFlow<String> = _selectedLearnCategory.asStateFlow()

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectLearnCategory(category: String) {
        _selectedLearnCategory.value = category
    }

    // Saved Resources
    val savedResources: StateFlow<List<SavedResource>> = repository.savedResources
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun toggleSavedResource(resourceId: String, title: String, category: String) {
        viewModelScope.launch {
            repository.toggleSaveResource(resourceId, title, category)
        }
    }

    // Generatore di Risposte Sicure (Safe Response Generator)
    private val _offensiveInput = MutableStateFlow("")
    val offensiveInput: StateFlow<String> = _offensiveInput.asStateFlow()

    private val _generatedResponse = MutableStateFlow<String?>(null)
    val generatedResponse: StateFlow<String?> = _generatedResponse.asStateFlow()

    fun updateOffensiveInput(text: String) {
        _offensiveInput.value = text
        if (text.isBlank()) {
            _generatedResponse.value = null
        }
    }

    fun generateSafeResponse() {
        val input = _offensiveInput.value
        if (input.isBlank()) return

        // Provide custom, clever, emotionally constructive local response recommendations based on the input text
        val lowercaseInput = input.lowercase()
        val generated = when {
            lowercaseInput.contains("fallito") || lowercaseInput.contains("fai schifo") || lowercaseInput.contains("brutto") -> {
                "\"Capisco che tu possa avere questa opinione, ma non rispecchia chi sono. Preferisco focalizzarmi su cose positive.\""
            }
            lowercaseInput.contains("scemo") || lowercaseInput.contains("stupido") || lowercaseInput.contains("idiota") -> {
                "\"Mi dispiace che tu scelga di insultarmi. Se c'è un problema reale di cui vuoi discutere con calma, sono qui.\""
            }
            lowercaseInput.contains("minaccia") || lowercaseInput.contains("ti picchio") || lowercaseInput.contains("rovino") -> {
                "\"Questa conversazione sta superando il limite del rispetto. Ti invito a smettere immediatamente, altrimenti documenterò e segnalerò questo messaggio.\""
            }
            lowercaseInput.contains("escluso") || lowercaseInput.contains("non ti vogliamo") -> {
                "\"Va bene, accetto la vostra scelta. Ho altre persone splendide con cui passare il mio tempo e condividere le mie passioni.\""
            }
            else -> {
                "\"Il tuo messaggio è offensivo e non intendo proseguire una conversazione su questi toni. Ti chiedo di comunicare con rispetto.\""
            }
        }
        _generatedResponse.value = generated
    }
}

class CyberHelpViewModelFactory(private val repository: CyberHelpRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CyberHelpViewModel::class.java)) {
            return CyberHelpViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
