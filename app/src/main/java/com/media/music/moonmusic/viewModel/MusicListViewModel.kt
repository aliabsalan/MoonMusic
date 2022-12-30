package com.media.music.moonmusic.viewModel


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.media.music.moonmusic.webParser.Category
import com.media.music.moonmusic.data.Music
import com.media.music.moonmusic.webParser.Website
import com.media.music.moonmusic.webParser.getMusics
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MusicListViewModel(private val categoryRout: String) : ViewModel() {

    val _musics = MutableStateFlow(emptyList<Music>())
    val musics: StateFlow<List<Music>> = _musics
    val _page by mutableStateOf(1)
    var page =  1

    var isInProcess = false

    init {
        viewModelScope.launch(Dispatchers.IO) {

            when (categoryRout) {
                Category.New.rout -> {
                    _musics.value = getMusics(Website.MusicFa , Category.New , page)
                }
                Category.Remix.rout -> {
                    _musics.value = getMusics(Website.MusicFa , Category.Remix , page)
                }
                Category.Maddahi.rout -> {
                    _musics.value = getMusics(Website.MusicFa , Category.Maddahi , page)
                }
                Category.VIP.rout -> {
                    _musics.value = getMusics(Website.MusicFa , Category.VIP , page)
                }
            }

        }
    }

    fun nextPage(){
        if (!isInProcess) {
            isInProcess = true
            page++
            viewModelScope.launch(Dispatchers.IO) {

                when (categoryRout) {
                    Category.New.rout -> {
                        _musics.value += getMusics(Website.MusicFa , Category.New , page)
                    }
                    Category.Remix.rout -> {
                        _musics.value += getMusics(Website.MusicFa , Category.Remix , page)
                    }
                    Category.Maddahi.rout -> {
                        _musics.value += getMusics(Website.MusicFa , Category.Maddahi , page)
                    }
                    Category.VIP.rout -> {
                        _musics.value += getMusics(Website.MusicFa , Category.VIP , page)
                    }
                }
                isInProcess = false
            }
        }
    }
}

class MusicListViewModelFactory(private val categoryRout: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MusicListViewModel(categoryRout) as T
    }
}