package com.hackhathon.mentalhealthapp.chatGptScreen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.hackhathon.ui_kit.theme.MentalHealthAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatWithGptActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MentalHealthAppTheme {
                ChatWithGptScreen()
            }
        }
    }
}