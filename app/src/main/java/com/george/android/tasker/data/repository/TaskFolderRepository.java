package com.george.android.tasker.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.george.android.tasker.data.model.TaskFolder;
import com.george.android.tasker.data.database.tasks.folder.TaskFolderDao;
import com.george.android.tasker.data.database.tasks.folder.TaskFolderDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskFolderRepository {

    final TaskFolderDao taskFolderDao;
    final LiveData<List<TaskFolder>> allFolderTask;
    final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public TaskFolderRepository(Application application) {
        TaskFolderDatabase database = TaskFolderDatabase.getInstance(application);
        taskFolderDao = database.taskFolderDao();
        allFolderTask = taskFolderDao.getAllTaskFolders();
    }

    public void insert(TaskFolder taskFolder) {
        executorService.execute(() -> taskFolderDao.insert(taskFolder));
    }

    public void update(TaskFolder taskFolder) {
        executorService.execute(() -> taskFolderDao.update(taskFolder));
    }

    public void delete(int folderId) {
        executorService.execute(() -> taskFolderDao.delete(folderId));
    }

    public LiveData<List<TaskFolder>> getAllFolderTask() {
        return allFolderTask;
    }
}
