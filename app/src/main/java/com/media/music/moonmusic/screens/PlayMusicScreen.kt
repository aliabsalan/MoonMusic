package com.media.music.moonmusic.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.media.music.moonmusic.R
import com.media.music.moonmusic.data.Music
import com.media.music.moonmusic.isBottomNavShowing
import com.media.music.moonmusic.ui.theme.*
import com.media.music.moonmusic.utils.convertMillisToTimeUnites
import com.media.music.moonmusic.utils.toTime
import com.media.music.moonmusic.viewModel.MusicControllerViewModel
import com.media.music.moonmusic.viewModel.RoomViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlayMusicScreen(
    musicControllerViewModel: MusicControllerViewModel,
    navController: NavController,
    roomViewModel: RoomViewModel,
) {

    val scope = rememberCoroutineScope()
    val playingMusicState by musicControllerViewModel.state.collectAsState()
    val playList by musicControllerViewModel.playList.collectAsState()
    val currentIndexOfPlayList by musicControllerViewModel.currentIndexOfPlayList.collectAsState()
    val isMusicLike by roomViewModel.isMusicLike.collectAsState()
    val detailUrl = playingMusicState.detailUrl.value
    val music = playingMusicState.music.value

    //check like of music and setValue of "isMusicLiked"
    roomViewModel.onCheckLike(detailUrl)

    val canPreviousMusic = when {
        playList.isEmpty() -> false
        currentIndexOfPlayList > 0 -> true
        else -> false
    }
    val canNextMusic = when {
        playList.isEmpty() -> false
        currentIndexOfPlayList < playList.lastIndex -> true
        else -> false
    }

    isBottomNavShowing.value = false


    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    ModalBottomSheetLayout(sheetContent = {
        LazyColumn(contentPadding = PaddingValues(top = 20.dp, bottom = 20.dp)) {
            itemsIndexed(items = playList) { index, music ->
                val isPlaying = currentIndexOfPlayList == index
                PlayListItem(music = music, isPlaying = isPlaying) {
                    //play music
                    musicControllerViewModel.playMusic(music = music,
                        roomViewModel = roomViewModel,
                        playList = playList,
                        currentIndexOfPlayList = index)

                    //hide bottom sheet
                    scope.launch {
                        sheetState.hide()
                    }
                }
            }
        }
    },
        sheetState = sheetState,
        sheetBackgroundColor = backgroundColor,
        sheetShape = RoundedCornerShape(6.dp)) {
        Column(Modifier
            .background(darkGradient)
            .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

            Spacer(modifier = Modifier.height(20.dp))
            Box(Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()) {
                Icon(painter = painterResource(id = R.drawable.ic_collapse),
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                        .align(
                            Alignment.CenterStart)
                        .clickable {
                            navController.popBackStack()
                        },
                    tint = Color.White)

                Icon(painter = painterResource(id = R.drawable.ic_options),
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                        .align(
                            Alignment.CenterEnd),
                    tint = Color.White)
            }

            Spacer(modifier = Modifier.height(50.dp))

            Image(
                painter = rememberAsyncImagePainter(model = playingMusicState.imageUrl.value),
                contentDescription = null,
                modifier = Modifier
                    .size(270.dp)
            )


            Spacer(modifier = Modifier.height(5.dp))
            Text(text = playingMusicState.name.value,
                color = Color.White,
                fontFamily = iranSansFont,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 30.dp))

            Spacer(modifier = Modifier.height(30.dp))
            Slider(
                value = playingMusicState.currentPosition.value.toFloat(),
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                onValueChange = {
                    musicControllerViewModel.onPositionChanged(it)
                },
                valueRange = 0f..(playingMusicState.duration.value.toFloat()),
                colors = SliderDefaults.colors(thumbColor = Color.Transparent,
                    activeTickColor = Color.White,
                    activeTrackColor = backgroundLighterColor)
            )

            //time
            Box(Modifier
                .padding(horizontal = 25.dp)
                .fillMaxWidth()) {
                val currentTime =
                    convertMillisToTimeUnites(playingMusicState.currentPosition.value).toTime()
                val durationTime =
                    convertMillisToTimeUnites(playingMusicState.duration.value).toTime()

                Text(text = currentTime,
                    modifier = Modifier.align(Alignment.CenterStart),
                    color = Color.White, fontSize = 10.sp)
                Text(text = durationTime,
                    modifier = Modifier.align(Alignment.CenterEnd),
                    color = Color.White, fontSize = 10.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            //music controller
            Row(
                Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                var like by remember { mutableStateOf(isMusicLike) }

                Icon(
                    painter = if (like) painterResource(id = R.drawable.ic_liked) else painterResource(
                        id = R.drawable.ic_like),
                    contentDescription = null,
                    modifier = Modifier
                        .size(25.dp)
                        .clickable {
                            if (like) {
                                //dislike the music
                                roomViewModel.onDisLikeMusic(detailUrl = detailUrl)
                            } else {
                                //like the music
                                music?.let {
                                    roomViewModel.onLikeMusic(music = it)
                                }
                            }
                            like = if (like) false else true
                        },
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(38.dp))


                Icon(
                    painter = painterResource(id = R.drawable.ic_previous_media),
                    contentDescription = null,
                    modifier = Modifier
                        .size(25.dp)
                        .alpha(if (canPreviousMusic) 1f else 0.5f)
                        .clickable {
                            if (canPreviousMusic) {
                                musicControllerViewModel.onPrevious()
                            }
                        },
                    tint = Color.White
                )


                Spacer(modifier = Modifier.width(33.dp))

                if (musicControllerViewModel.state.value.isPlaying.value ?: false) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_pause),
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.dp)
                            .clickable {
                                musicControllerViewModel.onPause()
                            },
                    )

                } else {
                    Image(
                        painter = painterResource(id = R.drawable.ic_play),
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.dp)
                            .clickable {
                                musicControllerViewModel.onResume()
                            },
                    )
                }

                Spacer(modifier = Modifier.width(33.dp))

                Icon(
                    painter = painterResource(id = R.drawable.ic_next_media),
                    contentDescription = null,
                    modifier = Modifier
                        .size(25.dp)
                        .alpha(if (canNextMusic) 1f else 0.5f)
                        .clickable {
                            if (canNextMusic) {
                                musicControllerViewModel.onNext()
                            }
                        },
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(38.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_playlist),
                    contentDescription = null,
                    modifier = Modifier
                        .size(25.dp)
                        .alpha(if (canPreviousMusic || canNextMusic) 1f else 0.5f)
                        .clickable {
                            if (canPreviousMusic || canNextMusic) {
                                scope.launch {
                                    sheetState.show()
                                }
                            }
                        },
                    tint = Color.White)


            }

        }
    }


}

@Composable
fun PlayListItem(music: Music, isPlaying: Boolean, onclick: () -> Unit) {

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Row(modifier = Modifier
            .clickable {
                onclick()
            }
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 10.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top) {
            Image(painter = rememberAsyncImagePainter(model = music.image),
                contentDescription = null,
                modifier = Modifier
                    .size(55.dp)
                    .clip(RoundedCornerShape(6.dp))
            )
            Spacer(modifier = Modifier.width(15.dp))
            Text(
                text = music.name,
                fontSize = 10.sp,
                fontFamily = iranSansFont,
                color = if (isPlaying) Color.Green else Color.White,
                modifier = Modifier
                    .padding(top = 5.dp)
                    .width(150.dp),
            )

        }

    }

}

@Preview
@Composable
fun PlayMusicScreenPreview() {
    PlayMusicScreen(viewModel(), rememberNavController(), viewModel())
}