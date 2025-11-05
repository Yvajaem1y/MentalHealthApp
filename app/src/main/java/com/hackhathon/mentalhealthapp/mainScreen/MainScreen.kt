package com.hackhathon.mentalhealthapp.mainScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.hackhathon.data.models.Message
import com.hackhathon.data.models.Note
import com.hackhathon.mentalhealthapp.addNoteScreen.AddNoteDialog
import com.hackhathon.mentalhealthapp.addNoteScreen.NoteState
import com.hackhathon.mentalhealthapp.addNoteScreen.NoteViewModel
import com.hackhathon.mentalhealthapp.chatGptScreen.EmptyScreen
import com.hackhathon.mentalhealthapp.chatGptScreen.ErrorScreen
import com.hackhathon.mentalhealthapp.chatGptScreen.GptViewModel
import com.hackhathon.mentalhealthapp.chatGptScreen.MessageState
import com.hackhathon.mentalhealthapp.chatGptScreen.LoadingScreen
import com.hackhathon.ui_kit.theme.LightPurpleColor
import com.hackhathon.ui_kit.theme.MainPurpleColor
import com.hackhathon.ui_kit.theme.White
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import com.hackhathon.features.R
import com.hackhathon.mentalhealthapp.profileScreen.DefaultAvatarImage
import com.hackhathon.mentalhealthapp.profileScreen.ProfileViewModel
import com.hackhathon.mentalhealthapp.profileScreen.toRequestResult

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MainScreen(
    onNavigateToChat: () -> Unit,
    messageViewModel: GptViewModel,
    noteViewModel: NoteViewModel,
    mainScreenViewModel: MainScreenViewModel,
    profileViewModel: ProfileViewModel
) {
    val dates by mainScreenViewModel.dates
    val selectedDateIndex by mainScreenViewModel.selectedDateIndex
    val showAddNoteDialog by mainScreenViewModel.showAddNoteDialog

    val messagesState = messageViewModel.messagesGptRequestState.collectAsState()
    val noteState = noteViewModel.todayNotesState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0x32BACFFF), Color(0x32FFCEB7)),
                    start = Offset(0f, 0f),
                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                )
            )
    ) {
        HeaderSection(dates, selectedDateIndex, mainScreenViewModel, profileViewModel)
        FeelingSection(
            showAddNoteDialog = showAddNoteDialog,
            onBackClick = onNavigateToChat,
            onShowAddNoteDialogChange = { show ->
                if (show) mainScreenViewModel.showAddNoteDialog()
                else mainScreenViewModel.hideAddNoteDialog()
            },
            onSaveNote = {
                noteViewModel.addNewNote()
                mainScreenViewModel.hideAddNoteDialog()
            }
        )
        DiarySection(messagesState.value, onNavigateToChat)
        NotesSection(noteState.value)
    }
}

