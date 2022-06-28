package com.george.android.tasker.data.notes.recycle_bin;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class BinNoteRepository {

    private final BinNoteDao binNoteDao;
    private final LiveData<List<BinNote>> allBinNotes;

    public BinNoteRepository(Application application) {
        BinNoteDatabase database = BinNoteDatabase.getInstance(application);
        binNoteDao = database.noteBinDao();
        allBinNotes = binNoteDao.getAllBinNote();
    }

    public void insert(BinNote binNote) {
        new InsertBinAsyncTask(binNoteDao).execute(binNote);
    }

    public void clearBin() {
        new ClearBinAsyncTask(binNoteDao).execute();
    }

    public void delete(BinNote binNote) {
        new DeleteBinAsyncTask(binNoteDao).execute(binNote);
    }

    public LiveData<List<BinNote>> getAllBinNotes() {
        return allBinNotes;
    }

    private static class InsertBinAsyncTask extends AsyncTask<BinNote, Void, Void> {
        private final BinNoteDao noteDao;

        private InsertBinAsyncTask(BinNoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(BinNote... binNotes) {
            noteDao.insert(binNotes[0]);
            return null;
        }
    }

    private static class DeleteBinAsyncTask extends AsyncTask<BinNote, Void, Void> {
        private final BinNoteDao noteDao;

        private DeleteBinAsyncTask(BinNoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(BinNote... binNotes) {
            noteDao.delete(binNotes[0]);
            return null;
        }
    }

    private static class ClearBinAsyncTask extends AsyncTask<BinNote, Void, Void> {
        private final BinNoteDao noteDao;

        private ClearBinAsyncTask(BinNoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(BinNote... binNotes) {
            noteDao.deleteBin();
            return null;
        }
    }


}
