package com.george.android.voltage_online.network.endpoint;

import com.george.android.voltage_online.model.Note;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface INote {

    @POST("note/create")
    Call<Note> createNote(@Body Note note);

    @PUT("note/update")
    Call<Note> updateNote(@Body Note note);

    @DELETE("note/delete")
    Call<Note> deleteNote(@Query("id") long id);

    @GET("note/getAll")
    Call<List<Note>> getAllNotes();

}
