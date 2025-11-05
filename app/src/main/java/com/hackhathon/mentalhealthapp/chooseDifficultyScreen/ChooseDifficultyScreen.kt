package com.hackhathon.mentalhealthapp.chooseDifficultyScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hackhathon.features.R
import com.hackhathon.mentalhealthapp.breathingScreen.BreathingViewModel
import com.hackhathon.ui_kit.theme.MainPurpleColor

@Composable
fun ChooseDifficultyScreen(
    onNext : () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: BreathingViewModel
){
    var selectedDifficulty by remember { mutableStateOf(1) }

    val difficultyTexts = listOf(
        "Короткая практика для начинающих с базовыми техниками",
        "Умеренная по длительности практика с более глубоким погружением",
        "Продолжительная практика для опытных пользователей с продвинутыми техниками"
    )

    val difficultyImages = listOf(
        R.drawable.left_choose_dif,
        R.drawable.mid_choose_dif,
        R.drawable.right_choose_dif
    )

    val difficultyDurations = listOf(8, 16, 24)

    LaunchedEffect(selectedDifficulty) {
        viewModel.selectDifficulty(difficultyDurations[selectedDifficulty])
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(198, 213, 248, 128))
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    painter = painterResource(R.drawable.left_arrow),
                    contentDescription = "Назад",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Text(
            text = "Выберите сложность\nпрактики",
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.inter_medium)),
            color =Color(16, 15, 17, 191),
            textAlign = TextAlign.Center,
        )

        Image(
            painter = painterResource(difficultyImages[selectedDifficulty]),
            contentDescription = "Сложность: ${when(selectedDifficulty) {
                0 -> "Новичок"
                1 -> "Средний"
                else -> "Опытный"
            }}",
            modifier = Modifier.size(306.dp)
        )

        FilterDifficulty(
            selectedFilter = selectedDifficulty,
            onFilterSelected = { selectedDifficulty = it }
        )

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.TopCenter
        ) {
            Text(
                text = difficultyTexts[selectedDifficulty],
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.inter_regular)),
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }

        Button(
            onClick = onNext,
            modifier = Modifier
                .padding(horizontal = 14.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(32.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MainPurpleColor,
                disabledContainerColor = Color.Gray,
            ),
        ) {
            Text(
                text = "Продолжить",
                color = Color.White,
                fontSize = 16.sp,
                fontFamily = FontFamily(
                    Font(resId = R.font.inter_semi_bold, weight = FontWeight.Bold)
                )
            )
        }
    }
}

@Composable
fun FilterDifficulty(
    selectedFilter: Int,
    onFilterSelected: (Int) -> Unit
) {
    val filters = listOf("Новичок", "Средний", "Опытный")

    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp)
            .background(Color.White, RoundedCornerShape(30.dp))
    ) {
        filters.forEachIndexed { index, label ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null
                    ) { onFilterSelected(index) }
                    .padding(4.dp)
                    .background(
                        if (index == selectedFilter) MainPurpleColor else Color.Transparent,
                        shape = RoundedCornerShape(30.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    color = if (index == selectedFilter) Color.White else Color.Gray,
                    fontFamily = FontFamily(Font(R.font.inter_semi_bold)),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(vertical = 6.dp)
                )
            }
        }
    }
}