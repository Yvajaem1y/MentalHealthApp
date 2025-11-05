package com.hackhathon.mentalhealthapp.breathingScreen

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hackhathon.features.R
import com.hackhathon.ui_kit.theme.MainPurpleColor

@Composable
fun BreathingScreen(
    onNavigateBack: () -> Unit,
    viewModel : BreathingViewModel,
) {

    val technique by viewModel.selectedTechnique.collectAsState()
    val isActive by viewModel.isBreathingActive.collectAsState()
    val currentPhase by viewModel.currentPhase.collectAsState()
    val timeRemaining by viewModel.timeRemaining.collectAsState()
    val sessionProgress by viewModel.sessionProgress.collectAsState()
    val elapsedTime by viewModel.elapsedTime.collectAsState()
    val totalSessionTime by viewModel.totalSessionTime.collectAsState()

    if (technique == null) {
        Log.d("AAA","technique == null")
        onNavigateBack()
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(198, 213, 248, 128))
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
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

                Text(
                    text = technique!!.name,
                    fontFamily = FontFamily(Font(R.font.inter_medium)),
                    fontSize = 18.sp
                )

                IconButton(onClick = onNavigateBack) {
                    Icon(
                        painter = painterResource(R.drawable.ic_outline_heart),
                        contentDescription = "Назад",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }


            BreathingAnimation(
                phase = currentPhase,
                size = 200.dp,
                color = MainPurpleColor,
                modifier = Modifier.weight(1f)
            )

        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = timeRemaining.toString(),
                fontFamily = FontFamily(Font(R.font.inter_bold)),
                fontSize = 24.sp,
                color = Color(16, 15, 17, 190)
            )

            Text(
                text = getPhaseText(currentPhase),
                fontFamily = FontFamily(Font(R.font.inter_medium)),
                fontSize = 18.sp,
                color = Color(16, 15, 17, 190)
            )

            VideoLikeProgressBar(
                progress = sessionProgress,
                elapsedTime = elapsedTime,
                totalTime = totalSessionTime,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 20.dp)
                    .height(48.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (isActive) {
                Button(
                    onClick = { viewModel.stopBreathing() },
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Text("Остановить")
                }
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { viewModel.startBreathing() },
                        modifier = Modifier.fillMaxWidth(0.8f)
                    ) {
                        Text(if (elapsedTime > 0) "Продолжить" else "Начать дыхание")
                    }
                }
            }

            if (!isActive && sessionProgress >= 1f) {
                Text(
                    text = "✅ Сессия завершена!",
                    color = Color.Green,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}

@Composable
fun VideoLikeProgressBar(
    progress: Float,
    elapsedTime: Int,
    totalTime: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(Color.Gray.copy(alpha = 0.3f), RoundedCornerShape(2.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .height(4.dp)
                    .background(Color.Black, RoundedCornerShape(2.dp))
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = formatTime(elapsedTime),
                color = Color.Gray,
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.inter_medium))
            )
            Text(
                text = formatTime(totalTime),
                color = Color.Gray,
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.inter_medium))
            )
        }
    }
}

private fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%d:%02d", minutes, remainingSeconds)
}

@Composable
fun CycleSelector(
    currentCycles: Int,
    onCyclesChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Количество циклов",
            color = Color.Gray
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "$currentCycles",
                modifier = Modifier.padding(horizontal = 16.dp)
            )

        }
    }
}

@Composable
fun BreathingAnimation(phase: BreathingPhase, size: Dp, color: Color, modifier: Modifier) {

    val animatedSize by animateDpAsState(
        targetValue = when (phase) {
            BreathingPhase.INHALE -> size * 1.5f
            BreathingPhase.EXHALE -> size * 0.5f
            BreathingPhase.HOLD -> size * 1.5f
            BreathingPhase.HOLD_AFTER_EXHALE -> size * 0.5f
            else -> size
        },
        animationSpec = when (phase) {
            BreathingPhase.INHALE -> tween(durationMillis = 4000)
            BreathingPhase.EXHALE -> tween(durationMillis = 8000)
            else -> tween(durationMillis = 1000)
        }
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(animatedSize)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(81, 129, 238),
                            Color(81, 129, 238),
                            Color.Transparent
                        ),
                    ),
                    shape = CircleShape
                )
        )

        Box(
            modifier = Modifier
                .size(animatedSize * 1.5f)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(166, 123, 244),
                            Color(166, 123, 244),
                            Color(166, 123, 244),
                            Color.Transparent,
                        ),
                    ),
                    alpha = 0.5f,
                    shape = CircleShape
                )
        )
    }
}
private fun getPhaseText(phase: BreathingPhase): String {
    return when (phase) {
        BreathingPhase.INHALE -> "Вдох"
        BreathingPhase.HOLD -> "Задержка"
        BreathingPhase.EXHALE -> "Выдох"
        BreathingPhase.HOLD_AFTER_EXHALE -> "Задержка"
        BreathingPhase.IDLE -> "Готовность"
    }
}

private fun getPhaseColor(phase: BreathingPhase, baseColor: Color): Color {
    return when (phase) {
        BreathingPhase.INHALE -> Color.Green
        BreathingPhase.EXHALE -> Color(0xFFFF6B6B)
        else -> baseColor
    }
}