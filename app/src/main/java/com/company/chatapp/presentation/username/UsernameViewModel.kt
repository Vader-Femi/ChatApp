package com.company.chatapp.presentation.username

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsernameViewModel @Inject constructor(): ViewModel(){

    private val _usernameText = MutableStateFlow("")
    val usernameText :StateFlow<String> = _usernameText

    private val _onJoinChat = MutableSharedFlow<String>()
    val onJoinChat = _onJoinChat.asSharedFlow()

    fun onUserNameChanged(username: String){
        _usernameText.update { username }
    }

    fun onJoinClick() {
        viewModelScope.launch {
            if (usernameText.value.isNotBlank())
                _onJoinChat.emit(usernameText.value)
        }
    }


}