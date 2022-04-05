package com.george.android.tasker.data.passwords.room;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

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
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private PasswordDao passwordDao;
        private PopulateDbAsyncTask(PasswordDatabase db) {
            passwordDao = db.passwordDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }

}
