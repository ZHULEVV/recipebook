package com.recipebook.android.presentation.comments

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.recipebook.android.domain.model.Comment
import com.recipebook.android.domain.usecase.AddCommentUseCase
import com.recipebook.android.domain.usecase.GetCommentsUseCase
import com.recipebook.android.domain.util.Resource
import com.recipebook.android.presentation.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CommentsUiState(
    val comments: List<Comment> = emptyList(),
    val inputText: String = "",
    val isLoading: Boolean = false,
    val isSending: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class CommentsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCommentsUseCase: GetCommentsUseCase,
    private val addCommentUseCase: AddCommentUseCase
) : ViewModel() {

    private val recipeId: String = checkNotNull(savedStateHandle[Screen.Comments.ARG_RECIPE_ID])

    private val _uiState = MutableStateFlow(CommentsUiState())
    val uiState: StateFlow<CommentsUiState> = _uiState.asStateFlow()

    init {
        loadComments()
    }

    fun loadComments() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = getCommentsUseCase(recipeId)) {
                is Resource.Success -> _uiState.update { it.copy(isLoading = false, comments = result.data) }
                is Resource.Error   -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                else                -> _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onInputChange(value: String) {
        _uiState.update { it.copy(inputText = value) }
    }

    fun sendComment() {
        val text = _uiState.value.inputText.trim()
        if (text.isBlank()) return
        viewModelScope.launch {
            _uiState.update { it.copy(isSending = true) }
            when (val result = addCommentUseCase(recipeId, text)) {
                is Resource.Success -> _uiState.update { state ->
                    state.copy(
                        isSending = false,
                        inputText = "",
                        comments = state.comments + result.data
                    )
                }
                is Resource.Error -> _uiState.update { it.copy(isSending = false, error = result.message) }
                else              -> _uiState.update { it.copy(isSending = false) }
            }
        }
    }
}
