package com.george.android.tasker.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.george.android.tasker.data.model.BinNote;
import com.george.android.tasker.data.database.notes.recycle_bin.BinNoteDao;
import com.george.android.tasker.data.database.notes.recycle_bin.BinNoteDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BinNoteRepository {

    private final BinNoteDao binNoteDao;
    private final LiveData<List<BinNote>> allBinNotes;
    final ExecutorService service = Executors.newSingleThreadExecutor();

    public BinNoteRepository(Application application) {
        BinNoteDatabase database = BinNoteDatabase.getInstance(application);
        binNoteDao = database.noteBinDao();
        allBinNotes = binNoteDao.getAllBinNote();
    }

    public void insert(BinNote binNote) {
        service.execute(() -> binNoteDao.insert(binNote));
    }

    public void clearBin() {
        service.execute(binNoteDao::deleteBin);
    }

    public void delete(BinNote binNote) {
        service.execute(() -> binNoteDao.delete(binNote));
    }

    public LiveData<List<BinNote>> getAllBinNotes() {
        return allBinNotes;
    }

}
