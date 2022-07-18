package com.george.android.tasker.data.database.tasks.task;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.george.android.tasker.data.model.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    void insert(Task task);

    @Update
    void update(Task task);

    @Query("DELETE FROM task_table WHERE id LIKE :taskId")
    void delete(int taskId);

    @Query("SELECT * FROM task_table")
    LiveData<List<Task>> getAllTasks();

    @Query("SELECT * FROM task_table WHERE folderId LIKE :folderId")
    LiveData<List<Task>> getTasksInFolder(int folderId);

    @Query("SELECT * FROM task_table WHERE title LIKE '%' || :search || '%' ")
    LiveData<List<Task>> findTasks(String search);

    @Query("DELETE FROM task_table WHERE folderId LIKE :folderId")
    void deleteTasksFolder(int folderId);

}
