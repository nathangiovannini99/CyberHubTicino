package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.data.AppDatabase
import com.example.data.CyberHelpRepository
import com.example.ui.CyberHelpMainScreen
import com.example.ui.CyberHelpViewModel
import com.example.ui.CyberHelpViewModelFactory
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private val database by lazy { AppDatabase.getDatabase(this) }
    private val repository by lazy { CyberHelpRepository(database) }
    private val viewModel: CyberHelpViewModel by viewModels {
        CyberHelpViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                CyberHelpMainScreen(viewModel)
            }
        }
    }
}

