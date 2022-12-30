package com.media.music.moonmusic.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.media.music.moonmusic.data.Music
import com.media.music.moonmusic.database.lastPlay.LastPlay
import com.media.music.moonmusic.database.RoomDb
import com.media.music.moonmusic.database.likeMusic.LikeMusic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RoomViewModel(application: Application) : AndroidViewModel(application) {

    val roomDb = RoomDb.getInstance(application)
    val lastPlayDao = roomDb.lastPlayDao()
    val likeMusicDao = roomDb.likeMusicDao()


    val _lastPlays = MutableStateFlow(emptyList<LastPlay>())
    val lastPlays: StateFlow<List<LastPlay>> = _lastPlays

    val _likes = MutableStateFlow(emptyList<LikeMusic>())
    val likes: StateFlow<List<LikeMusic>> = _likes

    val _isMusicLike = MutableStateFlow(false)
    val isMusicLike: StateFlow<Boolean> = _isMusicLike


    //events
    fun onShowLastPlays() {
        viewModelScope.launch(Dispatchers.IO) {
            _lastPlays.value = lastPlayDao.selectAll()
        }
    }

    fun onAddLastPlay(music: Music) {
        viewModelScope.launch(Dispatchers.IO) {
            val lastPlay = LastPlay(musicUrl = music.musicUrl,
                name = music.name,
                imageUrl = music.image,
                detailUrl = music.detailUrl,
                websiteRout = music.website.rout)
            lastPlayDao.insert(lastPlay)
        }
    }

    fun onDeleteAllLastPlay() {
        viewModelScope.launch(Dispatchers.IO) {
            lastPlayDao.deleteAll()
        }
    }

    fun onShowLikes() {
        viewModelScope.launch(Dispatchers.IO) {
            _likes.value = likeMusicDao.selectAll()
        }
    }

    fun onLikeMusic(music: Music) {
        viewModelScope.launch(Dispatchers.IO) {
            val likeMusic = LikeMusic(musicUrl = music.musicUrl,
                name = music.name,
                imageUrl = music.image,
                detailUrl = music.detailUrl,
                websiteRout = music.website.rout)
            likeMusicDao.insert(likeMusic)
        }
    }

    fun onDisLikeMusic(detailUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            likeMusicDao.delete(detailUrl)
        }
    }

    fun onCheckLike(detailUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val likeMusic = likeMusicDao.select(detailUrl)
            _isMusicLike.value = likeMusic.isNotEmpty()
        }
    }


}