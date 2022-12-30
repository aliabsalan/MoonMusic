package com.media.music.moonmusic.utils


data class TimeUnites(val minutes:Int , val seconds:Int)

fun convertMillisToTimeUnites(timeMillis:Int):TimeUnites{
    var seconds:Int = timeMillis / 1000

    val minutes:Int = seconds / 60
    seconds -= (minutes * 60)
    return TimeUnites(minutes = minutes , seconds = seconds)
}

fun TimeUnites.toTime():String{
    var result = ""
    when{
        this.seconds.toString().count() == 1 ->{
            result = "$minutes:0$seconds"
        }
        this.seconds.toString().count() > 1 ->{
            result = "$minutes:$seconds"
        }
    }
    return result
}