@Composable
private fun HeaderSection(dates: List<DateItem>,
                          selectedDateIndex: Int,
                          mainScreenViewModel: MainScreenViewModel,
                          profileViewModel: ProfileViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color(0x32C6D3F3),
                shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(bottom = 16.dp, top = 8.dp, start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileImage(profileViewModel)
            GreetingText(profileViewModel)
        }
        CalendarSection(
            dates = dates,
            selectedDateIndex = selectedDateIndex,
            onDateSelected = { index ->
                mainScreenViewModel.selectDate(index)
            }
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun ProfileImage(viewModel : ProfileViewModel) {
    val imageUri = viewModel.userImageUri.value

    if (imageUri != null) {
        GlideImage(
            model = imageUri,
            contentDescription = "Profile photo",
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
        )
    } else {
        DefaultAvatarImage(modifier = Modifier.size(44.dp)
            .clip(CircleShape)
            .background(White))
    }
}

@Composable
private fun GreetingText(profileViewModel: ProfileViewModel) {
    Row(modifier = Modifier.padding(12.dp)) {
        Text(
            text = "Привет,",
            fontFamily = FontFamily(Font(resId = R.font.inter_medium, weight = FontWeight.Medium)),
            fontSize = 18.sp,
            lineHeight = 18.sp,
            letterSpacing = (-0.01).em
        )
        Text(
            text = " ${profileViewModel.userDataState.value.toRequestResult().data?.userName} \uD83D\uDC4B",
            fontFamily = FontFamily(Font(resId = R.font.inter_bold, weight = FontWeight.Bold)),
            fontSize = 18.sp,
            lineHeight = 18.sp,
            letterSpacing = (-0.01).em
        )
    }
}

@Composable
private fun FeelingSection(
    showAddNoteDialog: Boolean,
    onBackClick: () -> Unit,
    onShowAddNoteDialogChange: (Boolean) -> Unit,
    onSaveNote: (String) -> Unit
) {
    Text(
        text = "Как ты сегодня себя чувствуешь?",
        modifier = Modifier.padding(start = 18.dp, top = 32.dp, bottom = 16.dp),
        fontFamily = FontFamily(Font(resId = R.font.inter_semi_bold, weight = FontWeight.Bold)),
        fontSize = 14.sp,
        lineHeight = 18.sp,
        letterSpacing = (-0.01).em
    )

    Row(
        modifier = Modifier
            .size(186.dp, 40.dp)
            .padding(start = 18.dp)
            .background(MainPurpleColor, shape = RoundedCornerShape(200.dp))
            .clip(RoundedCornerShape(8.dp))
            .clickable { onShowAddNoteDialogChange(true) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.plus),
            contentDescription = "Add note",
            modifier = Modifier.padding(start = 16.dp, end = 4.dp)
        )
        Text(
            text = "Добавить заметку",
            fontFamily = FontFamily(Font(resId = R.font.inter_regular, weight = FontWeight.Bold)),
            color = Color.White,
            fontSize = 14.sp,
            lineHeight = 18.sp,
            letterSpacing = (-0.01).em
        )
    }

    if (showAddNoteDialog) {
        AddNoteDialog(
            onDismiss = { onShowAddNoteDialogChange(false) },
            onSave = { noteText ->
                onSaveNote(noteText)
            },
            onBackClick = onBackClick
        )
    }
}

@Composable
private fun DiarySection(messagesState: MessageState, onNavigateToChat: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 18.dp, end = 18.dp, top = 32.dp, bottom = 16.dp),
        Arrangement.SpaceBetween
    ) {
        Text(
            text = "Дневник AI",
            fontFamily = FontFamily(Font(resId = R.font.inter_semi_bold, weight = FontWeight.Bold)),
            fontSize = 14.sp,
            lineHeight = 18.sp,
            letterSpacing = (-0.01).em
        )
        OpenFullChatText(onNavigateToChat)
    }

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 18.dp, end = 18.dp)
    ) {
        when (messagesState) {
            is MessageState.Success -> ListMessagesScreen(messagesState.messages)
            is MessageState.Error -> ErrorScreen(messagesState.messages)
            is MessageState.Loading -> LoadingScreen(messagesState.messages)
            is MessageState.None -> EmptyScreen()
        }
    }
}

@Composable
private fun OpenFullChatText(onNavigateToChat: () -> Unit) {
    Text(
        text = "Открыть полный чат",
        fontFamily = FontFamily(Font(resId = R.font.inter_regular, weight = FontWeight.Bold)),
        fontSize = 14.sp,
        lineHeight = 18.sp,
        letterSpacing = (-0.01).em,
        color = Color(0x80101111),
        modifier = Modifier
            .clickable {
                onNavigateToChat()
            }
            .padding(4.dp)
    )
}

@Composable
private fun NotesSection(noteState: NoteState) {
    Text(
        text = "Мои заметки",
        modifier = Modifier.padding(start = 18.dp, top = 32.dp),
        fontFamily = FontFamily(Font(resId = R.font.inter_semi_bold, weight = FontWeight.Bold)),
        fontSize = 14.sp,
        lineHeight = 18.sp,
        letterSpacing = (-0.01).em
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 18.dp, end = 18.dp)
    ) {
        when (noteState) {
            is NoteState.Success -> ListNoteScreen(noteState.notes)
            is NoteState.Error -> ErrorScreen(noteState.notes)
            is NoteState.Loading -> NoteDuringUpdate(noteState.notes)
            is NoteState.None -> EmptyScreen()
        }
    }
}

@Composable
fun CalendarSection(
    dates: List<DateItem>,
    selectedDateIndex: Int,
    onDateSelected: (Int) -> Unit
) {
    val listState = rememberLazyListState()
    val initialScrollDone = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!initialScrollDone.value) {
            listState.animateScrollToItem(selectedDateIndex)
            initialScrollDone.value = true
        }
    }

    LazyRow(
        state = listState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp, start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        itemsIndexed(dates) { index, dateItem ->
            DateItemView(
                dateItem = dateItem,
                onClick = { onDateSelected(index) }
            )
        }
    }
}

