package com.george.android.tasker.data.tasks.task;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskRepository {

    private final TaskDao taskDao;
    private final LiveData<List<Task>> allTasks;
    final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public TaskRepository(Application app) {
        TaskDatabase database = TaskDatabase.getInstance(app);
        taskDao = database.taskDao();
        allTasks = taskDao.getAllTasks();
    }

    public void insert(Task task) {
        executorService.execute(() -> taskDao.insert(task));
    }

    public void update(Task task) {
        executorService.execute(() -> taskDao.update(task));
    }

    public void delete(Task task) {
        executorService.execute(() -> taskDao.delete(task));
    }

    public void deleteTasksFolder(int folderId) {
        executorService.execute(() -> taskDao.deleteTasksFolder(folderId));
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public LiveData<List<Task>> getFoldersTasks(int folderId) {
        return taskDao.getTasksInFolder(folderId);
    }

    public LiveData<List<Task>> findTask(String search) {
        return taskDao.findTasks(search);
    }

}
