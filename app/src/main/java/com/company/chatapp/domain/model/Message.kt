package com.company.chatapp.domain.model

data class Message(
    val messageText: String,
    val username: String,
    val formattedTime: String
)
