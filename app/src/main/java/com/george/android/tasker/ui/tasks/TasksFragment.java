package com.george.android.tasker.ui.tasks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
                tasks -> taskAdapter.setTasks(tasks));

        binding.buttonAddTask.setOnClickListener(v -> {
            AddTaskBottomSheet addTaskBottomSheet = new AddTaskBottomSheet();
            addTaskBottomSheet.show(getParentFragmentManager(), "AddTaskBottomSheet");
        });

        taskAdapter.setOnClickListener((task, position) -> {
            Intent intent = new Intent(TasksFragment.this.requireActivity(), AddEditTaskActivity.class);
            intent.putExtra(AddEditTaskActivity.EXTRA_ID, task.getId());
            intent.putExtra(AddEditTaskActivity.EXTRA_TEXT, task.getTitle());
            intent.putExtra(AddEditTaskActivity.EXTRA_STATUS, task.isStatus());
            intent.putExtra(AddEditTaskActivity.EXTRA_ADAPTER_POSITION, position);
            editTaskResultLauncher.launch(intent);
        });

        taskAdapter.setOnClickSateListener((task, position) -> {
            boolean currentState = task.isStatus();

            Task updateStatus;
            if (currentState) {
                updateStatus = new Task(task.getTitle(), false);
            } else {
                updateStatus = new Task(task.getTitle(), true);
            }
            updateStatus.setId(task.getId());
            tasksViewModel.update(updateStatus);
        });

        return root;
    }

    ActivityResultLauncher<Intent> editTaskResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == Activity.RESULT_OK) {
                    Intent intent = result.getData();

                    assert intent != null;
                    int id = intent.getIntExtra(AddEditTaskActivity.EXTRA_ID, -1);
                    if (id == -1) {
                        Toast.makeText(TasksFragment.this.requireActivity(), "Task can't update", Toast.LENGTH_SHORT).show();
                       return;
                    }

                    String textTask = intent.getStringExtra(AddEditTaskActivity.EXTRA_TEXT);
                    boolean statusTask = intent.getBooleanExtra(AddEditTaskActivity.EXTRA_STATUS, false);

                    Task task = new Task(textTask, statusTask);
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