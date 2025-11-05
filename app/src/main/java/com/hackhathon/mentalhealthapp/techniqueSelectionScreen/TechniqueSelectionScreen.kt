package com.hackhathon.mentalhealthapp.techniqueSelectionScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hackhathon.features.R
import com.hackhathon.mentalhealthapp.breathingScreen.BreathingTechnique
import com.hackhathon.mentalhealthapp.breathingScreen.BreathingViewModel
import com.hackhathon.ui_kit.theme.MainPurpleColor
import com.hackhathon.ui_kit.theme.White

@Composable
fun TechniqueSelectionScreen(
    onNavigateToBreathing: () -> Unit,
    viewModel : BreathingViewModel,
    techniqueSelectionScreenViewModel : TechniqueSelectionScreenViewModel
) {

    val selectedTechnique by techniqueSelectionScreenViewModel.filteredItems.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        HeaderSection(techniqueSelectionScreenViewModel)

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(selectedTechnique) { technique ->
                TechniqueCard(
                    technique = technique,
                    onClick = {
                        viewModel.selectTechnique(technique)
                        onNavigateToBreathing()
                    },
                    techniqueSelectionScreenViewModel
                )
            }
        }
    }
}

@Composable
private fun HeaderSection(techniqueSelectionScreenViewModel : TechniqueSelectionScreenViewModel) {
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
                .padding(bottom = 24.dp, top = 8.dp, start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_meditation),
                contentDescription = "ic_meditation",
                modifier = Modifier
                    .size(32.dp)
                    .padding(end = 10.dp)
            )
            Text(
                text = "Медитации",
                fontFamily = FontFamily(Font(R.font.inter_medium)),
                fontSize = 20.sp
            )
        }

        SegmentedFilter(techniqueSelectionScreenViewModel)
    }
}

@Composable
fun SegmentedFilter(vm: TechniqueSelectionScreenViewModel) {
    val filters = listOf("Все", "Избранное")
    val selectedFilter by vm.selectedFilter.collectAsState()

    Row(
        modifier = Modifier
            .padding(horizontal =  16.dp)
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
                    ) { vm.updateFilter(index) }
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

@Composable
fun TechniqueCard(
    technique: BreathingTechnique,
    onClick: () -> Unit,
    techniqueSelectionScreenViewModel: TechniqueSelectionScreenViewModel
) {
    var expanded by remember { mutableStateOf(false) }

    val favoriteTechniques by techniqueSelectionScreenViewModel.favoriteTechniques.collectAsState()
    val isFavorite = favoriteTechniques.any { it.id == technique.id }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .padding(horizontal = 14.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .background(brush = Brush.linearGradient(
                    technique.colors,
                    start = Offset(0.0f,0.0f),
                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                ))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = technique.name,
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.inter_medium)),
                    color = White,
                    lineHeight = 21.sp
                )
                IconButton(
                    onClick = { expanded = !expanded }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_down_arrow),
                        modifier = Modifier
                            .size(16.dp, 9.dp)
                            .rotate(if (expanded) 180f else 0f),
                        contentDescription = if (expanded) "Свернуть" else "Развернуть",
                        tint = Color.White
                    )
                }
            }

            Text(
                text = technique.description,
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.inter_regular)),
                color = White
            )

            if (expanded) {
                Column(
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text(
                        text = "Техника:",
                        modifier = Modifier.padding(bottom = 4.dp),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.inter_semi_bold)),
                        color = White
                    )

                    Text(
                        text = technique.tuter,
                        modifier = Modifier.padding(bottom = 20.dp),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.inter_regular)),
                        color = White
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            shape = RoundedCornerShape(200.dp),
                            colors = ButtonDefaults.buttonColors(Color.White),
                            onClick = onClick,
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                text = "Начать медитацию",
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                                fontSize = 14.sp,
                                fontFamily = FontFamily(Font(R.font.inter_semi_bold)),
                                color = Color.Black
                            )
                        }

                        IconButton(
                            modifier = Modifier.size(33.dp)
                                .background(Color.White, shape = RoundedCornerShape(1000.dp)),
                            onClick = {
                                if (isFavorite) {
                                    techniqueSelectionScreenViewModel.isUnfavoriteTechnique(technique)
                                } else {
                                    techniqueSelectionScreenViewModel.isFavoriteTechnique(technique)
                                }
                            },
                        ) {
                            Icon(
                                painter = if (isFavorite) painterResource(R.drawable.ic_heart)
                                else painterResource(R.drawable.ic_outline_heart),
                                contentDescription = if (isFavorite) "Убрать из избранного" else "Добавить в избранное",
                                tint = if (isFavorite) MainPurpleColor else Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}