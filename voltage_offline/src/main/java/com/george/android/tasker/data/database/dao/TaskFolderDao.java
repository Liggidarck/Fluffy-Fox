package com.george.android.tasker.data.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.george.android.tasker.data.model.TaskFolder;

import java.util.List;

@Dao
public interface TaskFolderDao {

    @Insert
    void insert(TaskFolder taskFolder);

    @Update
    void update(TaskFolder taskFolder);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updatePosition(List<TaskFolder> folders);

    @Query("DELETE FROM folder_task_table WHERE folderId LIKE :folderId")
    void delete(int folderId);

    @Query("SELECT * FROM folder_task_table")
    LiveData<List<TaskFolder>> getAllTaskFolders();

}