package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.SettingsRepository
import com.example.speech.SpeechRecognizerHelper
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class VoiceCameraViewModel(application: Application) : AndroidViewModel(application) {

    private val settingsRepository = SettingsRepository(application)
    private val speechRecognizerHelper = SpeechRecognizerHelper(application)

    val startRecordingCommand = settingsRepository.startRecordingCommand
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SettingsRepository.DEFAULT_START_COMMAND)

    val stopRecordingCommand = settingsRepository.stopRecordingCommand
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SettingsRepository.DEFAULT_STOP_COMMAND)

    val isListening = speechRecognizerHelper.isListening
    val recognizedText = speechRecognizerHelper.recognizedText
    val speechError = speechRecognizerHelper.error

    fun startListening() {
        speechRecognizerHelper.startListening()
    }

    fun stopListening() {
        speechRecognizerHelper.stopListening()
    }

    fun updateStartCommand(command: String) {
        viewModelScope.launch {
            settingsRepository.updateStartRecordingCommand(command)
        }
    }

    fun updateStopCommand(command: String) {
        viewModelScope.launch {
            settingsRepository.updateStopRecordingCommand(command)
        }
    }

    override fun onCleared() {
        super.onCleared()
        speechRecognizerHelper.stopListening()
    }
}
