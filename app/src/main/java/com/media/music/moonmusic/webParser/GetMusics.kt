package com.media.music.moonmusic.webParser


import com.media.music.moonmusic.data.Music
import org.jsoup.Jsoup


suspend fun getMusicsFromWebsite(url: String, website: Website): ArrayList<Music> {
    val musics = arrayListOf<Music>()

    try {
        when (website) {
            Website.Download1music -> {
                val doc = Jsoup.connect(url).get()
                val articles = doc.selectFirst("div[class=d1ctr]")?.children()
                articles?.forEach {
                    val name = it.select("header").select("h2").text()
                    val detailLink = it.select("header").select("a").attr("href")
                    val image = it.selectFirst("img")?.attr("src")
                    val musicFile = it.selectFirst("audio")?.attr("src")



                    if (!name.isNullOrEmpty() && !image.isNullOrEmpty() && !detailLink.isNullOrEmpty()) {
                        val music = Music(name = name,
                            image = image,
                            musicUrl = musicFile,
                            detailUrl = detailLink,
                            website)
                        musics.add(music)
                    }
                }


            }
            Website.MusicFa -> {

                val doc = Jsoup.connect(url).get()
                val articles = doc.selectFirst("div[class=crw]")?.children()
                articles?.forEach {
                    val name = it.select("header").select("a").text().trimMusicName()
                    val detailLink = it.select("header").select("a").attr("href")
                    val image = it.selectFirst("img")?.attr("src")
                    val musicFile = it.selectFirst("audio")?.attr("src")



                    if (!name.isNullOrEmpty() && !image.isNullOrEmpty() && !detailLink.isNullOrEmpty()) {
                        val music = Music(
                            name = name,
                            image = image,
                            musicUrl = musicFile,
                            detailUrl = detailLink,
                            website
                        )
                        musics.add(music)
                    }
                }


            }
        }
    } catch (ex: Exception) {
        ex.printStackTrace()
    }

    return musics

}

suspend fun getMusics(
    website: Website,
    category: Category,
    page: Int,
): ArrayList<Music> {
    var musics = arrayListOf<Music>()
    if (category in website.categories) {
        try {
            when (website) {
                Website.Download1music -> {
                    val url = when (category) {
                        Category.New -> "https://download1music.ir/page/$page"
                        else -> ""
                    }

                    musics = getMusicsFromWebsite(url , website)


                }
                Website.MusicFa -> {
                    val url = when (category) {
                        Category.New -> "https://music-fa.com/download-songs/page/$page"
                        Category.Remix -> "https://music-fa.com/remix/page/$page"
                        Category.Maddahi -> "https://music-fa.com/madahi/page/$page"
                        Category.VIP -> "https://music-fa.com/vip-music/page/$page"
                        else -> ""
                    }

                    musics = getMusicsFromWebsite(url, website)
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    return musics

}

suspend fun getMusicDetail(detailLink: String, website: Website): Music? {
    var music: Music? = null

    try {
        when (website) {
            Website.Download1music -> {}
            Website.MusicFa -> {

                val doc = Jsoup.connect(detailLink).get()

                val article = doc.selectFirst("article[class=pstfa]")
                val downloadBox = article?.selectFirst("div[class=bdownloadfa]")

                val img = article?.selectFirst("img")
                val audio = downloadBox?.selectFirst("audio")

                val image = img?.attr("src")
                val name = img?.attr("title")?.trimMusicName()
                val musicFile = audio?.attr("src")

                if (!image.isNullOrEmpty() and !name.isNullOrEmpty() and !musicFile.isNullOrEmpty()) {
                    music = Music(
                        name = name!!,
                        image = image!!,
                        musicUrl = musicFile,
                        detailUrl = detailLink,
                        website = website)
                }

            }
            else -> {}
        }

    } catch (e: Exception) {
        e.printStackTrace()
    }

    return music
}

suspend fun searchMusic(musicName: String,page:Int = 1 ,  website: Website): ArrayList<Music> {
    var musics = arrayListOf<Music>()
    try {
        when (website) {
            Website.Download1music -> {
                val url = "https://download1music.ir/search/$musicName/page/$page"
                musics =  getMusicsFromWebsite(url , website)
            }
            Website.MusicFa -> {
                val url = "https://music-fa.com/search/$musicName/page/$page"
                musics =  getMusicsFromWebsite(url , website)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return musics

}
