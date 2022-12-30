package com.media.music.moonmusic.utils

import java.text.SimpleDateFormat
import java.util.*

enum class Day {
    Morning,
    Afternoon,
    Night
}

fun getTimeOfDay(): Day {
    val day: Day
    val timeInMillis = System.currentTimeMillis()
    val date = Date(timeInMillis)
    val hours = SimpleDateFormat("HH", Locale.ENGLISH).format(date).toInt()
    day = when (hours) {
        in 6..11 -> {
            Day.Morning
        }
        in 12..18 -> {
            Day.Afternoon
        }
        else -> {
            Day.Night
        }
    }
    return day
}