package com.media.music.moonmusic.screens


import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.media.music.moonmusic.components.PlayingMusicControl
import com.media.music.moonmusic.data.Music
import com.media.music.moonmusic.isBottomNavShowing
import com.media.music.moonmusic.selectedBottomNavItem
import com.media.music.moonmusic.ui.theme.backgroundLightColor
import com.media.music.moonmusic.ui.theme.darkGradient
import com.media.music.moonmusic.ui.theme.iranSansFont
import com.media.music.moonmusic.utils.Day
import com.media.music.moonmusic.utils.getTimeOfDay
import com.media.music.moonmusic.viewModel.MainViewModel
import com.media.music.moonmusic.viewModel.MusicControllerViewModel
import com.media.music.moonmusic.viewModel.RoomViewModel
import com.media.music.moonmusic.webParser.Category
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior

@OptIn(ExperimentalSnapperApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    mainViewModel: MainViewModel,
    musicControllerViewModel: MusicControllerViewModel,
    roomViewModel: RoomViewModel,
) {

    isBottomNavShowing.value = true

    val context = LocalContext.current

    val newMusics by mainViewModel.newMusics.collectAsState()
    val remixMusics by mainViewModel.remixMusics.collectAsState()
    val maddahiMusics by mainViewModel.maddahiMusics.collectAsState()
    val vipMusics by mainViewModel.vipMusics.collectAsState()
    val randomMusic by mainViewModel.randomMusic.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier
            .background(darkGradient)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
        ) {

            Spacer(modifier = Modifier.height(40.dp))

            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                    val timeOfDay = when (getTimeOfDay()) {
                        Day.Morning -> "صبح بخیر"
                        Day.Afternoon -> "ظهر بخیر"
                        Day.Night -> "شب بخیر"
                    }
                    Text(text = timeOfDay,
                        fontFamily = iranSansFont,
                        fontSize = 25.sp,
                        color = Color.White)
                    Spacer(modifier = Modifier.width(100.dp))

                    Icon(painter = painterResource(id = R.drawable.ic_settings_outlined),
                        contentDescription = null,
                        modifier = Modifier
                            .size(25.dp)
                            .clickable {
                                Toast
                                    .makeText(context,
                                        "هیچ تنظیماتی در این نسخه درنظر گرفته نشده",
                                        Toast.LENGTH_SHORT)
                                    .show()
                            },
                        tint = Color.White)

                    Spacer(modifier = Modifier.width(20.dp))

                    Icon(painter = painterResource(id = R.drawable.ic_recent),
                        contentDescription = null,
                        modifier = Modifier
                            .size(25.dp)
                            .clickable {
                                navController.navigate(Screen.LastPlay.rout)
                                selectedBottomNavItem.value = Screen.LastPlay
                            },
                        tint = Color.White)

                    Spacer(modifier = Modifier.width(20.dp))

                    Icon(painter = painterResource(id = R.drawable.ic_random_play),
                        contentDescription = null,
                        modifier = Modifier
                            .size(25.dp)
                            .clickable {
                                mainViewModel.onRandomMusic()

                                if (randomMusic != null) {
                                    navController.navigate(Screen.PlayMusic.rout)
                                    musicControllerViewModel.playMusic(music = randomMusic!!,
                                        roomViewModel = roomViewModel)
                                }
                            },
                        tint = Color.White)


                }

            }

            Spacer(modifier = Modifier.height(20.dp))

            if (newMusics.isNotEmpty() and vipMusics.isNotEmpty() and remixMusics.isNotEmpty() and maddahiMusics.isNotEmpty()) {
                Row {
                    CategoryItem(imageUrl = newMusics.first().image, label = "جدیدترین ها") {
                        navController.navigate("${Screen.MusicList.rout}/${Category.New.rout}")
                    }
                    CategoryItem(vipMusics.first().image, label = "آهنگ های ویژه") {
                        navController.navigate("${Screen.MusicList.rout}/${Category.VIP.rout}")
                    }
                }

                Row {
                    CategoryItem(imageUrl = remixMusics.first().image, label = "ریمیکس") {
                        navController.navigate("${Screen.MusicList.rout}/${Category.Remix.rout}")
                    }
                    CategoryItem(maddahiMusics.first().image, label = "مداحی") {
                        navController.navigate("${Screen.MusicList.rout}/${Category.Maddahi.rout}")
                    }
                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            //new musics | horizontal list
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {

                AnimatedVisibility(visible = newMusics.isNotEmpty()) {

                    Column {
                        Box(modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .fillMaxWidth()) {
                            Text(modifier = Modifier.align(Alignment.CenterStart),
                                text = "جدیدترین ها",
                                fontSize = 17.sp,
                                fontFamily = iranSansFont, color = Color.White)
                            TextButton(onClick = {
                                navController.navigate("${Screen.MusicList.rout}/${Category.New.rout}")
                            }, modifier = Modifier.align(Alignment.CenterEnd)) {
                                Text(text = "همه",
                                    fontSize = 17.sp,
                                    fontFamily = iranSansFont,
                                    color = Color.White)
                            }
                        }
                        Spacer(modifier = Modifier.height(5.dp))

                        val lazyListState = rememberLazyListState()
                        LazyRow(Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(13.dp),
                            contentPadding = PaddingValues(horizontal = 15.dp),
                            state = lazyListState,
                            flingBehavior = rememberSnapperFlingBehavior(lazyListState)) {
                            itemsIndexed(items = newMusics) { index, music ->
                                MusicItem(music = music,
                                    navController = navController,
                                    musicControllerViewModel = musicControllerViewModel,
                                    roomViewModel = roomViewModel,
                                    playlist = newMusics,
                                    currentIndex = index)
                            }
                        }

                    }

                }


            }

            Spacer(modifier = Modifier.height(20.dp))

            //vip musics | horizontal list
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                AnimatedVisibility(visible = vipMusics.isNotEmpty()) {
                    Column {
                        Box(modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .fillMaxWidth()) {
                            Text(modifier = Modifier.align(Alignment.CenterStart),
                                text = "آهنگ های ویژه",
                                fontSize = 17.sp,
                                fontFamily = iranSansFont,
                                color = Color.White)
                            TextButton(onClick = {
                                navController.navigate("${Screen.MusicList.rout}/${Category.VIP.rout}")
                            }, modifier = Modifier.align(Alignment.CenterEnd)) {
                                Text(text = "همه",
                                    fontSize = 17.sp,
                                    fontFamily = iranSansFont,
                                    color = Color.White)
                            }
                        }

                        Spacer(modifier = Modifier.height(5.dp))

                        val lazyListState = rememberLazyListState()
                        LazyRow(Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(13.dp),
                            contentPadding = PaddingValues(horizontal = 15.dp),
                            state = lazyListState,
                            flingBehavior = rememberSnapperFlingBehavior(lazyListState)) {
                            itemsIndexed(items = vipMusics) { index, music ->
                                MusicItem(music = music,
                                    navController = navController,
                                    musicControllerViewModel = musicControllerViewModel,
                                    roomViewModel = roomViewModel,
                                    playlist = vipMusics,
                                    currentIndex = index)

                            }
                        }

                    }
                }


            }

            Spacer(modifier = Modifier.height(20.dp))

            //remix musics | horizontal list
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                AnimatedVisibility(visible = vipMusics.isNotEmpty()) {
                    Column {
                        Box(modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .fillMaxWidth()) {
                            Text(modifier = Modifier.align(Alignment.CenterStart),
                                text = "ریمیکس ها",
                                fontSize = 17.sp,
                                fontFamily = iranSansFont,
                                color = Color.White)
                            TextButton(onClick = {
                                navController.navigate("${Screen.MusicList.rout}/${Category.Remix.rout}")
                            }, modifier = Modifier.align(Alignment.CenterEnd)) {
                                Text(text = "همه",
                                    fontSize = 17.sp,
                                    fontFamily = iranSansFont,
                                    color = Color.White)
                            }
                        }

                        Spacer(modifier = Modifier.height(5.dp))

                        val lazyListState = rememberLazyListState()
                        LazyRow(Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(13.dp),
                            contentPadding = PaddingValues(horizontal = 15.dp),
                            state = lazyListState,
                            flingBehavior = rememberSnapperFlingBehavior(lazyListState)) {
                            itemsIndexed(items = remixMusics) { index, music ->
                                MusicItem(music = music,
                                    navController = navController,
                                    musicControllerViewModel = musicControllerViewModel,
                                    roomViewModel = roomViewModel,
                                    playlist = remixMusics,
                                    currentIndex = index)

                            }
                        }

                    }
                }


            }

            Spacer(modifier = Modifier.height(20.dp))

            //maddahi musics | horizontal list
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                AnimatedVisibility(visible = vipMusics.isNotEmpty()) {
                    Column {
                        Box(modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .fillMaxWidth()) {
                            Text(modifier = Modifier.align(Alignment.CenterStart),
                                text = "مداحی ها",
                                fontSize = 17.sp,
                                fontFamily = iranSansFont,
                                color = Color.White)
                            TextButton(onClick = {
                                navController.navigate("${Screen.MusicList.rout}/${Category.Maddahi.rout}")
                            }, modifier = Modifier.align(Alignment.CenterEnd)) {
                                Text(text = "همه",
                                    fontSize = 17.sp,
                                    fontFamily = iranSansFont,
                                    color = Color.White)
                            }
                        }

                        Spacer(modifier = Modifier.height(5.dp))

                        val lazyListState = rememberLazyListState()
                        LazyRow(Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(13.dp),
                            contentPadding = PaddingValues(horizontal = 15.dp),
                            state = lazyListState,
                            flingBehavior = rememberSnapperFlingBehavior(lazyListState)) {
                            itemsIndexed(items = maddahiMusics) { index, music ->
                                MusicItem(music = music,
                                    navController = navController,
                                    musicControllerViewModel = musicControllerViewModel,
                                    roomViewModel = roomViewModel,
                                    playlist = maddahiMusics,
                                    currentIndex = index)

                            }
                        }

                    }
                }


            }



            if (newMusics.isEmpty() || remixMusics.isEmpty() || vipMusics.isEmpty() || maddahiMusics.isEmpty()) {
                CircularProgressIndicator(color = Color.White.copy(0.7f),
                    modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            Spacer(modifier = Modifier.height(135.dp))
        }

        if (musicControllerViewModel.state.value.isPlaying.value ?: false) {
            PlayingMusicControl(
                musicControllerViewModel = musicControllerViewModel,
                navController = navController,
                modifier = Modifier
                    .padding(bottom = 60.dp)
                    .padding(horizontal = 3.dp)
                    .align(Alignment.BottomCenter)
            )
        }
    }


}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CategoryItem(imageUrl: String, label: String, onclick: () -> Unit = {}) {
    Card(modifier = Modifier
        .padding(horizontal = 10.dp, vertical = 6.dp)
        .size(160.dp, 65.dp),
        shape = RoundedCornerShape(5.dp),
        backgroundColor = backgroundLightColor,
        onClick = onclick) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(painter = rememberAsyncImagePainter(model = imageUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .size(65.dp))
                Box(Modifier.fillMaxSize()) {
                    Text(text = label,
                        fontFamily = iranSansFont,
                        fontSize = 11.sp,
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.White)
                }
            }

        }

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MusicItem(
    music: Music,
    navController: NavController,
    musicControllerViewModel: MusicControllerViewModel,
    roomViewModel: RoomViewModel,
    playlist: List<Music>? = null,
    currentIndex: Int? = null,
) {

    val width = 130.dp
    val imageSize = width // for width and height
    Column(modifier = Modifier
        .width(130.dp)) {
        Card(modifier = Modifier.size(imageSize),
            backgroundColor = backgroundLightColor,
            shape = RoundedCornerShape(0.dp),
            onClick = {
                //encode detail page url of music
                /* val encodeDetailLink =
                     URLEncoder.encode(music.detailLink, StandardCharsets.UTF_8.name())*/
                //navigate to playMusic Screen to show detail
                navController.navigate(Screen.PlayMusic.rout)

                //start playing music
                if (!playlist.isNullOrEmpty() && currentIndex != null) {
                    musicControllerViewModel.playMusic(music = music,
                        roomViewModel = roomViewModel,
                        playList = playlist,
                        currentIndexOfPlayList = currentIndex)
                } else {
                    musicControllerViewModel.playMusic(music = music, roomViewModel = roomViewModel)
                }
            }) {
            Image(painter = rememberAsyncImagePainter(model = music.image),
                contentDescription = null,
                Modifier
                    .fillMaxSize(), contentScale = ContentScale.Crop)

        }
        Spacer(modifier = Modifier.height(3.dp))
        Text(text = music.name,
            fontFamily = iranSansFont,
            fontSize = 10.sp,
            modifier = Modifier.width(width), color = Color.White)

    }

}


@Preview(showBackground = true)
@Composable
fun ExploreScreenPreview() {
    HomeScreen(rememberNavController(), viewModel(), viewModel(), viewModel())
}