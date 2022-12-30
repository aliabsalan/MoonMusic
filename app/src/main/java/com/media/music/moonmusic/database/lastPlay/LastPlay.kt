package com.media.music.moonmusic.database.lastPlay

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.media.music.moonmusic.data.Music
import com.media.music.moonmusic.database.likeMusic.LikeMusic
import com.media.music.moonmusic.webParser.Website

@Entity(tableName = "LastPlay" , indices = arrayOf(Index("detailUrl" , unique = true)))
data class LastPlay(

    @ColumnInfo
    var musicUrl:String?,

    @ColumnInfo
    var name:String,

    @ColumnInfo
    var imageUrl :String ,

    @ColumnInfo
    var detailUrl:String,

    @ColumnInfo
    var websiteRout :String ,

    @PrimaryKey(autoGenerate = true)
    var id:Int? = null
)
fun LastPlay.toMusic(): Music? {
    val website = Website.convertRoutToWebsite(websiteRout)
    return if (website != null)
        Music(name = name,
            image = imageUrl,
            musicUrl = musicUrl,
            detailUrl = detailUrl,
            website = website)
    else null
}

