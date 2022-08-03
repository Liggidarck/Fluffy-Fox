package com.george.android.tasker.data.database.notes.recycle_bin;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.george.android.tasker.data.model.BinNote;

import java.util.List;

@Dao
public interface BinNoteDao {

    @Insert
    void insert(BinNote binNote);

    @Query("DELETE FROM note_bin WHERE id LIKE :id")
    void delete(int id);

    @Query("SELECT * FROM note_bin")
    LiveData<List<BinNote>> getAllBinNote();

    @Query("DELETE FROM note_bin")
    void deleteBin();

}
