package com.george.android.tasker.data.tasks.task;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    void insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);

    @Query("SELECT * FROM task_table")
    LiveData<List<Task>> getAllTasks();

    @Query("SELECT * FROM task_table WHERE folderId LIKE :folderId")
    LiveData<List<Task>> getTasksInFolder(int folderId);

    @Query("SELECT * FROM task_table WHERE title LIKE '%' || :search || '%' ")
    LiveData<List<Task>> findTasks(String search);

}
