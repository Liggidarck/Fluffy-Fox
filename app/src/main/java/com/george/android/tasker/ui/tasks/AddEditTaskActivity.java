package com.george.android.tasker.ui.tasks;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.george.android.tasker.databinding.ActivityAddEditTaskBinding;

public class AddEditTaskActivity extends AppCompatActivity {

    ActivityAddEditTaskBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddEditTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}