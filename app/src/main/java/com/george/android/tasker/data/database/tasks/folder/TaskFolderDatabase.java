package com.george.android.tasker.data.database.tasks.folder;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.george.android.tasker.data.model.TaskFolder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
            ExecutorService service = Executors.newSingleThreadExecutor();
            service.execute(() -> instance.taskFolderDao());
        }
    };

}
