package com.media.music.moonmusic.webParser

fun String.trimMusicName():String{
   return this.replace("دانلود" , "").replace("آهنگ","").trim()
}

