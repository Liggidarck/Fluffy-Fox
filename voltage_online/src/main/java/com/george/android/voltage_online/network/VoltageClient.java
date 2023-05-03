package com.george.android.voltage_online.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VoltageClient {

    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8080/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
