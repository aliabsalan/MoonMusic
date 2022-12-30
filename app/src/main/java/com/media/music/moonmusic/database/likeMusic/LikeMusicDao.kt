package com.media.music.moonmusic.database.likeMusic

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LikeMusicDao {

    @Insert
    fun insert(likeMusic: LikeMusic):Long

    @Query("DELETE FROM LikeMusic WHERE detailUrl = :detailUrl")
    fun delete(detailUrl:String):Int


    @Query("SELECT * FROM LikeMusic WHERE detailUrl = :detailUrl")
    fun select(detailUrl:String):List<LikeMusic>

    @Query("SELECT * FROM LikeMusic ORDER BY id DESC")
    fun selectAll():List<LikeMusic>

}