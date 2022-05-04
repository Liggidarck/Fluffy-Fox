package com.george.android.tasker.ui.tasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.george.android.tasker.data.tasks.TaskAdapter;
import com.george.android.tasker.data.tasks.room.Task;
import com.george.android.tasker.databinding.FragmentTasksBinding;

import java.util.List;

public class TasksFragment extends Fragment {

    private FragmentTasksBinding binding;
    TasksViewModel tasksViewModel;

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



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}