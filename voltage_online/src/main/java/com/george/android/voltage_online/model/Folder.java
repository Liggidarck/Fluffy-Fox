package com.george.android.voltage_online.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Folder implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;

    @Expose
    private final String nameFolder;

    public Folder(int id, String nameFolder) {
        this.id = id;
        this.nameFolder = nameFolder;
    }

    public Folder(String nameFolder) {
        this.nameFolder = nameFolder;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getNameFolder() {
        return nameFolder;
    }
}
