package com.company.chatapp.data.remote

import com.company.chatapp.domain.model.Message
import com.company.chatapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface ChatSocketService {

    suspend fun initSession(
        username: String
    ): Resource<Unit>

    suspend fun sendMessage(message: String)

    fun observeMessages(): Flow<Message>

    suspend fun closeSession()

    companion object {
        const val BASE_URL = "ws://192.168.8.101:8080"
        // For server, it'll be ws://10.0.2.2:$port I guess
        // For same wifi, it'll be ip address then port e.g ws://192.168.8.101:$port
        // To check type ipconfig in CMD and you'll see something like IPv4 Address.....: 192.168.8.101
        // Always add the :8080 port or whatever it is in ktor
    }

    sealed class Endpoints(val url: String) {
        object ChatSocket: Endpoints("$BASE_URL/chat-socket")
    }
}