package com.george.android.voltage_online.network.endpoint;

import com.george.android.voltage_online.model.Folder;
import com.george.android.voltage_online.model.Message;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface IFolder {

    @POST("folders/create")
    Call<Folder> createFolder(@Body Folder folder);

    @PUT("folders/update")
    Call<Message> updateNote(@Query("id") long id, @Body Folder folder);

    @DELETE("folders/delete")
    Call<Message> deleteFolder(@Query("id") long id);

    @GET("folders/getAllFolders")
    Call<List<Folder>> getAllFolders();

}
