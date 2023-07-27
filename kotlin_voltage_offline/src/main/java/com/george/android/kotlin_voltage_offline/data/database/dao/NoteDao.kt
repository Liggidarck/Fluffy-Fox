package com.george.android.kotlin_voltage_offline.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.george.android.kotlin_voltage_offline.data.model.Note

@Dao
interface NoteDao {

    @Insert
    fun insert(note: Note)

    @Update
    fun update(note: Note)

    @Query("DELETE FROM note_table WHERE id LIKE :noteId")
    fun delete(noteId: Int)

    @Query("SELECT * FROM note_table")
    fun getAllNotes(): LiveData<List<Note>>

}