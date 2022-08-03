package com.george.android.tasker.data.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.george.android.tasker.data.model.BinNote;
import com.george.android.tasker.data.repository.BinNoteRepository;

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

    public void delete(int id) {
        repository.delete(id);
    }

    public void clearBin() {
        repository.clearBin();
    }

    public LiveData<List<BinNote>> getAllBinNotes() {
        return allBinNotes;
    }

}
