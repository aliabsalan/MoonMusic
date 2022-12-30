package com.media.music.moonmusic.viewModel

import android.media.MediaPlayer
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.media.music.moonmusic.data.Music
import com.media.music.moonmusic.data.PlayingMusicState
import com.media.music.moonmusic.webParser.getMusicDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MusicControllerViewModel : ViewModel() {

    val state = MutableStateFlow(PlayingMusicState())

    var _playList = MutableStateFlow(emptyList<Music>())
    var playList: StateFlow<List<Music>> = _playList

    var _currentIndexOfPlayList = MutableStateFlow(0)
    var currentIndexOfPlayList: StateFlow<Int> = _currentIndexOfPlayList

    var roomViewModel: RoomViewModel? = null

    var player = MediaPlayer().apply {
        setOnPreparedListener {
            it.start()
            state.value.duration.value = this.duration
        }
        setOnCompletionListener {
            state.value.isPlaying.value = false
        }
    }


    init {
        viewModelScope.launch {
            while (true) {
                delay(1000)
                state.value.currentPosition.value = player.currentPosition
            }
        }
    }

    fun playMusic(
        music: Music,
        roomViewModel: RoomViewModel,
        playList: List<Music>? = null,
        currentIndexOfPlayList: Int? = null,
    ) {
        if (state.value.musicUrl.value != music.musicUrl) {
            this.roomViewModel = roomViewModel
            viewModelScope.launch(Dispatchers.IO) {
                changeState(music)
            }

            roomViewModel.onAddLastPlay(music)
            if (playList != null) {
                this._playList.value = playList
            } else {
                this._playList.value = emptyList<Music>()
            }

            currentIndexOfPlayList?.let {
                this._currentIndexOfPlayList.value = it
            }

            //check like of music and setValue of "isMusicLiked"
            roomViewModel.onCheckLike(music.detailUrl)

        }
    }

    fun changeState(state: PlayingMusicState) {
        this.state.value = state
        changePlayerData(state.musicUrl.value)
    }

    suspend fun changeState(music: Music) {
        var musicUrl: String? = null
        if (music.musicUrl.isNullOrEmpty()) {
            musicUrl = getMusicFileUrl(music)
        } else {
            musicUrl = music.musicUrl
        }

        withContext(Dispatchers.Main) {
            //create state object with music object
            val state = PlayingMusicState(
                musicUrl = mutableStateOf(musicUrl ?: ""),
                imageUrl = mutableStateOf(music.image),
                detailUrl = mutableStateOf(music.detailUrl),
                music = mutableStateOf(music),
                name = mutableStateOf(music.name),
                isPlaying = mutableStateOf(true)
            )
            //change state
            this@MusicControllerViewModel.state.value = state

            changePlayerData(state.musicUrl.value)
        }
    }

    private suspend fun getMusicFileUrl(music: Music): String? {
        return getMusicDetail(music.detailUrl, music.website)?.musicUrl
    }

    private fun changePlayerData(audioUrl: String) {
        if (player.isPlaying) {
            player.stop()
            player.release()
        }

        //set new instance of player
        //because new setDataSource will get Exception
        player = MediaPlayer().apply {
            setOnPreparedListener {
                it.start()
                state.value.duration.value = this.duration
            }
            setOnCompletionListener {
                state.value.isPlaying.value = false
            }
        }
        player.setDataSource(audioUrl)
        player.prepare()
    }


    // Events
    fun onPositionChanged(newPosition: Float) {
        player.seekTo(newPosition.toInt())
    }

    fun onResume() {
        if (!player.isPlaying) {
            player.start()
            state.value.isPlaying.value = true
        } else {
            state.value.isPlaying.value = false
        }
    }

    fun onPause() {
        player.pause()
        state.value.isPlaying.value = false
    }

    fun onNext() {
        _currentIndexOfPlayList.value += 1
        val music = playList.value[currentIndexOfPlayList.value]
        playMusic(music, roomViewModel!!, playList.value)
        roomViewModel?.onAddLastPlay(music)
    }

    fun onPrevious() {
        _currentIndexOfPlayList.value -= 1
        val music = playList.value[currentIndexOfPlayList.value]
        playMusic(music, roomViewModel!!, playList.value)
        roomViewModel?.onAddLastPlay(music)
    }


}