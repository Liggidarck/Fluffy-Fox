package com.george.android.voltage_online.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.george.android.voltage_online.model.Folder;
import com.george.android.voltage_online.model.Message;
import com.george.android.voltage_online.network.VoltageClient;
import com.george.android.voltage_online.network.endpoint.IFolder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FolderRepository {

    private final IFolder iFolder;

    public static final String TAG = FolderRepository.class.getSimpleName();

    public FolderRepository() {
        this.iFolder = VoltageClient.getClient().create(IFolder.class);
    }

    public MutableLiveData<Folder> createFolder(Folder folder) {
        MutableLiveData<Folder> folderMutableLiveData = new MutableLiveData<>();

        iFolder.createFolder(folder).enqueue(new Callback<Folder>() {
            @Override
            public void onResponse(@NonNull Call<Folder> call, @NonNull Response<Folder> response) {
                Log.i(TAG, "onResponse: createFolder: " + response.code());
                folderMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Folder> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });

        return folderMutableLiveData;
    }

    public MutableLiveData<Message> updateFolder(long id, Folder folder) {
        MutableLiveData<Message> messageMutableLiveData = new MutableLiveData<>();

        iFolder.updateNote(id, folder).enqueue(new Callback<Message>() {
            @Override
            public void onResponse(@NonNull Call<Message> call, @NonNull Response<Message> response) {
                Log.i(TAG, "onResponse: updateFolder: " + response.code());
                messageMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Message> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: updateFolder: ", t);
            }
        });

        return messageMutableLiveData;
    }


    public MutableLiveData<Message> deleteFolder(long id) {
        MutableLiveData<Message> messageMutableLiveData = new MutableLiveData<>();

        iFolder.deleteFolder(id).enqueue(new Callback<Message>() {
            @Override
            public void onResponse(@NonNull Call<Message> call, @NonNull Response<Message> response) {
                Log.i(TAG, "onResponse: deleteFolder: " + response.code());
                messageMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Message> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: deleteFolder: ", t);
            }
        });

        return messageMutableLiveData;
    }


    public MutableLiveData<List<Folder>> getAllFolders() {
        MutableLiveData<List<Folder>> folders = new MutableLiveData<>();

        iFolder.getAllFolders().enqueue(new Callback<List<Folder>>() {
            @Override
            public void onResponse(@NonNull Call<List<Folder>> call, @NonNull Response<List<Folder>> response) {
                Log.i(TAG, "onResponse: get All folder: " + response.code());
                assert response.body() != null;
                folders.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<Folder>> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: getAllFOlder:", t);
            }
        });

        return folders;
    }

}
