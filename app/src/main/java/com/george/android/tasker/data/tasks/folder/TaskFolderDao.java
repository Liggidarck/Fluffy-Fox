package com.george.android.tasker.data.tasks.folder;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskFolderDao {

    @Insert
    void insert(TaskFolder taskFolder);

    @Update
    void update(TaskFolder taskFolder);

    @Delete
    void delete(TaskFolder taskFolder);

    @Query("SELECT * FROM folder_task_table")
    LiveData<List<TaskFolder>> getAllTaskFolders();

}