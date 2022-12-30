package com.media.music.moonmusic.screens

sealed class Screen(val rout:String) {
    object Home:Screen("Home")
    object Search:Screen("Search")
    object PlayMusic:Screen("PlayMusic")
    object MusicList:Screen("MusicList")
    object LastPlay:Screen("LastPlay")
    object LikeMusic:Screen("LikeMusic")

}