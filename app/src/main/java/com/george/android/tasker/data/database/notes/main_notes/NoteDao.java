package com.george.android.tasker.data.database.notes.main_notes;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.george.android.tasker.data.model.Note;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert
    void insert(Note note);

    @Update()
    void update(Note note);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updatePosition(List<Note> noteList);

    @Query("DELETE FROM note_table WHERE id LIKE :noteId")
    void delete(int noteId);

    @Query("DELETE FROM note_table")
    void deleteAllNotes();

    @Query("SELECT * FROM note_table ORDER BY position")
    LiveData<List<Note>> getAllNotes();

    @Query("SELECT * FROM note_table WHERE title LIKE '%' || :search  || '%' OR description LIKE '%' || :search  || '%'")
    LiveData<List<Note>> findNote(String search);

}
