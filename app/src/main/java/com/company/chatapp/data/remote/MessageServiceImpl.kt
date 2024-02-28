package com.company.chatapp.data.remote

import com.company.chatapp.data.remote.dto.MessageDto
import com.company.chatapp.domain.model.Message
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse


class MessageServiceImpl(
    private val client: HttpClient,
) : MessageService {

    override suspend fun getAllMessages(): List<Message> {

        return try {
            val response = client.get(MessageService.Endpoints.GetAllMessages.url)

            if (response.status.value in 200..299) {
                response.body<List<MessageDto>>()
                    .map { it.toMessage() }
            } else {
                emptyList()
            }

        } catch (e: Exception){
            emptyList()
        }

    }

}