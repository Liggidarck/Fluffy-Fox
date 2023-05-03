package com.george.android.voltage_online.model;

public class TaskFolder {

    private int folderId;

    private final String nameFolder;

    public TaskFolder(String nameFolder) {
        this.nameFolder = nameFolder;
    }

    public void setFolderId(int folderId) {
        this.folderId = folderId;
    }

    public int getFolderId() {
        return folderId;
    }

    public String getNameFolder() {
        return nameFolder;
    }
}
