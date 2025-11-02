package com.hackhathon.mentalhealthapp.chatGptScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hackhathon.data.models.Message
import com.hackhathon.features.R
import com.hackhathon.ui_kit.theme.MainPurpleColor

private lateinit var viewModel: GptViewModel

@Composable
fun ChatWithGptScreen() {
    viewModel = viewModel()
    val messagesState = viewModel.messagesGptRequestState.collectAsState()
    val gptRequestState = viewModel.gptRequestStateRequestToGpt.collectAsState()
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
            .systemBarsPadding()
    ) {
        Box(modifier = Modifier.weight(1f)) {
            when (val currentState = messagesState.value) {
                is MessageState.Success -> MessageListScreen(currentState.messages, listState)
                is MessageState.Error -> ErrorScreen(currentState.messages)
                is MessageState.Loading -> LoadingScreen(currentState.messages)
                is MessageState.None -> EmptyScreen()
            }
        }

        Box {
            when (val currentState = gptRequestState.value) {
                is GptRequestState.Success -> {
                    textValue = ""
                    ChatInputField(textValue, { textValue = it })
                }
                is GptRequestState.None -> ChatInputField(textValue, { textValue = it })
                is GptRequestState.Error -> ChatInputField(textValue, { textValue = it })
                is GptRequestState.Loading -> LoadingInputField(textValue, { textValue = it })
            }
        }
    }
}

@Composable
private fun ChatInputField(
    textValue: String,
    onTextChange: (String) -> Unit
) {
    InputFieldLayout(
        textValue = textValue,
        onTextChange = onTextChange,
        placeholder = "Поделись мыслями с AI",
        enabled = true,
        showProgress = false,
        onSendClick = { viewModel.requestToGpt(textValue) }
    )
}

@Composable
private fun LoadingInputField(
    textValue: String,
    onTextChange: (String) -> Unit
) {
    InputFieldLayout(
        textValue = textValue,
        onTextChange = onTextChange,
        placeholder = "ИИ генерирует ответ...",
        enabled = false,
        showProgress = true,
        onSendClick = { }
    )
}

@Composable
private fun InputFieldLayout(
    textValue: String,
    onTextChange: (String) -> Unit,
    placeholder: String,
    enabled: Boolean,
    showProgress: Boolean,
    onSendClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 14.dp, end = 14.dp)
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        BasicTextField(
            value = textValue,
            onValueChange = onTextChange,
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp)
                .height(56.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(32.dp),
                    clip = false,
                    ambientColor = Color(0xFFBACFFF),
                    spotColor = Color(0xFFBACFFF)
                )
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(32.dp),
                    clip = false,
                    ambientColor = Color(0xFF769FFF),
                    spotColor = Color(0xFF769FFF)
                )
                .background(Color.White, shape = RoundedCornerShape(32.dp)),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (textValue.isEmpty()) {
                        Text(
                            placeholder,
                            color = Color.Gray,
                            fontSize = 16.sp,
                            fontFamily = InterMediumFont
                        )
                    }
                    innerTextField()
                }
            },
            textStyle = LocalTextStyle.current.copy(
                fontSize = 16.sp,
                textAlign = TextAlign.Start,
                fontFamily = InterMediumFont,
                color = Color.Black,
            ),
            singleLine = true,
            enabled = enabled
        )

        Button(
            onClick = onSendClick,
            modifier = Modifier.size(41.dp),
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp),
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(containerColor = MainPurpleColor)
        ) {
            if (showProgress) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = Color.White
                )
            } else {
                Image(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(R.drawable.send_message),
                    contentDescription = "send message",
                )
            }
        }
    }
}

@Composable
internal fun EmptyScreen() {
}

@Composable
private fun MessageListScreen(
    messages: List<Message>,
    listState: LazyListState = rememberLazyListState()
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (messages.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listState
            ) {
                items(messages) { message ->
                    if (message.sender == "server") {
                        ServerMessageItem(message)
                    } else {
                        UserMessageItem(message)
                    }
                }
            }
        } else {
            Text(
                text = "Здесь вы можете обсудить \nсвое эмоцианальное состояние",
                fontSize = 12.sp,
                color = Color.Black,
                fontFamily = InterMediumFont,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 0.dp),
            )
        }
    }
}

@Composable
private fun UserMessageItem(message: Message) {
    MessageBubble(
        message = message,
        backgroundColor = MainPurpleColor,
        textColor = MaterialTheme.colorScheme.onPrimary,
        alignment = Alignment.CenterEnd,
        corners = RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp,
            bottomStart = 16.dp,
            bottomEnd = 1.dp
        )
    )
}

@Composable
private fun ServerMessageItem(message: Message) {
    MessageBubble(
        message = message,
        backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
        textColor = MaterialTheme.colorScheme.onSurfaceVariant,
        alignment = Alignment.CenterStart,
        corners = RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp,
            bottomStart = 1.dp,
            bottomEnd = 16.dp
        )
    )
}

@Composable
private fun MessageBubble(
    message: Message,
    backgroundColor: Color,
    textColor: Color,
    alignment: Alignment,
    corners: RoundedCornerShape
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = if (alignment == Alignment.CenterEnd) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .wrapContentWidth()
                .background(color = backgroundColor, shape = corners)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            contentAlignment = alignment
        ) {
            Text(
                text = message.text,
                color = textColor,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = if (alignment == Alignment.CenterEnd) TextAlign.End else TextAlign.Start
            )
        }
    }
}

@Composable
internal fun LoadingScreen(messages: List<Message>?) {
    val listState = rememberLazyListState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .systemBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        if (messages != null) {
            MessageListScreen(messages, listState)
        } else {
            CircularProgressIndicator()
        }
    }
}

@Composable
internal fun ErrorScreen(messages: List<Message>?) {
    val listState = rememberLazyListState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .systemBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        if (messages != null) {
            MessageListScreen(messages, listState)
        } else {
            Text("Error")
        }
    }
}

private val InterMediumFont = FontFamily(
    Font(resId = R.font.inter_regular, weight = FontWeight.Medium)
)