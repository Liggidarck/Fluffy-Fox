package com.george.android.tasker.ui.tasks;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.george.android.tasker.R;
import com.george.android.tasker.data.tasks.TaskAdapter;
import com.george.android.tasker.databinding.ActivityAddEditTaskBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditTaskActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "com.george.android.tasker.ui.tasks.EXTRA_ID";
    public static final String EXTRA_TEXT = "com.george.android.tasker.ui.tasks.EXTRA_TEXT";
    public static final String EXTRA_STATUS = "com.george.android.tasker.ui.tasks.EXTRA_STATUS";
    public static final String EXTRA_DATE_COMPLETE = "com.george.android.tasker.ui.tasks.EXTRA_DATE_COMPLETE";
    public static final String EXTRA_DATE_CREATE = "com.george.android.tasker.ui.tasks.EXTRA_DATE_CREATE";
    public static final String EXTRA_NOTE_TASK = "com.george.android.tasker.ui.tasks.EXTRA_NOTE_TASK";
    public static final String EXTRA_ADAPTER_POSITION = "com.george.android.tasker.ui.tasks.EXTRA_ADAPTER_POSITION";

    TasksViewModel tasksViewModel;
    TaskAdapter taskAdapter = new TaskAdapter();

    ActivityAddEditTaskBinding binding;
    Calendar datePickCalendar;

    String dateCreate;

    int adapterPosition;
    boolean status;

    public static final String TAG = "AddEditTaskActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddEditTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        tasksViewModel = new ViewModelProvider(this).get(TasksViewModel.class);
        tasksViewModel.getAllTasks().observe(EditTaskActivity.this, tasks -> taskAdapter.setTasks(tasks));

        setSupportActionBar(binding.toolbarEditTask);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            String textTask = intent.getStringExtra(EXTRA_TEXT);
            status = intent.getBooleanExtra(EXTRA_STATUS, false);
            adapterPosition = intent.getIntExtra(EXTRA_ADAPTER_POSITION, -1);
            String dateComplete = intent.getStringExtra(EXTRA_DATE_COMPLETE);
            dateCreate = intent.getStringExtra(EXTRA_DATE_CREATE);
            String noteTask = intent.getStringExtra(EXTRA_NOTE_TASK);

            Log.d(TAG, "onCreate: " + dateCreate);

            if (status) {
                binding.textEditTaskInput.setPaintFlags(binding.textEditTaskInput.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                binding.taskCompleteBtn.setText("Задача не выполнена");
                binding.taskCompleteBtn.setOnClickListener(v -> {
                    saveTask(false);
                    finish();
                });
            } else {
                binding.taskCompleteBtn.setText("Задача выполнена");
                binding.taskCompleteBtn.setOnClickListener(v -> {
                    saveTask(true);
                    finish();
                });
            }

            binding.textEditTaskInput.setText(textTask);
            binding.dateCreateTextView.setText("Создано: " + dateCreate);
            if (dateComplete != null)
                binding.calendarTaskText.setText(dateComplete);

            if (noteTask != null)
                binding.informationTaskEdit.setText(noteTask);

        }

        binding.toolbarEditTask.setNavigationOnClickListener(v -> saveTask(status));

        datePickCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = (view, year, month, dayOfMonth) -> {
            datePickCalendar.set(Calendar.YEAR, year);
            datePickCalendar.set(Calendar.MONTH, month);
            datePickCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setDate();
        };

        binding.calendarTaskView.setOnClickListener(v ->
                new DatePickerDialog(EditTaskActivity.this, date, datePickCalendar
                        .get(Calendar.YEAR), datePickCalendar.get(Calendar.MONTH), datePickCalendar.get(Calendar.DAY_OF_MONTH)).show());
    }

    void setDate() {
        String date_text = "dd.MM.yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(date_text, Locale.US);
        binding.calendarTaskText.setText("Дата выполнения: " + sdf.format(datePickCalendar.getTime()));
    }

    void saveTask(boolean status) {
        String textTask = binding.textEditTaskInput.getText().toString();
        Intent intent = new Intent();
        intent.putExtra(EXTRA_TEXT, textTask);
        intent.putExtra(EXTRA_STATUS, status);
        intent.putExtra(EXTRA_DATE_COMPLETE, binding.calendarTaskText.getText().toString());
        intent.putExtra(EXTRA_DATE_CREATE, dateCreate);
        intent.putExtra(EXTRA_NOTE_TASK, getNoteTask());

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

    String getNoteTask() {
        return binding.informationTaskEdit.getText().toString();
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditTaskActivity.this);
                    builder.setTitle("Внимание!")
                            .setMessage("Вы уверены что хотите удалить задачу?")
                            .setPositiveButton("ок", (dialog, id) -> {
                                        tasksViewModel.delete(taskAdapter.getTaskAt(adapterPosition));
                                        Toast.makeText(EditTaskActivity.this, "Задача удалена", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        saveTask(status);
        super.onBackPressed();
    }
}