package com.media.music.moonmusic.screens

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.media.music.moonmusic.ui.theme.darkGradient
import com.media.music.moonmusic.ui.theme.iranSansFont
import com.media.music.moonmusic.R
import com.media.music.moonmusic.data.Music
import com.media.music.moonmusic.ui.theme.backgroundLightColor
import com.media.music.moonmusic.viewModel.MainViewModel
import com.media.music.moonmusic.viewModel.MusicControllerViewModel
import com.media.music.moonmusic.viewModel.RoomViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchScreen(
    navController: NavController,
    mainViewModel: MainViewModel,
    musicControllerViewModel: MusicControllerViewModel,
    roomViewModel: RoomViewModel,
) {

    val searchMusics by mainViewModel.searchMusics.collectAsState()

    var isOpenedSearchFiled by remember { mutableStateOf(false) }


    var searchText by remember {
        mutableStateOf("")
    }

    Column(modifier = Modifier
        .background(darkGradient)
        .fillMaxSize()) {

        //first schema of search field
        AnimatedVisibility(visible = !isOpenedSearchFiled) {
            Column {
                Spacer(modifier = Modifier.height(45.dp))
                Text(
                    text = "جستجو",
                    fontFamily = iranSansFont,
                    fontSize = 22.sp,
                    color = Color.White,
                    modifier = Modifier
                        .padding(end = 35.dp)
                        .align(Alignment.End)
                )
                Spacer(modifier = Modifier.height(20.dp))
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {

                    Card(modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .height(50.dp),
                        onClick = {
                            isOpenedSearchFiled = true
                        }) {
                        Row(Modifier
                            .padding(start = 10.dp)
                            .fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                            Icon(painter = painterResource(id = R.drawable.ic_search),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp), tint = Color.DarkGray)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = "خواننده ، آهنگ یا قسمتی از متن",
                                fontFamily = iranSansFont,
                                fontSize = 14.sp)
                        }
                    }
                }

            }
        }

        //search field
        AnimatedVisibility(visible = isOpenedSearchFiled) {
            val focusRequester = remember { FocusRequester() }
            val focusManager = LocalFocusManager.current
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                TextField(value = searchText,
                    onValueChange = { searchText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .focusRequester(focusRequester),
                    placeholder = {
                        Text(text = "به دنبال چه آهنگی میگردید",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 13.sp, fontFamily = iranSansFont)
                    },
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = backgroundLightColor,
                        textColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        cursorColor = Color.White),
                    keyboardActions = KeyboardActions(onSearch = {
                        mainViewModel.onSearch(searchText)
                        focusManager.clearFocus()
                    }), keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search))

            }
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
        }

        //show result of search in LazyVerticalGrid
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            AnimatedVisibility(visible = searchMusics.isNotEmpty(),
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut()) {

                LazyVerticalGrid(columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    contentPadding = PaddingValues(top = 30.dp, bottom = 60.dp)) {
                    itemsIndexed(items = searchMusics) { index ,music ->
                        MusicSearchItem(music = music,
                            navController = navController,
                            musicControllerViewModel = musicControllerViewModel,
                            roomViewModel = roomViewModel,
                        playlist = searchMusics ,
                        currentIndex = index)
                    }
                }

            }
        }

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MusicSearchItem(
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
        .padding(30.dp)
        .width(130.dp)) {
        Card(modifier = Modifier.size(imageSize),
            backgroundColor = backgroundLightColor,
            shape = RoundedCornerShape(15.dp),
            onClick = {
                /*     val encodeDetailLink =
                         URLEncoder.encode(music.detailLink, StandardCharsets.UTF_8.name())*/
                navController.navigate(Screen.PlayMusic.rout)

                Log.i("searchMusic", "${music.musicUrl} , ${music}")
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

@Preview
@Composable
fun SearchScreenPreview() {
    SearchScreen(rememberNavController(), viewModel(), viewModel(), viewModel())
}