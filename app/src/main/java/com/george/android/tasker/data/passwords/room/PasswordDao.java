package com.george.android.tasker.data.passwords.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PasswordDao {

    @Insert
    void insert(Password password);

    @Update
    void update(Password password);

    @Delete
    void delete(Password password);

    @Query("SELECT * FROM password_table")
    LiveData<List<Password>> getAllPasswords();

}
