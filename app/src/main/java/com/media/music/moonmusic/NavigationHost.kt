package com.media.music.moonmusic

import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.media.music.moonmusic.screens.*
import com.media.music.moonmusic.viewModel.MainViewModel
import com.media.music.moonmusic.viewModel.MusicControllerViewModel
import com.media.music.moonmusic.viewModel.RoomViewModel
import com.media.music.moonmusic.webParser.Category
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun NavigationHost(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    musicControllerViewModel: MusicControllerViewModel,
    roomViewModel: RoomViewModel,
) {

    NavHost(navController = navController, startDestination = Screen.Home.rout) {
        composable(Screen.Home.rout) {
            HomeScreen(navController = navController,
                mainViewModel = mainViewModel,
                musicControllerViewModel = musicControllerViewModel,
                roomViewModel = roomViewModel)
        }
        composable(Screen.Search.rout) {
            SearchScreen(navController = navController,
                mainViewModel = mainViewModel,
                musicControllerViewModel = musicControllerViewModel,
                roomViewModel = roomViewModel)
        }
        composable("${Screen.MusicList.rout}/{categoryRout}",
            arguments = listOf(navArgument("categoryRout") { NavType.StringType })) { backStackEntry ->
            MusicListScreen(categoryRout = backStackEntry.arguments?.getString("categoryRout")
                ?: Category.New.rout,
                navController = navController,
                musicControllerViewModel = musicControllerViewModel,
                roomViewModel = roomViewModel)
        }
        composable(Screen.PlayMusic.rout) { backStackEntry ->

            PlayMusicScreen(musicControllerViewModel = musicControllerViewModel,
                navController = navController, roomViewModel = roomViewModel)
        }

        composable(Screen.LastPlay.rout) {
            LastPlayScreen(navController = navController,
                musicControllerViewModel = musicControllerViewModel,
                roomViewModel = roomViewModel)
        }

        composable(Screen.LikeMusic.rout) {
            LikeMusicScreen(roomViewModel = roomViewModel,
                musicControllerViewModel = musicControllerViewModel,
                navController = navController)
        }
    }
}