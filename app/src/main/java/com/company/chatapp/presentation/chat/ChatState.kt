package com.company.chatapp.presentation.chat

import com.company.chatapp.domain.model.Message

data class ChatState(
    val messages: List<Message> = emptyList(),
    val isLoading: Boolean = false,

)
