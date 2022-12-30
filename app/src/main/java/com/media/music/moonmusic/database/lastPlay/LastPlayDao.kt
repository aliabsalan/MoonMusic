package com.media.music.moonmusic.database.lastPlay

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.media.music.moonmusic.database.lastPlay.LastPlay

@Dao
interface LastPlayDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(lastPlay: LastPlay):Long


    @Query("DELETE FROM LastPlay WHERE 1")
    suspend fun deleteAll():Int


    @Query("SELECT * FROM  LastPlay ORDER BY id DESC LiMIT 30")
    suspend fun selectAll():List<LastPlay>


}