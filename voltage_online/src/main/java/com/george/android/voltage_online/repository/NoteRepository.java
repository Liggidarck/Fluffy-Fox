package com.george.android.voltage_online.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.george.android.voltage_online.model.Message;
import com.george.android.voltage_online.model.Note;
import com.george.android.voltage_online.network.VoltageClient;
import com.george.android.voltage_online.network.endpoint.INote;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoteRepository {

    private final INote iNote;

    public static final String TAG = NoteRepository.class.getSimpleName();

    public NoteRepository() {
        iNote = VoltageClient.getClient().create(INote.class);
    }

    public MutableLiveData<Note> createNote(Note note) {
        MutableLiveData<Note> noteMutableLiveData = new MutableLiveData<>();

        iNote.createNote(note).enqueue(new Callback<Note>() {
            @Override
            public void onResponse(@NonNull Call<Note> call, @NonNull Response<Note> response) {
                noteMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Note> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });

        return noteMutableLiveData;
    }

    public MutableLiveData<Message> updateNote(long id, Note note) {
        MutableLiveData<Message> messageMutableLiveData = new MutableLiveData<>();

        iNote.updateNote(id, note).enqueue(new Callback<Message>() {
            @Override
            public void onResponse(@NonNull Call<Message> call, @NonNull Response<Message> response) {
                Log.d(TAG, "onResponse: code: " + response.code());
                messageMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Message> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });

        return messageMutableLiveData;
    }

    public MutableLiveData<Message> deleteNote(long id) {
        MutableLiveData<Message> message = new MutableLiveData<>();

        iNote.deleteNote(id).enqueue(new Callback<Message>() {
            @Override
            public void onResponse(@NonNull Call<Message> call, @NonNull Response<Message> response) {
                Log.d(TAG, "onResponse: code: " + response.code());
                message.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Message> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });

        return message;
    }

    public MutableLiveData<List<Note>> getAllNotes() {
        MutableLiveData<List<Note>> notes = new MutableLiveData<>();

        iNote.getAllNotes().enqueue(new Callback<List<Note>>() {
            @Override
            public void onResponse(@NonNull Call<List<Note>> call, @NonNull Response<List<Note>> response) {
                Log.d(TAG, "onResponse: code: " + response.code());
                notes.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<Note>> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });

        return notes;
    }
}
