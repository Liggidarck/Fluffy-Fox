package com.george.android.tasker.data.passwords;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.george.android.tasker.data.passwords.room.Password;
import com.george.android.tasker.data.passwords.room.PasswordDao;
import com.george.android.tasker.data.passwords.room.PasswordDatabase;

import java.util.List;

public class PasswordRepository {

    private final PasswordDao passwordDao;
    private final LiveData<List<Password>> allPasswords;

    public PasswordRepository(Application application) {
        PasswordDatabase database = PasswordDatabase.getInstance(application);
        passwordDao = database.passwordDao();
        allPasswords = passwordDao.getAllPasswords();
    }

    public void insert(Password password) {
        new InsertPasswordAsyncTask(passwordDao).execute(password);
    }

    public void update(Password password) {
        new UpdatePasswordAsyncTask(passwordDao).execute(password);
    }

    public void delete(Password password) {
        new DeletePasswordAsyncTask(passwordDao).execute(password);
    }

    public LiveData<List<Password>> getAllPasswords() {
        return allPasswords;
    }

    private static class InsertPasswordAsyncTask extends AsyncTask<Password, Void, Void> {

        private final PasswordDao passwordDao;
        private InsertPasswordAsyncTask(PasswordDao passwordDao) {
            this.passwordDao = passwordDao;
        }

        @Override
        protected Void doInBackground(Password... passwords) {
            passwordDao.insert(passwords[0]);
            return null;
        }
    }

    private static class UpdatePasswordAsyncTask extends AsyncTask<Password, Void, Void> {

        private final PasswordDao passwordDao;
        private UpdatePasswordAsyncTask(PasswordDao passwordDao) {
            this.passwordDao = passwordDao;
        }

        @Override
        protected Void doInBackground(Password... passwords) {
            passwordDao.update(passwords[0]);
            return null;
        }
    }


    private static class DeletePasswordAsyncTask extends AsyncTask<Password, Void, Void> {

        private final PasswordDao passwordDao;
        private DeletePasswordAsyncTask(PasswordDao passwordDao) {
            this.passwordDao = passwordDao;
        }

        @Override
        protected Void doInBackground(Password... passwords) {
            passwordDao.delete(passwords[0]);
            return null;
        }
    }

}
