package com.george.android.tasker.data.notes.recycle_bin;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {BinNote.class}, version = 1)
public abstract class BinNoteDatabase extends RoomDatabase {

    private static BinNoteDatabase instance;
    public abstract BinNoteDao noteBinDao();

    public static synchronized BinNoteDatabase getInstance(Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    BinNoteDatabase.class, "bin_note_database")
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
            new PopulateDb(instance).execute();
        }
    };

    private static class PopulateDb extends AsyncTask<Void, Void, Void> {

        private final BinNoteDao noteBinDao;
        private PopulateDb(BinNoteDatabase db) {
            noteBinDao = db.noteBinDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteBinDao.insert(new BinNote("Bin 1", "Description 1"));
            noteBinDao.insert(new BinNote("Bin 1", "Description 1"));
            noteBinDao.insert(new BinNote("Bin 1", "Description 1"));
            noteBinDao.insert(new BinNote("Bin 1", "Description 1"));
            noteBinDao.insert(new BinNote("Bin 1", "Description 1"));
            noteBinDao.insert(new BinNote("Bin 1", "Description 1"));
            return null;
        }
    }

}
