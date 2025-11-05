package com.hackhathon.mentalhealthapp.startScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.hackhathon.features.R
import com.hackhathon.mentalhealthapp.profileScreen.ProfileViewModel
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.hackhathon.ui_kit.theme.MainPurpleColor
import com.hackhathon.ui_kit.theme.White

@Composable
fun StartScreen(
    onNavigateToMain: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    var currentScreen by remember { mutableIntStateOf(0) } // 0=onboarding, 1=name, 2=age, 3=gender

    when (currentScreen) {
        0 -> OnboardingScreen { currentScreen = 1 }
        1 -> NameInputScreen(
            onNext = { currentScreen = 2 },
            viewModel = viewModel
        )

        2 -> AgeInputScreen(
            onNext = { currentScreen = 3 },
            viewModel = viewModel
        )

        3 -> GenderSelectionScreen(
            onSave = { viewModel.saveUserData()
                     onNavigateToMain()},
            viewModel = viewModel
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnboardingScreen(onStartClicked: () -> Unit) {
    val pagerState = rememberPagerState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(
                colors = listOf(
                    Color(28, 97, 255, 51),
                    Color(163, 88, 207, 51),
                    Color(255, 134, 78, 51)
                ),
                start = Offset(0f, 0f),
                end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
            ))
            .systemBarsPadding()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center) {
            Icon(
                painter = painterResource(R.drawable.ic_start_icon),
                contentDescription = "Кайфы",
                tint = MainPurpleColor
            )
        }

        HorizontalPager(
            count = 3,
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            when (page) {
                0 -> FirstOnboardingPage()
                1 -> MiddleOnboardingPage()
                2 -> LastOnboardingPage()
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.Start
            ) {
                for (i in 0..2) {
                    IndicatorDot(
                        isSelected = i == pagerState.currentPage,
                        modifier = Modifier.padding(end = 3.dp)
                    )
                }
            }

            if (pagerState.currentPage == 2) {
                Button(
                    onClick = onStartClicked,
                    modifier = Modifier
                        .height(50.dp),
                    shape = RoundedCornerShape(32.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MainPurpleColor,
                        disabledContainerColor = MainPurpleColor,
                    ),

                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 14.dp),
                        text = "Начать",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontFamily = FontFamily(
                            Font(resId = R.font.inter_semi_bold, weight = FontWeight.Bold)
                        )
                    )
                }

            }
        }
    }
}

@Composable
fun FirstOnboardingPage(){
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(top = 28.dp, bottom = 6.dp),
            text = "AI Дневник",
            fontSize = 40.sp,
            fontFamily = FontFamily(Font(R.font.inter_bold)),
            color = Color(16, 15, 17, 204)
        )

        Text(
            modifier = Modifier.padding(),
            text = "Место, где мысли находят\nотклик",
            fontSize = 18.sp,
            fontFamily = FontFamily(Font(R.font.inter_medium)),
            color = Color(16, 15, 17, 204),
            textAlign = TextAlign.Center
        )

        Box(modifier = Modifier.padding(horizontal = 20.dp).fillMaxSize(),
            contentAlignment = Alignment.TopCenter) {

            Image(
                painter = painterResource(R.drawable.first_start_page),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}

@Composable
fun MiddleOnboardingPage() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .padding(end =14.dp)
                .fillMaxWidth()
                .padding(top = 76.dp, bottom = 32.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                modifier = Modifier.background(
                    color = White,
                    shape = RoundedCornerShape(16.dp)
                )
                    .padding(vertical = 6.dp, horizontal = 12.dp),
                text = "Здесь ты можешь записывать\nсвои мысли и чувства",
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.inter_regular)),
                color = Color(35, 26, 118),
                textAlign = TextAlign.End
            )
        }

        Row(
            modifier = Modifier.padding(start =14.dp).fillMaxWidth()

        ){

            Text(
                modifier = Modifier.background(
                    color = White,
                    shape = RoundedCornerShape(16.dp)
                )
                    .padding(vertical = 6.dp, horizontal = 12.dp),
                text = "AI Дневник поможет\nпроанализировать твои записи",
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.inter_regular)),
                color = Color(35, 26, 118),
                textAlign = TextAlign.Start
            )
        }

        Box(modifier = Modifier.padding(horizontal = 14.dp).fillMaxSize(),
            contentAlignment = Alignment.BottomCenter){
            Image(
                painter = painterResource(R.drawable.mid_start_page),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}

@Composable
fun LastOnboardingPage(
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Первая текстовая карточка
        Row(
            modifier = Modifier
                .padding(end = 14.dp)
                .fillMaxWidth()
                .padding(top = 57.dp, bottom = 27.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                modifier = Modifier.background(
                    color = White,
                    shape = RoundedCornerShape(16.dp)
                )
                    .padding(vertical = 6.dp, horizontal = 12.dp),
                text = "Выбирай медитации под\nнастроение, добавляй в\nизбранное и слушай их в удобном\nплеере",
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.inter_regular)),
                color = Color(35, 26, 118),
                textAlign = TextAlign.End
            )
        }

        Box(
            modifier = Modifier
                .padding(vertical = 14.dp)
                .fillMaxWidth()
                .height(300.dp)
                .padding(bottom = 28.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Image(
                painter = painterResource(R.drawable.last_start_page),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
            )
        }

        Row(
            modifier = Modifier
                .padding(start = 14.dp)
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier.background(
                    color = White,
                    shape = RoundedCornerShape(16.dp)
                )
                    .padding(vertical = 6.dp, horizontal = 12.dp),
                text = "Такие практики помогут отпустить\nнапряжение и перезагрузиться",
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.inter_regular)),
                color = Color(35, 26, 118),
                textAlign = TextAlign.Start
            )
        }
    }
}

