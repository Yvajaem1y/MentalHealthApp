package com.hackhathon.mentalhealthapp.profileScreen

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.hackhathon.features.R
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.runtime.collectAsState

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfileScreen(viewModel: ProfileViewModel) {
    val context = LocalContext.current
    var showImageSourceDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val savedImageUri = ImageUtils.loadImageFromInternalStorage(context, "profile.jpg")
        if (savedImageUri != null) {
            viewModel.setUserImage(savedImageUri)
        }
    }

    // Лаунчер для выбора изображения из галереи
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val savedUri = ImageUtils.saveImageToInternalStorage(context, it, "profile.jpg")
            viewModel.setUserImage(savedUri ?: it)
        }
        showImageSourceDialog = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = 32.dp, top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(56.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                // Отображаем изображение пользователя или дефолтное
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
                    DefaultAvatarImage(modifier = Modifier.size(56.dp))
                }

                IconButton(
                    onClick = { showImageSourceDialog = true },
                    modifier = Modifier.size(24.dp),
                    colors = IconButtonDefaults.iconButtonColors(Color.White),
                    shape = RoundedCornerShape(200.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_fotik),
                        contentDescription = "Change photo",
                    )
                }
            }

            Text(
                text = "Профиль",
                fontFamily = FontFamily(Font(resId = R.font.inter_medium, weight = FontWeight.Medium)),
                fontSize = 18.sp,
                lineHeight = 20.sp,
                modifier = Modifier.padding(start = 12.dp)
            )
        }

        Text(
            text = viewModel.userDataState.value.toRequestResult().data!!.userName,
            fontFamily = FontFamily(Font(resId = R.font.inter_semi_bold, weight = FontWeight.Medium)),
            fontSize = 14.sp,
            lineHeight = 20.sp,
        )
        Text(
            text = "Имя пользователя",
            color = Color.Gray,
            fontFamily = FontFamily(Font(resId = R.font.inter_regular)),
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "${viewModel.userDataState.value.toRequestResult().data?.userAge}",
            fontFamily = FontFamily(Font(resId = R.font.inter_semi_bold, weight = FontWeight.Medium)),
            fontSize = 14.sp,
            lineHeight = 20.sp,
        )
        Text(
            text = "Возраст",
            color = Color.Gray,
            fontFamily = FontFamily(Font(resId = R.font.inter_regular)),
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = viewModel.userDataState.value.toRequestResult().data!!.userGender,
            fontFamily = FontFamily(Font(resId = R.font.inter_semi_bold, weight = FontWeight.Medium)),
            fontSize = 14.sp,
            lineHeight = 20.sp,
        )
        Text(
            text = "Пол",
            color = Color.Gray,
            fontFamily = FontFamily(Font(resId = R.font.inter_regular)),
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 18.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Изменить данные",
                color = Color.Gray,
                fontFamily = FontFamily(Font(resId = R.font.inter_regular)),
                fontSize = 14.sp,
            )

            IconButton(
                onClick = {}
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_down_arrow),
                    contentDescription = "arrow",
                    tint = Color.Black,
                    modifier = Modifier.rotate(270f)
                )
            }
        }
    }

    if (showImageSourceDialog) {
        AlertDialog(
            onDismissRequest = { showImageSourceDialog = false },
            title = { Text("Выберите источник") },
            text = { Text("Откуда вы хотите выбрать фотографию?") },
            confirmButton = {
                Button(
                    onClick = {
                        galleryLauncher.launch("image/*")
                    }
                ) {
                    Text("Галерея")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showImageSourceDialog = false }
                ) {
                    Text("Отмена")
                }
            }
        )
    }
}

@Composable
fun DefaultAvatarImage(modifier: Modifier) {
    Box(
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_user),
            contentDescription = "Default avatar",
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    }
}
