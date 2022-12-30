package com.media.music.moonmusic.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.media.music.moonmusic.data.Music
import com.media.music.moonmusic.database.lastPlay.toMusic
import com.media.music.moonmusic.ui.theme.darkGradient
import com.media.music.moonmusic.ui.theme.iranSansFont
import com.media.music.moonmusic.viewModel.MusicControllerViewModel
import com.media.music.moonmusic.viewModel.RoomViewModel

@Composable
fun LastPlayScreen(
    navController: NavController,
    musicControllerViewModel: MusicControllerViewModel,
    roomViewModel: RoomViewModel,
) {
    roomViewModel.onShowLastPlays()

    val mLastPlays by roomViewModel.lastPlays.collectAsState()

    Column(modifier = Modifier
        .background(darkGradient)
        .fillMaxSize()) {
        Spacer(modifier = Modifier.height(35.dp))
        Text(
            text = "پخش های اخیر",
            fontFamily = iranSansFont,
            fontSize = 22.sp,
            color = Color.White,
            modifier = Modifier
                .padding(end = 20.dp)
                .align(Alignment.End)
        )
        Spacer(modifier = Modifier.height(20.dp))
        LazyColumn(
            modifier = Modifier
                .padding(end = 25.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            val musics = arrayListOf<Music>()

            //convert "LastPlay" objects to "Music" objects
            mLastPlays.forEach { lastPlay ->
              val music = lastPlay.toMusic()
                music?.let {
                    musics.add(it)
                }
            }

            itemsIndexed(items = musics) { index, music ->
                LastPlayMusicItem(music = music) {
                    //navigate to PlayMusic screen
                    navController.navigate(Screen.PlayMusic.rout)
                    //play music
                    musicControllerViewModel.playMusic(music = music,
                        roomViewModel = roomViewModel,
                        playList = musics,
                        currentIndexOfPlayList = index)
                }


            }

        }


    }
}

@Composable
fun LastPlayMusicItem(music: Music, onclick: () -> Unit) {
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
                    .size(70.dp)
                    .clip(
                        RoundedCornerShape(5.dp)))
            Spacer(modifier = Modifier.width(15.dp))
            Text(
                text = music.name,
                fontSize = 12.sp,
                fontFamily = iranSansFont,
                color = Color.White,
                modifier = Modifier
                    .padding(top = 5.dp)
                    .width(150.dp),
            )

        }

    }
}