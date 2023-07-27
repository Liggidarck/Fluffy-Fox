package com.george.android.tasker.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.george.android.tasker.data.database.VoltageDatabase;
import com.george.android.tasker.data.database.dao.NoteDao;
import com.george.android.tasker.data.model.Note;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NoteRepository {
    private final NoteDao noteDao;
    private final LiveData<List<Note>> allNotes;
    final ExecutorService service = Executors.newSingleThreadExecutor();

    public NoteRepository(Application application) {
        VoltageDatabase database = VoltageDatabase.getInstance(application);
        noteDao = database.noteDao();
        allNotes = noteDao.getAllNotes();
    }

    public void insert(Note note) {
        service.execute(() -> noteDao.insert(note));
    }

    public void update(Note note) {
        service.execute(() -> noteDao.update(note));
    }

    public void updatePosition(List<Note> noteList) {
        service.execute(() -> noteDao.updatePosition(noteList));
    }

    public void delete(int noteId) {
        service.execute(() -> noteDao.delete(noteId));
    }

    public void deleteAllNotes() {
        service.execute(noteDao::deleteAllNotes);
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    public LiveData<List<Note>> findNotes(String query) {
        return noteDao.findNote(query);
    }

}
