package com.george.android.tasker.data.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.george.android.tasker.data.repository.TaskRepository;
import com.george.android.tasker.data.model.Task;

import java.util.List;

public class TasksViewModel extends AndroidViewModel {

    private final TaskRepository repository;
    private final LiveData<List<Task>> allTasks;

    public TasksViewModel(@NonNull Application application) {
        super(application);
        repository = new TaskRepository(application);
        allTasks = repository.getAllTasks();
    }

    public void insert(Task task) {
        repository.insert(task);
    }

    public void update(Task task) {
        repository.update(task);
    }

    public void delete(int taskId) {
        repository.delete(taskId);
    }

    public void deleteTasksFolder(int folderId) {
        repository.deleteTasksFolder(folderId);
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public void updatePosition(List<Task> tasks) {
        repository.updatePosition(tasks);
    }

    public LiveData<List<Task>> getFoldersTasks(int folderId) {
        return repository.getFoldersTasks(folderId);
    }

    public LiveData<List<Task>> findTask(String search) {
        return repository.findTask(search);
    }

}