package com.hackhathon.mentalhealthapp.addNoteScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hackhathon.data.models.Emotion
import com.hackhathon.features.R

@Composable
fun EmotionsSelector(
    selectedEmotions: List<Emotion>,
    onEmotionSelected: (Emotion) -> Unit,
    modifier: Modifier = Modifier
) {
    val emotions = remember {
        listOf(
            Emotion(1, "Радость"),
            Emotion(2, "Грусть"),
            Emotion(3, "Спокойствие"),
            Emotion(4, "Раздражение"),
            Emotion(5, "Вдохновение"),
            Emotion(6, "Тревога"),
            Emotion(7, "Усталость"),
            Emotion(8, "Скука"),
            Emotion(9, "Размышление"),
            Emotion(10, "Удовлетворение")
        )
    }

    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        maxItemsInEachRow = Int.MAX_VALUE
    ) {
        emotions.forEach { emotion ->
            val isSelected = selectedEmotions.any { it.id == emotion.id }

            EmotionItem(
                emotion = emotion,
                isSelected = isSelected,
                onEmotionSelected = onEmotionSelected,
                modifier = Modifier.widthIn(min = 100.dp, max = 150.dp)
            )
        }
    }
}

@Composable
fun EmotionItem(
    emotion: Emotion,
    isSelected: Boolean,
    onEmotionSelected: (Emotion) -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) {
        Color.White
    } else {
        Color.Transparent
    }

    val borderColor = if (isSelected) {
        Color.Transparent
    } else {
        Color.Black
    }

    Box(
        modifier = modifier
            .height(32.dp)
            .clip(RoundedCornerShape(36.dp))
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(36.dp)
            )
            .clickable { onEmotionSelected(emotion.copy(isSelected = !isSelected)) }
            .padding(horizontal = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = emotion.name,
            fontFamily = FontFamily(
                Font(
                    resId = R.font.inter_regular,
                )
            ),
            fontSize = 14.sp,
            color = Color.Black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}