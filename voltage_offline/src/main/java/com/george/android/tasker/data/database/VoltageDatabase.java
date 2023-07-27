package com.george.android.tasker.data.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.george.android.tasker.data.database.dao.NoteDao;
import com.george.android.tasker.data.database.dao.PasswordDao;
import com.george.android.tasker.data.database.dao.TaskFolderDao;
import com.george.android.tasker.data.database.dao.TaskDao;
import com.george.android.tasker.data.model.Note;
import com.george.android.tasker.data.model.Password;
import com.george.android.tasker.data.model.Task;
import com.george.android.tasker.data.model.TaskFolder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Note.class, TaskFolder.class, Task.class, Password.class}, version = 1)
public abstract class VoltageDatabase extends RoomDatabase {

    private static VoltageDatabase instance;

    public abstract NoteDao noteDao();

    public abstract TaskFolderDao taskFolderDao();

    public abstract TaskDao taskDao();

    public abstract PasswordDao passwordDao();

    public static synchronized VoltageDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            VoltageDatabase.class, "voltage_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static final RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            ExecutorService service = Executors.newSingleThreadExecutor();
            service.execute(() -> {
                instance.noteDao().insert(new Note("Note data", "Data"));

                instance.taskFolderDao().insert(new TaskFolder("name"));

                instance.taskDao().insert(new Task("test", false,
                        "123", "123", "123", 1));

                instance.passwordDao().insert(new Password("url", "email", "password"));
            });
        }
    };

}
