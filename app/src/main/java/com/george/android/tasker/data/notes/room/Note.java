package com.george.android.tasker.data.notes.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_table")
public class Note {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String description;
    private String dateCreate;

    public Note(String title, String description, String dateCreate) {
        this.title = title;
        this.description = description;
        this.dateCreate = dateCreate;
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

    public String getDescription() {
        return description;
    }

    public String getDateCreate() {
        return dateCreate;
    }
}