@Composable
fun DateItemView(dateItem: DateItem, onClick: () -> Unit) {
    val backgroundColor = when {
        dateItem.isSelected -> MainPurpleColor
        dateItem.isToday -> LightPurpleColor
        else -> White.copy(alpha = 0.7f)
    }

    val textColor = if (dateItem.isSelected || dateItem.isToday) Color.White else Color.Black
    val topTextColor = if (dateItem.isSelected || dateItem.isToday) Color.White else Color(0x80101111)

    Box(
        modifier = Modifier
            .size(height = 53.dp, width = 38.dp)
            .clip(RoundedCornerShape(100.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(horizontal = 3.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = dateItem.dayOfWeek,
                fontSize = 12.sp,
                color = topTextColor,
                fontFamily = FontFamily(Font(resId = R.font.inter_regular, weight = FontWeight.Medium)),
                modifier = Modifier.padding(bottom = 0.dp)
            )
            Text(
                text = dateItem.date,
                fontSize = 14.sp,
                color = textColor,
                fontFamily = FontFamily(Font(resId = R.font.inter_regular, weight = FontWeight.Bold))
            )
        }
    }
}

private fun generateDates(): List<DateItem> {
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("d", Locale.getDefault())
    val dayFormat = SimpleDateFormat("E", Locale.getDefault())

    val totalDays = 61
    val todayIndex = 30

    calendar.add(Calendar.DAY_OF_YEAR, -30)

    return List(totalDays) { index ->
        val currentDate = calendar.time
        val date = dateFormat.format(currentDate)
        val dayOfWeek = formatDayOfWeek(dayFormat.format(currentDate))

        val today = Calendar.getInstance()
        val isToday = calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                calendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                calendar.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)

        val isSelected = index == todayIndex

        val result = DateItem(
            date = date,
            dayOfWeek = dayOfWeek,
            fullDate = calendar.clone() as Calendar,
            isSelected = isSelected,
            isToday = isToday
        )

        calendar.add(Calendar.DAY_OF_YEAR, 1)
        result
    }
}

private fun formatDayOfWeek(day: String): String = when (day) {
    "Mon" -> "Пн"
    "Tue" -> "Вт"
    "Wed" -> "Ср"
    "Thu" -> "Чт"
    "Fri" -> "Пт"
    "Sat" -> "Сб"
    "Sun" -> "Вс"
    else -> day.take(2)
}

@Composable
private fun ListMessagesScreen(
    messages: List<Message>,
    listState: LazyListState = rememberLazyListState()
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color(0xFFCCCCCC).copy(alpha = 0.8f),
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        if (messages.size >= 2) {
            val lastTwoMessages = listOf(messages[messages.size - 2], messages[messages.size - 1])
            LazyColumn(state = listState) {
                items(lastTwoMessages) { message ->
                    if (message.sender == "server") ServerMessageItem(message)
                    else UserMessageItem(message)
                }
            }
        } else {
            EmptyDiaryState()
        }
    }
}

@Composable
private fun EmptyDiaryState() {
    Column(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Пусто...",
            fontSize = 12.sp,
            color = Color.Black,
            fontFamily = FontFamily(Font(resId = R.font.inter_regular, weight = FontWeight.Medium))
        )
        Text(
            text = "Сегодня ты еще не общался с AI",
            fontSize = 12.sp,
            color = Color.Black,
            fontFamily = FontFamily(Font(resId = R.font.inter_regular, weight = FontWeight.Medium))
        )
    }
}

@Composable
private fun UserMessageItem(message: Message) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Box(
            modifier = Modifier
                .wrapContentWidth()
                .background(
                    color = MainPurpleColor,
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
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
private fun ServerMessageItem(message: Message) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .background(
                    color = Color.White,
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
private fun ListNoteScreen(
    notes: List<Note>,
    listState: LazyListState = rememberLazyListState()
) {
    Box(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color(0xFFCCCCCC).copy(alpha = 0.8f),
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        if (notes.isNotEmpty()) {
            LazyColumn(state = listState) {
                items(notes) { note ->
                    NoteItem(note)
                }
            }
        } else {
            EmptyNotesState()
        }
    }
}

@Composable
private fun NoteItem(note: Note) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .wrapContentWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Text(
            text = note.noteText,
            color = Color.Black,
            fontSize = 12.sp,
            fontFamily = FontFamily(Font(resId = R.font.inter_regular, weight = FontWeight.Bold)),
            textAlign = TextAlign.End,
            modifier = Modifier.padding(12.dp)
        )
    }
}

@Composable
private fun EmptyNotesState() {
    Column(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Пусто...",
            fontSize = 12.sp,
            color = Color.Black,
            fontFamily = FontFamily(Font(resId = R.font.inter_regular, weight = FontWeight.Medium))
        )
        Text(
            text = "Сегодня еще не было заметок",
            fontSize = 12.sp,
            color = Color.Black,
            fontFamily = FontFamily(Font(resId = R.font.inter_regular, weight = FontWeight.Medium))
        )
    }
}

@Composable
internal fun NoteDuringUpdate(notes: List<Note>?) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .systemBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        if (notes != null) {
            ListNoteScreen(notes)
        } else {
            CircularProgressIndicator()
        }
    }
}

@Composable
internal fun ErrorScreen(notes: List<Note>?) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .systemBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        Text("Error")
    }
}