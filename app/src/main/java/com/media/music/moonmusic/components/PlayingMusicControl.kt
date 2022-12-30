package com.media.music.moonmusic.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.media.music.moonmusic.R
import com.media.music.moonmusic.screens.Screen
import com.media.music.moonmusic.ui.theme.backgroundLightColor
import com.media.music.moonmusic.ui.theme.backgroundLighterColor
import com.media.music.moonmusic.ui.theme.iranSansFont
import com.media.music.moonmusic.ui.theme.primaryColor
import com.media.music.moonmusic.viewModel.MusicControllerViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlayingMusicControl(
    musicControllerViewModel: MusicControllerViewModel,
    navController: NavController,
    modifier: Modifier,
) {

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Card(shape = RoundedCornerShape(6.dp),
            modifier = modifier.height(60.dp),
            backgroundColor = backgroundLightColor,
            onClick = {
                navController.navigate(Screen.PlayMusic.rout)
            }) {
            Box(modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxWidth()) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                    verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = rememberAsyncImagePainter(model = musicControllerViewModel.state.value.imageUrl.value
                            ?: ""),
                        contentDescription = "music image",
                        modifier = Modifier
                            .size(45.dp)
                            .clip(RoundedCornerShape(6.dp))
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = musicControllerViewModel.state.value.name.value ?: "",
                        color = Color.White,
                        fontSize = 10.sp, fontFamily = iranSansFont)

                   /* Spacer(modifier = Modifier.width(60.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_like),
                        contentDescription = "null",
                        modifier = Modifier.size(25.dp),
                        tint = Color.White
                    )*/

                }

                if (musicControllerViewModel.state.value.isPlaying.value ?: false) {
                    val duration = musicControllerViewModel.player.duration
                    val currentPosition = musicControllerViewModel.state.value.currentPosition.value?:0
                    val progress = (currentPosition.toFloat() / duration.toFloat())
                    LinearProgressIndicator(modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .align(Alignment.BottomCenter),
                        progress = progress,
                        backgroundColor = backgroundLighterColor.copy(0.5f),
                        color = Color.White.copy(0.6f))
                }
            }
        }
    }
}

@Preview
@Composable
fun PlayingMusicControlPreview() {
    PlayingMusicControl(viewModel(), rememberNavController(), Modifier)
}

