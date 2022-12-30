package com.media.music.moonmusic.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.media.music.moonmusic.database.lastPlay.LastPlay
import com.media.music.moonmusic.database.lastPlay.LastPlayDao
import com.media.music.moonmusic.database.likeMusic.LikeMusic
import com.media.music.moonmusic.database.likeMusic.LikeMusicDao

@Database(entities = [LastPlay::class , LikeMusic::class] , version = 1)
abstract class RoomDb: RoomDatabase() {

    abstract fun lastPlayDao(): LastPlayDao
    abstract fun likeMusicDao(): LikeMusicDao

    companion object{

        private val databaseName = "moonMusicDatabase"
        fun getInstance(context: Context): RoomDb {
           return Room.databaseBuilder(
                context ,
                RoomDb::class.java,
                databaseName
            ).build()
        }
    }

}