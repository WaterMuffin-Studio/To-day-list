package com.watermuffin.todaylist.data.database

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime? {
        return value?.let {
            Instant.fromEpochMilliseconds(it).toLocalDateTime(TimeZone.currentSystemDefault())
        }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): Long? {
        return date?.toInstant(TimeZone.currentSystemDefault())?.toEpochMilliseconds()
    }
}