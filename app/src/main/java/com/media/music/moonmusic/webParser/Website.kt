package com.media.music.moonmusic.webParser

sealed class Website(val rout:String ,val categories : List<Category>) {
    object Download1music:Website("Download1music",listOf(Category.New))
    object MusicFa:Website("MusicFa",listOf(Category.New , Category.Remix , Category.Maddahi , Category.VIP))

    companion object{
        fun convertRoutToWebsite(rout:String):Website?{
            return  when (rout) {
                Website.MusicFa.rout -> Website.MusicFa
                Website.Download1music.rout -> Website.Download1music
                else -> null
            }
        }
    }
}
