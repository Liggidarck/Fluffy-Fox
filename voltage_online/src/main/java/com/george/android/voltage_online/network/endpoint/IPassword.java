package com.george.android.voltage_online.network.endpoint;

import com.george.android.voltage_online.model.Message;
import com.george.android.voltage_online.model.Password;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface IPassword {

    @POST("passwords/create")
    Call<Password> createPassword(@Body Password password);

    @PUT("passwords/update")
    Call<Message> updatePassword(@Query("id") long id, @Body Password password);

    @DELETE("passwords/delete")
    Call<Message> deletePassword(@Query("id") long id);

    @GET("passwords/get")
    Call<List<Password>> getAllPasswords();

}
