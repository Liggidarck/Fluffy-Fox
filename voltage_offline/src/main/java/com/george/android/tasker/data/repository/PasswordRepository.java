package com.george.android.tasker.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.george.android.tasker.data.database.VoltageDatabase;
import com.george.android.tasker.data.database.dao.PasswordDao;
import com.george.android.tasker.data.model.Password;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PasswordRepository {

    private final PasswordDao passwordDao;
    private final LiveData<List<Password>> allPasswords;
    final ExecutorService service = Executors.newSingleThreadExecutor();

    public PasswordRepository(Application application) {
        VoltageDatabase database = VoltageDatabase.getInstance(application);
        passwordDao = database.passwordDao();
        allPasswords = passwordDao.getAllPasswords();
    }

    public void insert(Password password) {
        service.execute(() -> passwordDao.insert(password));
    }

    public void update(Password password) {
        service.execute(() -> passwordDao.update(password));
    }

    public void delete(int passwordId) {
        service.execute(() -> passwordDao.delete(passwordId));
    }

    public LiveData<List<Password>> getAllPasswords() {
        return allPasswords;
    }

    public LiveData<List<Password>> findPassword(String search) {
        return passwordDao.findPassword(search);
    }

}
