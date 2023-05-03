package com.george.android.tasker.data.database.passwords;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.george.android.tasker.data.model.Password;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Password.class}, version = 1)
public abstract class PasswordDatabase extends RoomDatabase {

    private static PasswordDatabase instance;

    public abstract PasswordDao passwordDao();

    public static synchronized PasswordDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    PasswordDatabase.class, "password_database")
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
            service.execute(() -> instance.passwordDao());
        }
    };

}
