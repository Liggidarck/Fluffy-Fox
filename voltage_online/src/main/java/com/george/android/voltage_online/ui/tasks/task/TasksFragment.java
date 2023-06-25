package com.george.android.voltage_online.ui.tasks.task;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.george.android.voltage_online.databinding.FragmentTasksBinding;
import com.george.android.voltage_online.model.Task;
import com.george.android.voltage_online.ui.adapters.TaskAdapter;
import com.george.android.voltage_online.viewmodel.TasksViewModel;

public class TasksFragment extends Fragment {

    FragmentTasksBinding binding;
    TasksViewModel tasksViewModel;

    TaskAdapter taskAdapter = new TaskAdapter();

    int folderId;

    public static final String TAG = TasksFragment.class.getSimpleName();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTasksBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        assert getArguments() != null;
        folderId = getArguments().getInt("folderId");
        String folderName = getArguments().getString("folderName");

        binding.toolbarTasks.setTitle(folderName);
        binding.toolbarTasks.setNavigationOnClickListener(v -> requireActivity().onBackPressed());

        tasksViewModel = new ViewModelProvider(this).get(TasksViewModel.class);
        initRecyclerView();

        tasksViewModel.getTasksByFolderId(folderId).observe(this.requireActivity(), tasks -> taskAdapter.setTasks(tasks));

        binding.buttonAddTask.setOnClickListener(v -> {
            AddTaskBottomSheet addTaskBottomSheet = new AddTaskBottomSheet();

            Bundle bundle = new Bundle();
            bundle.putInt("folderID", folderId);
            addTaskBottomSheet.setArguments(bundle);

            addTaskBottomSheet.show(getParentFragmentManager(), "AddTaskBottomSheet");
        });

        taskAdapter.setOnClickListener((task, position) -> {
            Intent intent = new Intent(this.requireActivity(), EditTaskActivity.class);
            intent.putExtra(EditTaskActivity.EXTRA_ID, task.getId());
            intent.putExtra(EditTaskActivity.EXTRA_TEXT, task.getTitle());
            intent.putExtra(EditTaskActivity.EXTRA_STATUS, task.isStatus());
            intent.putExtra(EditTaskActivity.EXTRA_ADAPTER_POSITION, position);
            intent.putExtra(EditTaskActivity.EXTRA_DATE_COMPLETE, task.getDateComplete());
            intent.putExtra(EditTaskActivity.EXTRA_DATE_CREATE, task.getDateCreate());
            intent.putExtra(EditTaskActivity.EXTRA_NOTE_TASK, task.getNoteTask());
            intent.putExtra(EditTaskActivity.EXTRA_FOLDER_ID, task.getFolderId());
            startActivity(intent);
        });

        taskAdapter.setOnClickSateListener((task, position) -> {
            boolean currentState = task.isStatus();
            String dateComplete = task.getDateComplete();
            String dateCreate = task.getDateCreate();
            String noteTask = task.getNoteTask();

            Task updateStatus;
            if (currentState) {
                updateStatus = new Task(task.getTitle(), false, dateComplete,
                        dateCreate, noteTask, folderId);
            } else {
                updateStatus = new Task(task.getTitle(), true, dateComplete,
                        dateCreate, noteTask, folderId);
            }
            updateStatus.setId(task.getId());

            tasksViewModel.updateTask(task.getId(), updateStatus).observe(this.requireActivity(), message -> {
                Log.d(TAG, "onCreateView: " + message.getMessage());
                tasksViewModel.getTasksByFolderId(folderId).observe(this.requireActivity(), tasks -> taskAdapter.setTasks(tasks));
            });

        });

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        tasksViewModel.getTasksByFolderId(folderId).observe(this.requireActivity(), tasks -> taskAdapter.setTasks(tasks));
    }

    private void initRecyclerView() {
        binding.recyclerTasks.setLayoutManager(new LinearLayoutManager(TasksFragment.this.requireActivity()));
        binding.recyclerTasks.setHasFixedSize(true);
        binding.recyclerTasks.setAdapter(taskAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}