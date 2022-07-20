package com.george.android.tasker.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_bin")
public class BinNote {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private final String title;
    private final String description;

    public BinNote(String title, String description) {
        this.title = title;
        this.description = description;
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
}
