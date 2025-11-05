package com.hackhathon.mentalhealthapp.addNoteScreen

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hackhathon.data.models.Emotion
import com.hackhathon.features.R
import com.hackhathon.mentalhealthapp.chatGptScreen.GptViewModel
import com.hackhathon.ui_kit.theme.MainPurpleColor

private lateinit var noteViewModel: NoteViewModel
private lateinit var gptViewModel: GptViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteDialog(
    onDismiss: () -> Unit,
    onSave: (String) -> Unit,
    onBackClick: () -> Unit
) {
    noteViewModel = hiltViewModel()
    gptViewModel = hiltViewModel()
    val noteText = noteViewModel.noteText

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(modifier = Modifier.fillMaxSize().offset(y = 20.dp)) {
            DialogContent(
                noteText = noteText,
                onNoteTextChange = { newText -> noteViewModel.updateNoteText(newText) },
                selectedEmotions = noteViewModel.selectedEmotions,
                onEmotionSelected = { emotion -> noteViewModel.onEmotionSelected(emotion) },
                onDiscussWithAI = {
                    gptViewModel.requestToGpt(noteViewModel.noteText)
                    noteViewModel.clearNoteText()
                    navigateToChatActivity(onBackClick)
                    onDismiss()
                },
                onSaveNote = {
                    if (noteText.isNotBlank()) {
                        noteViewModel.addNewNote()
                        noteViewModel.clearNoteText()
                        onDismiss()
                    }
                },
                onDismiss = onDismiss
            )
        }
    }
}

@Composable
private fun DialogContent(
    noteText: String,
    onNoteTextChange: (String) -> Unit,
    selectedEmotions: List<Emotion>,
    onEmotionSelected: (Emotion) -> Unit,
    onDiscussWithAI: () -> Unit,
    onSaveNote: () -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            )
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0x78C3FFD4),
                        Color(0x78CFCCFB),
                        Color(0x78EFF9F2)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                ),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            )
            .padding(start = 14.dp, end = 14.dp)
    ) {
        DialogHeader(onDismiss = onDismiss)

        NoteInputSection(
            noteText = noteText,
            onNoteTextChange = onNoteTextChange
        )

        EmotionsSection(
            selectedEmotions = selectedEmotions,
            onEmotionSelected = onEmotionSelected
        )

        Spacer(modifier = Modifier.weight(1f))

        ActionButtons(
            noteText = noteText,
            onDiscussWithAI = onDiscussWithAI,
            onSaveNote = onSaveNote
        )

        Spacer(modifier = Modifier.size(34.dp))
    }
}

@Composable
private fun DialogHeader(onDismiss: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 21.dp, top = 36.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onDismiss,
            modifier = Modifier.padding(end = 16.dp).size(24.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.left_arrow),
                contentDescription = "Назад",
                tint = Color.Black
            )
        }

        Text(
            text = "Добавить заметку",
            fontFamily = FontFamily(Font(resId = R.font.inter_medium)),
            fontSize = 18.sp,
            color = Color.Black
        )
    }
}

@Composable
private fun NoteInputSection(
    noteText: String,
    onNoteTextChange: (String) -> Unit
) {
    Text(
        text = "Чем хочешь поделиться сегодня?",
        fontFamily = FontFamily(Font(resId = R.font.inter_semi_bold)),
        fontSize = 14.sp,
        color = Color.Black,
        modifier = Modifier.padding(bottom = 12.dp)
    )

    BasicTextField(
        value = noteText,
        onValueChange = onNoteTextChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(292.dp)
            .background(Color.White, shape = RoundedCornerShape(14.dp)),
        decorationBox = { innerTextField ->
            Box(modifier = Modifier.fillMaxSize()) {
                if (noteText.isEmpty()) {
                    Text(
                        "Введите текст заметки...",
                        color = Color.Gray,
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        fontFamily = FontFamily(Font(resId = R.font.inter_regular)),
                        modifier = Modifier.padding(top = 14.dp, start = 20.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 14.dp, start = 20.dp, end = 20.dp, bottom = 14.dp)
                ) {
                    innerTextField()
                }
            }
        },
        textStyle = LocalTextStyle.current.copy(
            fontSize = 16.sp,
            lineHeight = 20.sp,
            fontFamily = FontFamily(Font(resId = R.font.inter_regular))
        )
    )
}

@Composable
private fun EmotionsSection(
    selectedEmotions: List<Emotion>,
    onEmotionSelected: (Emotion) -> Unit
) {
    Text(
        text = "Попробуй отметить свои эмоции",
        fontFamily = FontFamily(Font(resId = R.font.inter_semi_bold)),
        fontSize = 14.sp,
        color = Color.Black,
        modifier = Modifier.padding(bottom = 12.dp, top = 32.dp)
    )

    EmotionsSelector(
        selectedEmotions = selectedEmotions,
        onEmotionSelected = onEmotionSelected
    )
}

@Composable
private fun ActionButtons(
    noteText: String,
    onDiscussWithAI: () -> Unit,
    onSaveNote: () -> Unit
) {
    Button(
        onClick = onDiscussWithAI,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MainPurpleColor,
            disabledContainerColor = MainPurpleColor,
        ),
        enabled = noteText.isNotBlank()
    ) {
        Text(
            text = "Обсудить с AI",
            color = Color.White,
            fontSize = 16.sp,
            fontFamily = FontFamily(
                Font(resId = R.font.inter_regular, weight = FontWeight.Bold)
            )
        )
    }

    Button(
        onClick = onSaveNote,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent
        ),
        enabled = noteText.isNotBlank()
    ) {
        Text(
            text = "Сохранить",
            color = MainPurpleColor,
            fontSize = 16.sp,
            fontFamily = FontFamily(
                Font(resId = R.font.inter_regular, weight = FontWeight.Bold)
            )
        )
    }
}

private fun navigateToChatActivity(onBackClick: () -> Unit) {
    onBackClick()
}