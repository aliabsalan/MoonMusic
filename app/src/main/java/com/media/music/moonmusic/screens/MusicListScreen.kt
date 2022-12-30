package com.media.music.moonmusic.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.media.music.moonmusic.data.Music
import com.media.music.moonmusic.ui.theme.darkGradient
import com.media.music.moonmusic.ui.theme.iranSansFont
import com.media.music.moonmusic.utils.isScrolledToTheEnd
import com.media.music.moonmusic.viewModel.MusicControllerViewModel
import com.media.music.moonmusic.viewModel.MusicListViewModel
import com.media.music.moonmusic.viewModel.MusicListViewModelFactory
import com.media.music.moonmusic.viewModel.RoomViewModel


@Composable
fun MusicListScreen(
    categoryRout: String,
    navController: NavController,
    musicControllerViewModel: MusicControllerViewModel,
    roomViewModel: RoomViewModel,
) {
    val musicListViewModel =
        viewModel(MusicListViewModel::class.java, factory = MusicListViewModelFactory(categoryRout))
    val musics by musicListViewModel.musics.collectAsState()

    val lazyState = rememberLazyListState()
    if (lazyState.isScrolledToTheEnd()) {
        musicListViewModel.nextPage()
    }

    LazyColumn(
        state = lazyState,
        modifier = Modifier
            .background(darkGradient)
            .padding(bottom = 50.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        itemsIndexed(items = musics) {index, music ->
            MusicListItem(music = music) {
                /*  val encodeDetailLink =
                      URLEncoder.encode(music.detailLink, StandardCharsets.UTF_8.name())*/
                //navigate to playMusic Screen to show detail
                navController.navigate(Screen.PlayMusic.rout)

                //start playing music

                musicControllerViewModel.playMusic(music = music, roomViewModel = roomViewModel , playList = musics , currentIndexOfPlayList = index)
            }

        }
    }

}

@Composable
fun MusicListItem(music: Music,onclick: () -> Unit) {
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


@Preview
@Composable
fun MusicListScreenPreview() {
    MusicListScreen("", rememberNavController(), viewModel(), viewModel())
}