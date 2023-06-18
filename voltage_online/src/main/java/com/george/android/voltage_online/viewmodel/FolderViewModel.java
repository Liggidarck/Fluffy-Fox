package com.george.android.voltage_online.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.george.android.voltage_online.model.Folder;
import com.george.android.voltage_online.model.Message;
import com.george.android.voltage_online.repository.FolderRepository;

import java.util.List;

public class FolderViewModel extends AndroidViewModel {

    private final FolderRepository folderRepository;
    public FolderViewModel(@NonNull Application application) {
        super(application);
        folderRepository = new FolderRepository();
    }

    public MutableLiveData<Folder> createFolder(Folder folder) {
        return folderRepository.createFolder(folder);
    }

    public MutableLiveData<Message> updateFolder(long id, Folder folder) {
        return folderRepository.updateFolder(id, folder);
    }

    public MutableLiveData<Message> deleteFolderAndTasks(long id) {
        return folderRepository.deleteFolder(id);
    }

    public MutableLiveData<List<Folder>> getAllFolder() {
        return folderRepository.getAllFolders();
    }


}
