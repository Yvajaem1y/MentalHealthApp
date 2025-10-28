package com.hackhathon.data.utils

import com.hackhathon.data.models.Message
import com.hackhathon.local_database.models.MessageDBO
import java.text.SimpleDateFormat
import java.util.Date

private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

internal fun Message.toMessageDBO(): MessageDBO {
    return MessageDBO(
        id = 0,
        senderDBO = sender,
        textDBO = text,
        dateDBO = dateFormat.format(date) // Теперь date - это Date
    )
}

internal fun MessageDBO.toMessage(): Message {
    return Message(
        sender = senderDBO,
        text = textDBO,
        date = dateFormat.parse(dateDBO) ?: Date()
    )
}