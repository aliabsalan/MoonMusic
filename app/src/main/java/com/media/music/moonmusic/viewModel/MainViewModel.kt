package com.media.music.moonmusic.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.media.music.moonmusic.webParser.Category
import com.media.music.moonmusic.data.Music
import com.media.music.moonmusic.webParser.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel() : ViewModel() {

    val _newMusics = MutableStateFlow(emptyList<Music>())
    val newMusics: StateFlow<List<Music>> = _newMusics

    val _remixMusics = MutableStateFlow(emptyList<Music>())
    val remixMusics: StateFlow<List<Music>> = _remixMusics


    val _maddahiMusics = MutableStateFlow(emptyList<Music>())
    val maddahiMusics: StateFlow<List<Music>> = _maddahiMusics


    val _vipMusics = MutableStateFlow(emptyList<Music>())
    val vipMusics: StateFlow<List<Music>> = _vipMusics

    val _searchMusics = MutableStateFlow(emptyList<Music>())
    val searchMusics:StateFlow<List<Music>> = _searchMusics

    val _randomMusic = MutableStateFlow<Music?>(null)
    val randomMusic:StateFlow<Music?> = _randomMusic

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _newMusics.value = getMusics(Website.MusicFa, Category.New, 1)
            _remixMusics.value = getMusics(Website.MusicFa, Category.Remix, 1)
            _maddahiMusics.value = getMusics(Website.MusicFa, Category.Maddahi, 1)
            _vipMusics.value = getMusics(Website.MusicFa, Category.VIP, 1)
        }
    }


    //events

    fun onSearch(musicName:String){
        viewModelScope.launch(Dispatchers.IO) {
            _searchMusics.value = searchMusic(musicName =  musicName , website = Website.MusicFa)
        }
    }

    fun onRandomMusic() {
        viewModelScope.launch(Dispatchers.IO) {
            val page = (0..10).random()
            val category = arrayOf(Category.VIP , Category.New , Category.Remix).random()
            val randomMusics = getMusics(Website.MusicFa ,category , page)
           _randomMusic.value =  randomMusics.random()
        }
    }



}
