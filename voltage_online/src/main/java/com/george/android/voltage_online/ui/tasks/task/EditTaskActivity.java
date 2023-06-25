package com.george.android.voltage_online.ui.tasks.task;

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

import com.george.android.voltage_online.R;
import com.george.android.voltage_online.databinding.ActivityAddEditTaskBinding;
import com.george.android.voltage_online.model.Task;
import com.george.android.voltage_online.viewmodel.TasksViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditTaskActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "EXTRA_ID";
    public static final String EXTRA_TEXT = "EXTRA_TEXT";
    public static final String EXTRA_STATUS = "EXTRA_STATUS";
    public static final String EXTRA_DATE_COMPLETE = "EXTRA_DATE_COMPLETE";
    public static final String EXTRA_DATE_CREATE = "EXTRA_DATE_CREATE";
    public static final String EXTRA_NOTE_TASK = "EXTRA_NOTE_TASK";
    public static final String EXTRA_ADAPTER_POSITION = "EXTRA_ADAPTER_POSITION";
    public static final String EXTRA_FOLDER_ID = "EXTRA_FOLDER_ID";
    private TasksViewModel tasksViewModel;
    private ActivityAddEditTaskBinding binding;
    private Calendar datePickCalendar;
    private String dateCreate;
    private int folderId, taskId;
    private boolean status;
    public static final String TAG = EditTaskActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Tasker);
        super.onCreate(savedInstanceState);
        binding = ActivityAddEditTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        tasksViewModel = new ViewModelProvider(this).get(TasksViewModel.class);

        setSupportActionBar(binding.toolbarEditTask);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            taskId = intent.getIntExtra(EXTRA_ID, -1);
            String textTask = intent.getStringExtra(EXTRA_TEXT);
            status = intent.getBooleanExtra(EXTRA_STATUS, false);
            String dateComplete = intent.getStringExtra(EXTRA_DATE_COMPLETE);
            dateCreate = intent.getStringExtra(EXTRA_DATE_CREATE);
            String noteTask = intent.getStringExtra(EXTRA_NOTE_TASK);
            folderId = intent.getIntExtra(EXTRA_FOLDER_ID, -1);

            Log.d(TAG, "onCreate: " + dateCreate);
            Log.d(TAG, "onCreate: folderID: " + folderId);

            if (status) {
                binding.textEditTaskInput.setPaintFlags(binding.textEditTaskInput.getPaintFlags()
                        | Paint.STRIKE_THRU_TEXT_FLAG);

                binding.taskCompleteBtn.setText("Задача не выполнена");
                binding.taskCompleteBtn.setOnClickListener(v -> updateTask(false));

            } else {
                binding.taskCompleteBtn.setText("Задача выполнена");
                binding.taskCompleteBtn.setOnClickListener(v -> updateTask(true));
            }

            binding.textEditTaskInput.setText(textTask);
            String textCreate = "Создано: " + dateCreate;
            binding.dateCreateTextView.setText(textCreate);
            if (dateComplete != null)
                binding.calendarTaskText.setText(dateComplete);

            if (noteTask != null)
                binding.informationTaskEdit.setText(noteTask);

        }

        binding.toolbarEditTask.setNavigationOnClickListener(v -> updateTask(status));

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

    private void setDate() {
        String date_text = "dd.MM.yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(date_text, Locale.US);
        String textDateComplete = "Дата выполнения: " + sdf.format(datePickCalendar.getTime());
        binding.calendarTaskText.setText(textDateComplete);
    }

    private void updateTask(boolean status) {
        String textTask = binding.textEditTaskInput.getText().toString();

        Task task = new Task(textTask, status, binding.calendarTaskText.getText().toString(),
                dateCreate, getNoteTask(), folderId);

        tasksViewModel.updateTask(taskId, task).observe(this, message -> onBackPressed());
    }

    private String getNoteTask() {
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
        if (item.getItemId() == R.id.delete_item) {
            AlertDialog.Builder builder = new AlertDialog.Builder(EditTaskActivity.this);
            builder.setTitle("Внимание!")
                    .setMessage("Вы уверены что хотите удалить задачу?")
                    .setPositiveButton("ок", (dialog, id) -> {
//                                    tasksViewModel.delete(taskId);
                                Toast.makeText(EditTaskActivity.this, "Задача удалена", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                    )
                    .setNegativeButton("Отмена", (dialog, id) -> dialog.dismiss());
            builder.create().show();
        } else {
            Toast.makeText(this, "Такую задачу удалить невозможно", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        updateTask(status);
        super.onBackPressed();
    }
}