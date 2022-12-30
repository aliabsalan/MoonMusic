package com.media.music.moonmusic.webParser

sealed class Category(val rout:String){

    object New : Category("new")
    object Remix : Category("Remix")
    object Maddahi : Category("Maddahi")
    object VIP : Category("VIP")
}
