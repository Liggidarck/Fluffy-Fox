package com.george.android.voltage_online.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.george.android.voltage_online.model.Message;
import com.george.android.voltage_online.model.Task;
import com.george.android.voltage_online.network.VoltageClient;
import com.george.android.voltage_online.network.endpoint.ITask;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskRepository {

    private final ITask iTask;

    public static final String TAG = TaskRepository.class.getSimpleName();

    public TaskRepository() {
        iTask = VoltageClient.getClient().create(ITask.class);
    }


    public MutableLiveData<Task> createTask(Task task) {
        MutableLiveData<Task> taskMutableLiveData = new MutableLiveData<>();

        iTask.createTask(task).enqueue(new Callback<Task>() {
            @Override
            public void onResponse(@NonNull Call<Task> call, @NonNull Response<Task> response) {
                Log.d(TAG, "onResponse: createTask: " + response.code());
                taskMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Task> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: createTask: ", t);
            }
        });

        return taskMutableLiveData;
    }


    public MutableLiveData<Message> updateTask(long id, Task task) {
        MutableLiveData<Message> messageMutableLiveData = new MutableLiveData<>();


        Log.d(TAG, "updateTask: " + task.isStatus());

        iTask.updateTask(id, task).enqueue(new Callback<Message>() {
            @Override
            public void onResponse(@NonNull Call<Message> call, @NonNull Response<Message> response) {
                Log.d(TAG, "onResponse: update: " + response.code());
                messageMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Message> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: updateTask", t);
            }
        });

        return messageMutableLiveData;
    }

    public MutableLiveData<Message> deleteTask(long id) {
        MutableLiveData<Message> mutableLiveData = new MutableLiveData<>();

        iTask.deleteTask(id).enqueue(new Callback<Message>() {
            @Override
            public void onResponse(@NonNull Call<Message> call, @NonNull Response<Message> response) {
                Log.d(TAG, "onResponse: delete Task: " + response.code());
                mutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Message> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: deleteTask: ", t);
            }
        });

        return mutableLiveData;
    }

    public MutableLiveData<List<Task>> getTasksByFolderId(long id) {
        MutableLiveData<List<Task>> listMutableLiveData = new MutableLiveData<>();

        iTask.getTasksByFolderId(id).enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(@NonNull Call<List<Task>> call, @NonNull Response<List<Task>> response) {
                Log.d(TAG, "onResponse: get Tasks: " + response.code());
                listMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<Task>> call, @NonNull Throwable t) {

                Log.e(TAG, "onFailure: get tasks", t);

            }
        });

        return listMutableLiveData;
    }

}
