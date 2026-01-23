package com.watermuffin.todaylist.data.database.entities

import androidx.core.content.ContentProviderCompat.requireContext
import androidx.room.processor.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.UUID
import kotlin.time.Clock

@Entity(tableName = "text_notes")
data class TextNoteEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),

    val title: String = "New note",
    val content: String = "",

    @ColumnInfo(name = "created_at")
    val createdAt: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),

    @ColumnInfo(name = "updated_at")
    val updatedAt: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),

    val isArchived: Boolean = false,
    val isPinned: Boolean = false,
    val tags: String = "",
)