@Composable
fun IndicatorDot(isSelected: Boolean, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .width(if (isSelected) 28.dp else 17.dp)
            .height(4.dp)
            .clip(CircleShape)
            .background(
                color = if (isSelected) Color(139, 76, 252)
                else Color(139, 76, 252, 128)
            )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NameInputScreen(onNext: () -> Unit, viewModel: ProfileViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Введите свое имя",
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.inter_medium)),
            modifier = Modifier.padding(bottom = 20.dp)
        )

        OutlinedTextField(
            shape = RoundedCornerShape(20.dp),
            value = viewModel.name,
            onValueChange = { viewModel.name = it },
            label = { Text("Имя") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        )

        Button(
            onClick = onNext,
            modifier = Modifier
                .height(50.dp),
            shape = RoundedCornerShape(32.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MainPurpleColor,
                disabledContainerColor = Color.Gray,
            ),
            enabled = viewModel.name.isNotBlank()

            ) {
            Text(
                modifier = Modifier.padding(horizontal = 14.dp),
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgeInputScreen(onNext: () -> Unit, viewModel: ProfileViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Отлично! Теперь возраст",
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.inter_medium)),
            modifier = Modifier.padding(bottom = 20.dp)
        )

        OutlinedTextField(
            shape = RoundedCornerShape(20.dp),
            value = viewModel.age,
            onValueChange = { viewModel.age = it },
            label = { Text("Возраст") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        )

        Button(
            onClick = onNext,
            modifier = Modifier
                .height(50.dp),
            shape = RoundedCornerShape(32.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MainPurpleColor,
                disabledContainerColor = Color.Gray,
            ),
            enabled = viewModel.age.isNotBlank()
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 14.dp),
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
fun GenderSelectionScreen(onSave: () -> Unit, viewModel: ProfileViewModel) {
    var selectedGender by remember { mutableStateOf(viewModel.gender) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Укажи свой пол",
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.inter_medium)),
            modifier = Modifier.padding(bottom = 20.dp)
        )

        GenderOption(
            text = "Мужчина",
            isSelected = selectedGender == "Мужчина",
            onSelect = {
                selectedGender = "Мужчина"
                viewModel.gender = "Мужчина"
            }
        )

        GenderOption(
            text = "Женщина",
            isSelected = selectedGender == "Женщина",
            onSelect = {
                selectedGender = "Женщина"
                viewModel.gender = "Женщина"
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onSave,
            modifier = Modifier
                .height(50.dp),
            shape = RoundedCornerShape(32.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MainPurpleColor,
                disabledContainerColor = Color.Gray,
            ),
            enabled = selectedGender.isNotBlank()
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 14.dp),
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
fun GenderOption(text: String, isSelected: Boolean, onSelect: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onSelect
        )
        Text(
            text = text,
            modifier = Modifier.padding(start = 8.dp),
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.inter_semi_bold))
        )
    }
}