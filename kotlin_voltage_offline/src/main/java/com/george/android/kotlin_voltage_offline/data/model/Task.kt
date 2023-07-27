package com.george.android.kotlin_voltage_offline.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_table")
class Task(
    var title: String, var status: Boolean, var dateComplete: String,
    var timeComplete: String, var noteTask: String, var folderId: Int
) {

    @PrimaryKey(autoGenerate = true)
    var id = 0

}