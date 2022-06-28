package com.george.android.tasker.data.tasks.folder;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TaskFolderRepository {

    TaskFolderDao taskFolderDao;
    LiveData<List<TaskFolder>> allFolderTask;

    public TaskFolderRepository(Application application) {
        TaskFolderDatabase database = TaskFolderDatabase.getInstance(application);
        taskFolderDao = database.taskFolderDao();
        allFolderTask = taskFolderDao.getAllTaskFolders();
    }

    public void insert(TaskFolder taskFolder) {
        new insertFolderTask(taskFolderDao).execute(taskFolder);
    }

    public void update(TaskFolder taskFolder) {
        new updateFolderTask(taskFolderDao).execute(taskFolder);
    }

    public void delete(TaskFolder taskFolder) {
        new deleteFolderTask(taskFolderDao).execute(taskFolder);
    }

    public LiveData<List<TaskFolder>> getAllFolderTask() {
        return allFolderTask;
    }

    public static class insertFolderTask extends AsyncTask<TaskFolder, Void, Void> {

        private TaskFolderDao taskFolderDao;
        private insertFolderTask(TaskFolderDao taskFolderDao) {
            this.taskFolderDao = taskFolderDao;
        }


        @Override
        protected Void doInBackground(TaskFolder... taskFolders) {
            taskFolderDao.insert(taskFolders[0]);
            return null;
        }
    }

    public static class updateFolderTask extends AsyncTask<TaskFolder, Void, Void> {

        private TaskFolderDao taskFolderDao;
        private updateFolderTask(TaskFolderDao taskFolderDao) {
            this.taskFolderDao = taskFolderDao;
        }


        @Override
        protected Void doInBackground(TaskFolder... taskFolders) {
            taskFolderDao.update(taskFolders[0]);
            return null;
        }
    }

    public static class deleteFolderTask extends AsyncTask<TaskFolder, Void, Void> {

        private TaskFolderDao taskFolderDao;
        private deleteFolderTask(TaskFolderDao taskFolderDao) {
            this.taskFolderDao = taskFolderDao;
        }


        @Override
        protected Void doInBackground(TaskFolder... taskFolders) {
            taskFolderDao.delete(taskFolders[0]);
            return null;
        }
    }

}
