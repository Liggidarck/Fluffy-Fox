package com.george.android.tasker.ui.tasks.folder;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.george.android.tasker.data.tasks.folder.TaskFolder;
import com.george.android.tasker.data.tasks.folder.TaskFolderRepository;

import java.util.List;

public class TasksFolderViewModel extends AndroidViewModel {

    TaskFolderRepository repository;
    LiveData<List<TaskFolder>> allFolders;

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

    public void delete(TaskFolder taskFolder) {
        repository.delete(taskFolder);
    }

    public LiveData<List<TaskFolder>> getAllFolders() {
        return allFolders;
    }

}
