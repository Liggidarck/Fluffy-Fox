package com.george.android.tasker.ui.tasks;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.george.android.tasker.R;
import com.george.android.tasker.data.tasks.TaskAdapter;
import com.george.android.tasker.databinding.ActivityAddEditTaskBinding;

import java.util.Objects;

public class AddEditTaskActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "com.george.android.tasker.ui.tasks.EXTRA_ID";
    public static final String EXTRA_TEXT = "com.george.android.tasker.ui.tasks.EXTRA_TEXT";
    public static final String EXTRA_STATUS = "com.george.android.tasker.ui.tasks.EXTRA_STATUS";
    public static final String EXTRA_ADAPTER_POSITION = "com.george.android.tasker.ui.tasks.EXTRA_ADAPTER_POSITION";

    TasksViewModel tasksViewModel;
    TaskAdapter taskAdapter = new TaskAdapter();

    ActivityAddEditTaskBinding binding;
    int adapterPosition;
    boolean status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddEditTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        tasksViewModel = new ViewModelProvider(this).get(TasksViewModel.class);
        tasksViewModel.getAllTasks().observe(AddEditTaskActivity.this, tasks -> taskAdapter.setTasks(tasks));

        setSupportActionBar(binding.toolbarEditTask);
        binding.toolbarEditTask.setNavigationOnClickListener(v -> saveTask());

        Intent intent = getIntent();
        if(intent.hasExtra(EXTRA_ID)) {
            String textTask = intent.getStringExtra(EXTRA_TEXT);
            status = intent.getBooleanExtra(EXTRA_STATUS, false);
            adapterPosition = intent.getIntExtra(EXTRA_ADAPTER_POSITION, -1);

            if (status) {
                Objects.requireNonNull(binding.textEditTaskInput.
                        getEditText())
                        .setPaintFlags(binding.textEditTaskInput.getEditText().getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }

            Objects.requireNonNull(binding.textEditTaskInput.getEditText()).setText(textTask);
            binding.taskStateCheckBox.setChecked(status);
        }

        binding.taskStateCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                Objects.requireNonNull(binding.textEditTaskInput.
                        getEditText())
                        .setPaintFlags(binding.textEditTaskInput.getEditText().getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                Objects.requireNonNull(binding.textEditTaskInput.
                        getEditText())
                        .setPaintFlags(0);
            }
        });

    }

    void saveTask() {
        String textTask = Objects.requireNonNull(binding.textEditTaskInput.getEditText()).getText().toString();
        Intent intent = new Intent();
        intent.putExtra(EXTRA_TEXT, textTask);
        intent.putExtra(EXTRA_STATUS, binding.taskStateCheckBox.isChecked());

        if (textTask.trim().isEmpty()) {
            finish();
            return;
        }

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            intent.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.delete_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_item:
                if (adapterPosition != -1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddEditTaskActivity.this);
                    builder.setTitle("Внимание!")
                            .setMessage("Вы уверены что хотите удалить задачу?")
                            .setPositiveButton("ок", (dialog, id) -> {
                                        tasksViewModel.delete(taskAdapter.getTaskAt(adapterPosition));
                                        Toast.makeText(AddEditTaskActivity.this, "Задача удалена", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                            )
                            .setNegativeButton("Отмена", (dialog, id) -> dialog.dismiss());
                    builder.create().show();

                } else {
                    Toast.makeText(this, "Такую задачу удалить невозможно", Toast.LENGTH_SHORT).show();
                }


            default:
                return super.onOptionsItemSelected(item);
        }
    }
}