package com.george.android.tasker.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_table")
public class Note {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private final String title;
    private final String description;
    private int position;

    public Note(String title, String description, int position) {
        this.title = title;
        this.description = description;
        this.position = position;
    }

    public void setPosition(int position) {
        this.position = position;
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

    public int getPosition() {
        return position;
    }

    public String getDescription() {
        return description;
    }

}
