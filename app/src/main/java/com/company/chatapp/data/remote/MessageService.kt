package com.company.chatapp.data.remote

import com.company.chatapp.domain.model.Message

interface MessageService {

    suspend fun getAllMessages(): List<Message>

    companion object {
        const val BASE_URL = "http://192.168.8.101:8080"
        // For server, it'll be http://10.0.2.2:$port I guess
        // For same wifi, it'll be ip address then port e.g http://192.168.8.101:$port
        // To check type ipconfig in CMD and you'll see something like IPv4 Address.....: 192.168.8.101
        // Always add the :8080 port or whatever it is in ktor
    }

    sealed class Endpoints(val url: String) {
        object GetAllMessages: Endpoints("$BASE_URL/messages")
    }

}