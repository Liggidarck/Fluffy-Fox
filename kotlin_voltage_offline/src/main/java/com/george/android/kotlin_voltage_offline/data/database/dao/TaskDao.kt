package com.george.android.kotlin_voltage_offline.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import com.george.android.kotlin_voltage_offline.data.model.Task

@Dao
interface TaskDao {

    @Insert
    fun insert(task: Task)

}