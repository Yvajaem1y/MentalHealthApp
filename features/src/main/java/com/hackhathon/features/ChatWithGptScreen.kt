package com.hackhathon.features

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hackhathon.data.GPTRequestResult
import com.hackhathon.data.models.Message

lateinit var vm : GptViewModel

@Composable
fun ChatWithGptScreen(){
    vm = viewModel()
    val messagesState = vm.messagesGptRequestState.collectAsState()
    val gptRequestState = vm.gptRequestStateRequestToGpt.collectAsState()
    var textValue by remember { mutableStateOf("") }

    val listState = rememberLazyListState()

    LaunchedEffect(messagesState.value) {
        if (messagesState.value is MessageState.Success) {
            val messages = (messagesState.value as MessageState.Success).messages
            if (messages.isNotEmpty()) {
                listState.scrollToItem(messages.size - 1)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Box(modifier = Modifier.weight(1.0f)) {
            when (val curState = messagesState.value) {
                is MessageState.Success -> {
                    ListMessagesScreen(curState.messages, listState)
                }
                is MessageState.Error -> {
                    ErrorScreen(curState.messages)
                }
                is MessageState.Loading -> {
                    MessagesDuringUpdate(curState.messages)
                }
                is MessageState.None -> {
                    EmptyScreen()
                }
            }
        }

        Box(){
            when (val curState = gptRequestState.value){
                is GptRequestState.Success -> {
                    textValue = ""
                    ChatInputField(textValue = textValue, onTextChange = { textValue = it })
                }
                is GptRequestState.None-> ChatInputField(textValue = textValue, onTextChange = { textValue = it })
                is GptRequestState.Error-> {
                    ChatInputField(textValue = textValue, onTextChange = { textValue = it })
                }
                is GptRequestState.Loading-> LoadingChatTextField(textValue = textValue, onTextChange = { textValue = it })
            }
        }
    }
}

@Composable
private fun ChatInputField(
    textValue: String,
    onTextChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 45.dp, start = 30.dp, end = 20.dp)
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        OutlinedTextField(
            value = textValue,
            onValueChange = onTextChange,
            modifier = Modifier
                .weight(1f)
                .padding(end = 14.dp)
                .heightIn(min = 56.dp),
            placeholder = {
                Text(
                    "Спроси у ИИ совет или поделись мыслями...",
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )
            },
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(
                fontSize = 14.sp,
                textAlign = TextAlign.Start
            ),
            shape = RoundedCornerShape(13.dp),
        )

        Button(
            onClick = { vm.requestToGpt(textValue) },
            modifier = Modifier.size(41.dp),
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp)
        ) {
        }
    }
}

@Composable
private fun LoadingChatTextField(
    textValue: String,
    onTextChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 45.dp, start = 30.dp, end = 20.dp)
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        OutlinedTextField(
            value = textValue,
            onValueChange = onTextChange,
            modifier = Modifier
                .weight(1f)
                .padding(end = 14.dp)
                .heightIn(min = 56.dp),
            placeholder = {
                Text(
                    "ИИ генерирует ответ...",
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )
            },
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(
                fontSize = 14.sp,
                textAlign = TextAlign.Start
            ),
            shape = RoundedCornerShape(13.dp),
            enabled = false, // Полностью блокируем ввод
        )

        Button(
            onClick = { /* ничего не делаем - кнопка заблокирована */ },
            modifier = Modifier.size(41.dp),
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp),
            enabled = false // Блокируем кнопку
        ) {
            // Всегда показываем прогресс-бар
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp,
                color = Color.White
            )
        }
    }
}

@Composable
private fun EmptyScreen(){
    Box(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
        .systemBarsPadding(),
        contentAlignment = Alignment.Center){
        Text(text="Empty")
    }
}

@Composable
private fun ListMessagesScreen(
    messages: List<Message>,
    listState: LazyListState = rememberLazyListState() // Добавляем параметр с значением по умолчанию
){
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState // Передаем состояние в LazyColumn
    ) {
        items(messages){ message ->
            if (message.sender.equals("server")) ServerMessageItem(message)
            else UserMessageItem(message)
        }
    }
}

@Composable
private fun UserMessageItem(message: Message) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = 16.dp,
                        bottomEnd = 1.dp
                    )
                )
                .padding(horizontal = 16.dp, vertical = 12.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = message.text,
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
private fun ServerMessageItem(message: Message){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = 1.dp,
                        bottomEnd = 16.dp
                    )
                )
                .padding(horizontal = 16.dp, vertical = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = message.text,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
        }
    }
}
@Composable
fun MessagesDuringUpdate(messages: List<Message>?) {
    val listState = rememberLazyListState()
    Box(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
        .systemBarsPadding(),
        contentAlignment = Alignment.Center) {
        if (messages != null) {
            ListMessagesScreen(messages, listState)
        } else CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreen(messages: List<Message>?) {
    val listState = rememberLazyListState()
    Box(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
        .systemBarsPadding(),
        contentAlignment = Alignment.Center) {
        if (messages != null) {
            ListMessagesScreen(messages, listState)
        }  else Text("Error")
    }
}