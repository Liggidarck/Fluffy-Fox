package com.george.android.tasker.ui.passwords;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.george.android.tasker.data.passwords.PasswordRepository;
import com.george.android.tasker.data.passwords.room.Password;

import java.util.List;

public class PasswordsViewModel extends AndroidViewModel {

    private final PasswordRepository repository;
    private final LiveData<List<Password>> allPasswords;

    public PasswordsViewModel(Application application) {
        super(application);
        repository = new PasswordRepository(application);
        allPasswords = repository.getAllPasswords();
    }

    public void insert(Password password) {
        repository.insert(password);
    }

    public void update(Password password) {
        repository.update(password);
    }

    public void delete(Password password) {
        repository.delete(password);
    }

    public LiveData<List<Password>> getAllPasswords() {
        return allPasswords;
    }

}