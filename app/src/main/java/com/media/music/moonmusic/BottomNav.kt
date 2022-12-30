package com.media.music.moonmusic

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.media.music.moonmusic.screens.Screen
import com.media.music.moonmusic.ui.theme.backgroundColor
import com.media.music.moonmusic.ui.theme.primaryColor

val selectedBottomNavItem: MutableState<Screen> = mutableStateOf(Screen.Home)
val isBottomNavShowing = mutableStateOf(true)

@Composable
fun BottomNav(navController: NavHostController) {


    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        BottomNavigation(backgroundColor = Color.Black.copy(alpha = 0.9f)) {

            val iconSize = 24.dp
            val selectedColor = Color.White


            BottomNavigationItem(selected = selectedBottomNavItem.value == Screen.Home,
                onClick = {
                    selectedBottomNavItem.value = Screen.Home
                    navController.navigate(Screen.Home.rout){
                        navController.graph.startDestinationRoute?.let {rout->
                            popUpTo(rout){
                                saveState = true
                            }
                        }


                        launchSingleTop = true
                        restoreState = true
                    }
                }, icon = {
                    Icon(painter = painterResource(id = R.drawable.ic_home),
                        contentDescription = null,
                        modifier = Modifier.size(iconSize))
                }, selectedContentColor = selectedColor)



            BottomNavigationItem(selected = selectedBottomNavItem.value == Screen.Search,
                onClick = {
                    selectedBottomNavItem.value = Screen.Search
                    navController.navigate(Screen.Search.rout){
                        navController.graph.startDestinationRoute?.let {rout->
                            popUpTo(rout){
                                saveState = true
                            }
                        }


                        launchSingleTop = true
                        restoreState = true
                    }
                }, icon = {
                    Icon(painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = null,
                        modifier = Modifier.size(iconSize))
                }, selectedContentColor = selectedColor)


            BottomNavigationItem(selected = selectedBottomNavItem.value == Screen.LastPlay,
                onClick = {
                    selectedBottomNavItem.value = Screen.LastPlay
                    navController.navigate(Screen.LastPlay.rout){
                        navController.graph.startDestinationRoute?.let {rout->
                            popUpTo(rout){
                                saveState = true
                            }
                        }


                        launchSingleTop = true
                        restoreState = true
                    }
                }, icon = {
                    Icon(painter = painterResource(id = R.drawable.ic_recent),
                        contentDescription = null,
                        modifier = Modifier.size(iconSize))
                }, selectedContentColor = selectedColor
            )

            BottomNavigationItem(selected = selectedBottomNavItem.value == Screen.LikeMusic,
                onClick = {
                    selectedBottomNavItem.value = Screen.LikeMusic
                    navController.navigate(Screen.LikeMusic.rout){
                        navController.graph.startDestinationRoute?.let {rout->
                            popUpTo(rout){
                                saveState = true
                            }
                        }


                        launchSingleTop = true
                        restoreState = true
                    }
                }, icon = {
                    Icon(painter = painterResource(id = R.drawable.ic_like),
                        contentDescription = null,
                        modifier = Modifier.size(iconSize))
                }, selectedContentColor = selectedColor
            )


        }
    }
}

@Preview
@Composable
fun BottomNavPreview() {
    BottomNav(rememberNavController())
}