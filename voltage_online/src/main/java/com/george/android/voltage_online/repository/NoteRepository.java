package com.george.android.voltage_online.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

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
