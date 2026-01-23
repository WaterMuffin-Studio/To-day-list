package com.watermuffin.todaylist.data.database.dao

import androidx.room.*
import com.watermuffin.todaylist.data.database.entities.TextNoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TextNoteDao {
    @Query("SELECT * FROM text_notes ORDER BY created_at DESC")
    fun getAllNotes(): Flow<List<TextNoteEntity>>

    @Query("SELECT * FROM text_notes WHERE id = :id")
    suspend fun getNoteById(id: String): TextNoteEntity?

    @Insert
    suspend fun insertNote(note: TextNoteEntity)

    @Update
    suspend fun updateNote(note: TextNoteEntity)

    @Delete
    suspend fun deleteNote(note: TextNoteEntity)

    @Query("SELECT * FROM text_notes WHERE tags LIKE '%' || LOWER(:tag) || '%'")
    fun getNotesByTag(tag: String): Flow<List<TextNoteEntity>>
}