package com.george.android.voltage_online.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Password implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;

    private final String url;
    private final String email;
    private final String password;

    public Password(String url, String email, String password) {
        this.url = url;
        this.email = email;
        this.password = password;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
