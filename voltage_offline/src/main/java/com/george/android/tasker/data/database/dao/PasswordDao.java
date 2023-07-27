package com.george.android.tasker.data.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.george.android.tasker.data.model.Password;

import java.util.List;

@Dao
public interface PasswordDao {

    @Insert
    void insert(Password password);

    @Update
    void update(Password password);

    @Query("DELETE FROM password_table WHERE id LIKE :passwordId")
    void delete(int passwordId);

    @Query("SELECT * FROM password_table")
    LiveData<List<Password>> getAllPasswords();

    @Query("SELECT * FROM password_table WHERE url LIKE '%' || :search || '%'")
    LiveData<List<Password>> findPassword(String search);

}
