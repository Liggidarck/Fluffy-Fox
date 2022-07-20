package com.george.android.tasker.data.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.george.android.tasker.data.model.TaskFolder;
import com.george.android.tasker.data.repository.TaskFolderRepository;

import java.util.List;

public class TasksFolderViewModel extends AndroidViewModel {

    final TaskFolderRepository repository;
    final LiveData<List<TaskFolder>> allFolders;

    public TasksFolderViewModel(@NonNull Application application) {
        super(application);
        repository = new TaskFolderRepository(application);
        allFolders = repository.getAllFolderTask();
    }

    public void insert(TaskFolder taskFolder) {
        repository.insert(taskFolder);
    }

    public void update(TaskFolder taskFolder) {
        repository.update(taskFolder);
    }

    public void delete(int folderId) {
        repository.delete(folderId);
    }

    public LiveData<List<TaskFolder>> getAllFolders() {
        return allFolders;
    }

}
