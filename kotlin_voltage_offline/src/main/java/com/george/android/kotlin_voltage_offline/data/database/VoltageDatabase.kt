package com.george.android.kotlin_voltage_offline.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.george.android.kotlin_voltage_offline.data.database.dao.NoteDao
import com.george.android.kotlin_voltage_offline.data.database.dao.TaskDao
import com.george.android.kotlin_voltage_offline.data.model.Note
import com.george.android.kotlin_voltage_offline.data.model.Task
import java.util.concurrent.Executors

@Database(entities = [Note::class, Task::class], version = 1)
abstract class VoltageDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao
    abstract fun taskDao(): TaskDao

    companion object {
        private var instance: VoltageDatabase? = null

        @Synchronized
        fun getInstance(context: Context): VoltageDatabase? {
            if (instance == null) {
                instance = databaseBuilder(
                    context.applicationContext,
                    VoltageDatabase::class.java, "voltage_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(noteCallback)
                    .build()
            }
            return instance
        }

        private val noteCallback: Callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                val service = Executors.newSingleThreadExecutor()
                service.execute { instance!!.noteDao() }
            }
        }

    }
}