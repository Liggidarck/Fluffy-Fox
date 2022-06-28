package com.george.android.tasker.ui.notes.view_models;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.george.android.tasker.data.notes.recycle_bin.BinNote;
import com.george.android.tasker.data.notes.recycle_bin.BinNoteRepository;

import java.util.List;

public class NoteBinViewModel extends AndroidViewModel {

    private final BinNoteRepository repository;
    private final LiveData<List<BinNote>> allBinNotes;

    public NoteBinViewModel(Application application){
        super(application);
        repository = new BinNoteRepository(application);
        allBinNotes = repository.getAllBinNotes();
    }

    public void insert(BinNote note) {
        repository.insert(note);
    }

    public void delete(BinNote binNote) {
        repository.delete(binNote);
    }

    public void clearBin() {
        repository.clearBin();
    }

    public LiveData<List<BinNote>> getAllBinNotes() {
        return allBinNotes;
    }

}
