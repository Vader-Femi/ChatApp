package com.company.chatapp.data.remote.dto

import com.company.chatapp.domain.model.Message
import kotlinx.serialization.Serializable
import java.text.DateFormat
import java.util.Date

@Serializable
data class MessageDto(
    val id: String,
    val messageText: String,
    val username: String,
    val timeStamp: Long
){
    fun toMessage(): Message{
        val date = Date(timeStamp)
        val formattedDate = DateFormat
            .getDateInstance(DateFormat.DEFAULT)
            .format(date)

        return Message(
            messageText = messageText,
            username = username,
            formattedTime = formattedDate
        )
    }
}
