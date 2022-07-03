package com.george.android.tasker.data.passwords.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "password_table")
public class Password {

    @PrimaryKey(autoGenerate = true)
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
