package com.watermuffin.todaylist.data.repository

import com.watermuffin.todaylist.data.database.dao.TextNoteDao
import com.watermuffin.todaylist.data.database.entities.TextNoteEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

class TextNoteRepository(private val noteDao: TextNoteDao) {
    fun getAllNotes(): Flow<List<TextNoteEntity>> = noteDao.getAllNotes()

    suspend fun addNote(title: String, content: String) {
        val note = TextNoteEntity(
            title = title,
            content = content,
            createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
            updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        )
        noteDao.insertNote(note)
    }

    suspend fun deleteNoteById(id: String) {
        val note = noteDao.getNoteById(id)
        note?.let { noteDao.deleteNote(it) }
    }

    suspend fun archiveNote(id: String) {
        val note = noteDao.getNoteById(id)
        note?.let {
            noteDao.updateNote(it.copy(isArchived = true))
        }
    }

    suspend fun deleteFromArchive(id: String) {
        val note = noteDao.getNoteById(id)
        note?.let {
            noteDao.updateNote(it.copy(isArchived = false))
        }
    }

    suspend fun pinNote(id: String) {
        val note = noteDao.getNoteById(id)
        note?.let {
            noteDao.updateNote(it.copy(isPinned = true))
        }
    }

    suspend fun unpinNote(id: String) {
        val note = noteDao.getNoteById(id)
        note?.let {
            noteDao.updateNote(it.copy(isPinned = false))
        }
    }
}