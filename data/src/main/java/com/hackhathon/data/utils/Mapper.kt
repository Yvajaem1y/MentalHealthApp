package com.hackhathon.data.utils

import com.hackhathon.data.models.Emotion
import com.hackhathon.data.models.Message
import com.hackhathon.data.models.Note
import com.hackhathon.local_database.models.EmotionDBO
import com.hackhathon.local_database.models.MessageDBO
import com.hackhathon.local_database.models.NoteDBO
import java.text.SimpleDateFormat
import java.util.Date

private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

internal fun Message.toMessageDBO(): MessageDBO {
    return MessageDBO(
        id = 0,
        senderDBO = sender,
        textDBO = text,
        dateDBO = dateFormat.format(date)
    )
}

internal fun MessageDBO.toMessage(): Message {
    return Message(
        sender = senderDBO,
        text = textDBO,
        date = dateFormat.parse(dateDBO) ?: Date()
    )
}

internal fun Note.toNoteDBO(): NoteDBO {
    return NoteDBO(
        id= 0,
        date = date,
        emotions = emotions.toEmotionDBOList(),
        noteText = noteText
    )
}

internal fun NoteDBO.toNote(): Note {
    return Note(
        date = date,
        emotions = emotions.toEmotionList(),
        noteText = noteText
    )
}

internal fun Emotion.toEmotionDBO(): EmotionDBO {
    return EmotionDBO(
        id = id,
        name = name
    )
}

internal fun EmotionDBO.toEmotion(): Emotion {
    return Emotion(
        id = id,
        name = name
    )
}

internal fun List<Emotion>.toEmotionDBOList(): List<EmotionDBO> {
    return this.map { it.toEmotionDBO() }
}

internal fun List<EmotionDBO>.toEmotionList(): List<Emotion> {
    return this.map { it.toEmotion() }
}