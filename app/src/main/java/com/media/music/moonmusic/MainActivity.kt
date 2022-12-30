package com.media.music.moonmusic

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.media.music.moonmusic.screens.Screen
import com.media.music.moonmusic.ui.theme.MoonMusicTheme
import com.media.music.moonmusic.viewModel.MainViewModel
import com.media.music.moonmusic.viewModel.MusicControllerViewModel
import com.media.music.moonmusic.viewModel.RoomViewModel

class MainActivity : ComponentActivity() {
    var navController : NavHostController? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
             navController = rememberNavController()
            MoonMusicTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background) {
                    MyApp(navController = navController!!)
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val screen = when(navController?.currentDestination?.route){
            Screen.Home.rout -> Screen.Home
            Screen.Search.rout -> Screen.Search
            Screen.LastPlay.rout -> Screen.LastPlay
            else -> {null}
        }
        if (screen != null)
        selectedBottomNavItem.value = screen
    }
}



@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MyApp(navController: NavHostController) {
    Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
        AnimatedVisibility(visible = isBottomNavShowing.value , enter = fadeIn() + slideInVertically() , exit = fadeOut() + slideOutVertically()) {
            BottomNav(navController)
        }

    }) {
        val mainViewModel = viewModel<MainViewModel>()
        val musicControllerViewModel = viewModel<MusicControllerViewModel>()
        val roomViewModel = viewModel<RoomViewModel>()
        NavigationHost(navController = navController,
            mainViewModel = mainViewModel,
            musicControllerViewModel = musicControllerViewModel,
        roomViewModel = roomViewModel)
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MoonMusicTheme {
        MyApp(rememberNavController())
    }
}
