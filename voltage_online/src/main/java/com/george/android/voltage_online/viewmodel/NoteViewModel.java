package com.george.android.voltage_online.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.george.android.voltage_online.model.Note;
import com.george.android.voltage_online.repository.NoteRepository;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    private final NoteRepository noteRepository;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        noteRepository = new NoteRepository();
    }

    public MutableLiveData<List<Note>> getAllNotes() {
        return noteRepository.getAllNotes();
    }

}
