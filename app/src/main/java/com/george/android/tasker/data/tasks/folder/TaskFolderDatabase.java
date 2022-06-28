package com.george.android.tasker.data.tasks.folder;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {TaskFolder.class}, version = 1)
public abstract class TaskFolderDatabase extends RoomDatabase {

    private static TaskFolderDatabase instance;
    public abstract TaskFolderDao taskFolderDao();

    public static synchronized TaskFolderDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                     TaskFolderDatabase.class, "task_folder_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static final RoomDatabase.Callback roomCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private TaskFolderDao taskFolderDao;

        private PopulateDbAsyncTask(TaskFolderDatabase db) {
            taskFolderDao = db.taskFolderDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }

}
