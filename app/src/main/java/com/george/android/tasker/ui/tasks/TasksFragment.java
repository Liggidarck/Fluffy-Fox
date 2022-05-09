package com.george.android.tasker.ui.tasks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.george.android.tasker.data.tasks.TaskAdapter;
import com.george.android.tasker.data.tasks.room.Task;
import com.george.android.tasker.databinding.FragmentTasksBinding;

public class TasksFragment extends Fragment {

    FragmentTasksBinding binding;
    TasksViewModel tasksViewModel;

    public static final String TAG = "TasksFragment";

    TaskAdapter taskAdapter = new TaskAdapter();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTasksBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        tasksViewModel = new ViewModelProvider(this).get(TasksViewModel.class);

        binding.recyclerTasks.setLayoutManager(new LinearLayoutManager(TasksFragment.this.requireActivity()));
        binding.recyclerTasks.setHasFixedSize(true);
        binding.recyclerTasks.setAdapter(taskAdapter);

        tasksViewModel.getAllTasks().observe(TasksFragment.this.requireActivity(),
                tasks -> {
                    taskAdapter.setTasks(tasks);
                });

        binding.buttonAddTask.setOnClickListener(v -> {
            AddTaskBottomSheet addTaskBottomSheet = new AddTaskBottomSheet();
            addTaskBottomSheet.show(getParentFragmentManager(), "AddTaskBottomSheet");
        });

        taskAdapter.setOnClickListener((task, position) -> {
            Intent intent = new Intent(TasksFragment.this.requireActivity(), EditTaskActivity.class);
            intent.putExtra(EditTaskActivity.EXTRA_ID, task.getId());
            intent.putExtra(EditTaskActivity.EXTRA_TEXT, task.getTitle());
            intent.putExtra(EditTaskActivity.EXTRA_STATUS, task.isStatus());
            intent.putExtra(EditTaskActivity.EXTRA_ADAPTER_POSITION, position);
            intent.putExtra(EditTaskActivity.EXTRA_DATE_COMPLETE, task.getDateComplete());
            intent.putExtra(EditTaskActivity.EXTRA_DATE_CREATE, task.getDateCreate());
            intent.putExtra(EditTaskActivity.EXTRA_NOTE_TASK, task.getNoteTask());
            editTaskResultLauncher.launch(intent);
        });

        taskAdapter.setOnClickSateListener((task, position) -> {
            boolean currentState = task.isStatus();
            String dateComplete = task.getDateComplete();
            String dateCreate = task.getDateCreate();
            String noteTask = task.getNoteTask();

            Task updateStatus;
            if (currentState) {
                updateStatus = new Task(task.getTitle(), false, dateComplete, dateCreate, noteTask);
            } else {
                updateStatus = new Task(task.getTitle(), true, dateComplete, dateCreate, noteTask);
            }
            updateStatus.setId(task.getId());
            tasksViewModel.update(updateStatus);
        });

        return root;
    }

    ActivityResultLauncher<Intent> editTaskResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intent = result.getData();

                    assert intent != null;
                    int id = intent.getIntExtra(EditTaskActivity.EXTRA_ID, -1);
                    if (id == -1) {
                        Toast.makeText(TasksFragment.this.requireActivity(), "Task can't update", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String textTask = intent.getStringExtra(EditTaskActivity.EXTRA_TEXT);
                    boolean statusTask = intent.getBooleanExtra(EditTaskActivity.EXTRA_STATUS, false);
                    String dateComplete = intent.getStringExtra(EditTaskActivity.EXTRA_DATE_COMPLETE);
                    String dateCreate = intent.getStringExtra(EditTaskActivity.EXTRA_DATE_CREATE);
                    String noteTask = intent.getStringExtra(EditTaskActivity.EXTRA_NOTE_TASK);

                    Task task = new Task(textTask, statusTask, dateComplete, dateCreate, noteTask);
                    task.setId(id);
                    tasksViewModel.update(task);
                }
            }
    );

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}