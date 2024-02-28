package com.company.chatapp.presentation.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.chatapp.data.remote.ChatSocketService
import com.company.chatapp.data.remote.MessageService
import com.company.chatapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val messageService: MessageService,
    private val chatSocketService: ChatSocketService,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _messageText = MutableStateFlow<String>("")
    val messageText : StateFlow<String> = _messageText

    private val _chatState = MutableStateFlow(ChatState())
    val chatState: StateFlow<ChatState> = _chatState

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent: SharedFlow<String> = _toastEvent.asSharedFlow()

    fun onMessageChaned(message: String){
        _messageText.update { message }
    }

    fun connectToChat() {
        getAllMessages()
        savedStateHandle.get<String>("username")?.let { username ->
            viewModelScope.launch {
                when (val result = chatSocketService.initSession(username)){
                    is Resource.Success -> {
                        chatSocketService.observeMessages()
                            .onEach { message ->
                                val newList = chatState.value.messages.toMutableList().apply {
                                    add(0, message)
                                }
                                _chatState.update {
                                    it.copy( messages = newList)
                                }
                            }.launchIn(viewModelScope)
                    }
                    is Resource.Error -> {
                        _toastEvent.emit( result.message ?: "Unknown Error")
                    }
                }
            }
        }
    }

    fun disconnect(){
        viewModelScope.launch {
            chatSocketService.closeSession()
        }
    }

    fun getAllMessages () {
        viewModelScope.launch {
            _chatState.update { it.copy(isLoading = true) }

            val messages = messageService.getAllMessages()

            _chatState.update {
                it.copy(
                    messages = messages,
                    isLoading = false
                )
            }
        }
    }

    fun sendMessage() {
        viewModelScope.launch {
            if (messageText.value.isNotBlank())
                chatSocketService.sendMessage(messageText.value)
        }
    }

    override fun onCleared() {
        super.onCleared()
        disconnect()
    }

}