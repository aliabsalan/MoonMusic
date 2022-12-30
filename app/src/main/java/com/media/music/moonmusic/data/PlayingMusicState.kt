package com.media.music.moonmusic.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class PlayingMusicState(
    val musicUrl :MutableState<String> = mutableStateOf(""),
    val imageUrl :MutableState<String> = mutableStateOf(""),
    val detailUrl:MutableState<String> = mutableStateOf(""),
    val name:MutableState<String> = mutableStateOf(""),
    val music :MutableState<Music?> = mutableStateOf(null),
    var isPlaying:MutableState<Boolean> = mutableStateOf(false),
    var duration: MutableState<Int> = mutableStateOf(0),
    var currentPosition: MutableState<Int> = mutableStateOf(0)
)
