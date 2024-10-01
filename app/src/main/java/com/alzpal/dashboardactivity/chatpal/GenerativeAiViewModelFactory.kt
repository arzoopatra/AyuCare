package com.alzpal.dashboardactivity.chatpal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.alzpal.BuildConfig
import com.alzpal.dashboardactivity.chatpal.feature.chat.ChatViewModel
import com.alzpal.dashboardactivity.chatpal.feature.multimodal.PhotoReasoningViewModel
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig

// Define the factory as an object expression
object GenerativeViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {
        val config = generationConfig {
            temperature = 0.7f
        }

        return with(modelClass) {
            when {
                isAssignableFrom(PhotoReasoningViewModel::class.java) -> {
                    // Initialize a GenerativeModel with the AI model for multimodal text generation
                    val generativeModel = GenerativeModel(
                        modelName = "gemini-1.5-flash-latest",
                        apiKey = BuildConfig.API_KEY, // Use BuildConfig.API_KEY here
                        generationConfig = config
                    )
                    PhotoReasoningViewModel(generativeModel)
                }

                isAssignableFrom(ChatViewModel::class.java) -> {
                    // Initialize a GenerativeModel with the AI model for chat
                    val generativeModel = GenerativeModel(
                        modelName = "gemini-1.5-flash-latest",
                        apiKey = BuildConfig.API_KEY, // Use BuildConfig.API_KEY here
                        generationConfig = config
                    )
                    ChatViewModel(generativeModel)
                }

                else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
    }
}
