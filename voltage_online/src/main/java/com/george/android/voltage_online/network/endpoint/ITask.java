package com.george.android.voltage_online.network.endpoint;

import com.george.android.voltage_online.model.Message;
import com.george.android.voltage_online.model.Task;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ITask {

    @POST("tasks/create")
    Call<Task> createTask(@Body Task task);

    @PUT("tasks/update")
    Call<Message> updateTask(@Query("id") long id,
                             @Body Task task);

    @DELETE("tasks/delete")
    Call<Message> deleteTask(@Query("id") long id);

    @GET("tasks/getTasksByFolderId")
    Call<List<Task>> getTasksByFolderId(@Query("id") long folderId);

}
