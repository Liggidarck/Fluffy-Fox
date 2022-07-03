package com.george.android.tasker.data.tasks.folder;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "folder_task_table")
public class TaskFolder {

    @PrimaryKey(autoGenerate = true)
    private int folderId;

    private String nameFolder;

    public TaskFolder(String nameFolder) {
        this.nameFolder = nameFolder;
    }

    public int getFolderId() {
        return folderId;
    }

    public void setFolderId(int folderId) {
        this.folderId = folderId;
    }

    public String getNameFolder() {
        return nameFolder;
    }

    public void setNameFolder(String nameFolder) {
        this.nameFolder = nameFolder;
    }
}