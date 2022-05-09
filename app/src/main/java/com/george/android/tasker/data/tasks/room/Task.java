package com.george.android.tasker.data.tasks.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "task_table")
public class Task {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private final String title;
    private final boolean status;
    private final String dateComplete;
    private final String dateCreate;
    private final String noteTask;

    public Task(String title, boolean status, String dateComplete, String dateCreate, String noteTask) {
        this.title = title;
        this.status = status;
        this.dateComplete = dateComplete;
        this.dateCreate = dateCreate;
        this.noteTask = noteTask;
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
}
