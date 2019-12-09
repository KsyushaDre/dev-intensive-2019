package ru.skillbranch.devintensive.extensions

import java.lang.IllegalStateException
import java.text.SimpleDateFormat
import java.util.*

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR

fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.add(value: Int, units: TimeUnits = TimeUnits.SECOND): Date {
    var time = this.time

    time += when (units) {
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
    }
    this.time = time
    return this
}

fun Date.humanizeDiff(date: Date = Date()): String {
    val diff = date.time - this.time

    return when {
        0 <= diff && diff < SECOND -> "только что"
        SECOND <= diff && diff < SECOND * 45 -> "несколько секунд назад"
        SECOND * 45 <= diff && diff < SECOND * 75 -> "минуту назад"
        SECOND * 75 <= diff && diff < SECOND * 300 -> "${(diff / MINUTE)} минуты назад"
        SECOND * 300 <= diff && diff < MINUTE * 45 -> "${(diff / MINUTE)} минут назад"
        MINUTE * 45 <= diff && diff < MINUTE * 75 -> "час назад"
        MINUTE * 75 <= diff && diff < MINUTE * 300 -> "${(diff / HOUR)} часа назад"
        MINUTE * 300 <= diff && diff < HOUR * 22 -> "${(diff / HOUR)} часов назад"
        HOUR * 22 <= diff && diff < HOUR * 26 -> "день назад"
        HOUR * 26 <= diff && diff < HOUR * 100 -> "${(diff / DAY)} дня назад"
        HOUR * 100 <= diff && diff < DAY * 360 -> "${(diff / DAY)} дней назад"
        DAY * 360 <= diff -> "более года назад"
        -(SECOND) <= diff && diff < 0 -> "сейчас"
        -(SECOND * 45) <= diff && diff < -(SECOND) -> "через несколько секунд"
        -(SECOND * 75) <= diff && diff < -(SECOND * 45) -> "через минуту"
        -(SECOND * 300) <= diff && diff < -(SECOND * 75) -> "через ${(diff / MINUTE) * (-1)} минуты"
        -(MINUTE * 45) <= diff && diff < -(SECOND * 300) -> "через ${(diff / MINUTE) * (-1)} минут"
        -(MINUTE * 75) <= diff && diff < -(MINUTE * 45) -> "через час"
        -(MINUTE * 300) <= diff && diff < -(MINUTE * 75) -> "через ${(diff / HOUR) * (-1)} часа"
        -(HOUR * 22) <= diff && diff < -(MINUTE * 300) -> "через ${(diff / HOUR) * (-1)} часов"
        -(HOUR * 26) <= diff && diff < -(HOUR * 22) -> "через день"
        -(HOUR * 100) <= diff && diff < -(HOUR * 26) -> "через ${(diff / DAY) * (-1)} дня"
        -(DAY * 360) <= diff && diff < -(HOUR * 100) -> "через ${(diff / DAY) * (-1)} дней"
        -(DAY * 360) > diff -> "более чем через год"
        else -> throw IllegalStateException("wrong data")
    }
}

enum class TimeUnits {
    SECOND,
    MINUTE,
    HOUR,
    DAY
}