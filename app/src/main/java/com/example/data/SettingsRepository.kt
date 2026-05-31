package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "voice_camera_settings")

class SettingsRepository(private val context: Context) {

    private val dataStore = context.dataStore

    companion object {
        val START_RECORDING_COMMAND = stringPreferencesKey("start_recording_command")
        val STOP_RECORDING_COMMAND = stringPreferencesKey("stop_recording_command")
        
        const val DEFAULT_START_COMMAND = "start"
        const val DEFAULT_STOP_COMMAND = "stop"
    }

    val startRecordingCommand: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[START_RECORDING_COMMAND] ?: DEFAULT_START_COMMAND
        }

    val stopRecordingCommand: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[STOP_RECORDING_COMMAND] ?: DEFAULT_STOP_COMMAND
        }

    suspend fun updateStartRecordingCommand(command: String) {
        dataStore.edit { preferences ->
            preferences[START_RECORDING_COMMAND] = command.lowercase().trim()
        }
    }

    suspend fun updateStopRecordingCommand(command: String) {
        dataStore.edit { preferences ->
            preferences[STOP_RECORDING_COMMAND] = command.lowercase().trim()
        }
    }
}
