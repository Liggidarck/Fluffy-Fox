package com.george.android.voltage_online.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.george.android.voltage_online.model.Message;
import com.george.android.voltage_online.model.Password;
import com.george.android.voltage_online.network.VoltageClient;
import com.george.android.voltage_online.network.endpoint.IPassword;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PasswordRepository {

    private final IPassword iPassword;

    public static final String TAG = PasswordRepository.class.getSimpleName();

    public PasswordRepository() {
        iPassword = VoltageClient.getClient().create(IPassword.class);
    }

    public MutableLiveData<Password> createPassword(Password password) {
        MutableLiveData<Password> passwordMutableLiveData = new MutableLiveData<>();

        iPassword.createPassword(password).enqueue(new Callback<Password>() {
            @Override
            public void onResponse(@NonNull Call<Password> call, @NonNull Response<Password> response) {
                Log.d(TAG, "onResponse: createPassword: " + response.code());
                passwordMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Password> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: createPassword: ", t);
            }
        });

        return passwordMutableLiveData;
    }

    public MutableLiveData<Message> updatePassword(long id, Password password) {
        MutableLiveData<Message> passwordMutableLiveData = new MutableLiveData<>();

        iPassword.updatePassword(id, password).enqueue(new Callback<Message>() {
            @Override
            public void onResponse(@NonNull Call<Message> call, @NonNull Response<Message> response) {
                Log.d(TAG, "onResponse: updatePassword: " + response.code());
                passwordMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Message> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: updatePassword: ", t);
            }
        });

        return passwordMutableLiveData;
    }

    public MutableLiveData<Message> deletePassword(long id) {
        MutableLiveData<Message> messageMutableLiveData = new MutableLiveData<>();

        iPassword.deletePassword(id).enqueue(new Callback<Message>() {
            @Override
            public void onResponse(@NonNull Call<Message> call, @NonNull Response<Message> response) {
                Log.d(TAG, "onResponse: deletePassword: " + response.code());
                messageMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Message> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: deletePassword: ", t);
            }
        });

        return messageMutableLiveData;
    }

    public MutableLiveData<List<Password>> getAllPassword() {
        MutableLiveData<List<Password>> listMutableLiveData = new MutableLiveData<>();

        iPassword.getAllPasswords().enqueue(new Callback<List<Password>>() {
            @Override
            public void onResponse(@NonNull Call<List<Password>> call, @NonNull Response<List<Password>> response) {
                Log.d(TAG, "onResponse: getAllPassword: " + response.code());
                listMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<Password>> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: getAllPassword: ", t);
            }
        });

        return listMutableLiveData;
    }

}
