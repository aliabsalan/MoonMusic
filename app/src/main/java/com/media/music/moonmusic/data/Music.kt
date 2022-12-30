package com.media.music.moonmusic.data

import com.media.music.moonmusic.webParser.Website

data class Music(
    val name: String,
    val image: String,
    val musicUrl: String?,
    val detailUrl: String,
    val website: Website,
)