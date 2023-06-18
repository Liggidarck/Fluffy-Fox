package com.george.android.voltage_online.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.george.android.voltage_online.model.Message;
import com.george.android.voltage_online.model.Task;
import com.george.android.voltage_online.repository.TaskRepository;

import java.util.List;

public class TasksViewModel extends AndroidViewModel {

    private final TaskRepository taskRepository;

    public TasksViewModel(@NonNull Application application) {
        super(application);
        taskRepository = new TaskRepository();
    }

    public MutableLiveData<Task> createTask(Task task) {
        return taskRepository.createTask(task);
    }

    public MutableLiveData<Message> updateTask(long id, Task task) {
        return taskRepository.updateTask(id, task);
    }

    public MutableLiveData<Message> deleteTask(long id) {
        return taskRepository.deleteTask(id);
    }

    public MutableLiveData<List<Task>> getTasksByFolderId(long folderId) {
        return taskRepository.getTasksByFolderId(folderId);
    }

}
