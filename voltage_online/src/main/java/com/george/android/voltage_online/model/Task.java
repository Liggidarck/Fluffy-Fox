package com.george.android.voltage_online.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Task implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;
    @Expose
    private final String title;
    @Expose
    private final boolean status;
    @Expose
    private final String dateComplete;
    @Expose
    private final String dateCreate;
    @Expose
    private final String noteTask;
    @Expose
    private final int folderId;

    public Task(String title, boolean status, String dateComplete, String dateCreate,
                String noteTask, int folderId) {
        this.title = title;
        this.status = status;
        this.dateComplete = dateComplete;
        this.dateCreate = dateCreate;
        this.noteTask = noteTask;
        this.folderId = folderId;
    }

    public Task(int id, String title, boolean status, String dateComplete, String dateCreate, String noteTask, int folderId) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.dateComplete = dateComplete;
        this.dateCreate = dateCreate;
        this.noteTask = noteTask;
        this.folderId = folderId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isStatus() {
        return status;
    }

    public String getDateComplete() {
        return dateComplete;
    }

    public String getDateCreate() {
        return dateCreate;
    }

    public String getNoteTask() {
        return noteTask;
    }

    public int getFolderId() {
        return folderId;
    }
}
