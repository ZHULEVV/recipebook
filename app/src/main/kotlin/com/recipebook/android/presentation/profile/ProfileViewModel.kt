package com.recipebook.android.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.recipebook.android.data.local.datastore.UserPreferencesDataStore
import com.recipebook.android.domain.model.User
import com.recipebook.android.domain.usecase.GetCurrentUserUseCase
import com.recipebook.android.domain.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val user: User? = null,
    val isLoggedOut: Boolean = false
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val dataStore: UserPreferencesDataStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    val isDarkTheme: StateFlow<Boolean> = dataStore.isDarkTheme
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    init {
        getCurrentUserUseCase().onEach { user ->
            _uiState.update { it.copy(user = user) }
        }.launchIn(viewModelScope)
    }

    fun toggleTheme() {
        viewModelScope.launch {
            dataStore.setDarkTheme(!isDarkTheme.value)
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
            _uiState.update { it.copy(isLoggedOut = true) }
        }
    }
}
