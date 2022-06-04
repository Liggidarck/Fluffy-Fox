package com.george.android.tasker.ui.tasks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.george.android.tasker.data.tasks.TaskAdapter;
import com.george.android.tasker.data.tasks.room.Task;
import com.george.android.tasker.databinding.FragmentSearchTaskBinding;

import java.util.Objects;

public class SearchTaskFragment extends Fragment {

    FragmentSearchTaskBinding binding;
    TasksViewModel tasksViewModel;
    TaskAdapter adapter = new TaskAdapter();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchTaskBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        tasksViewModel = new ViewModelProvider(this).get(TasksViewModel.class);

        binding.searchTaskToolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());

        binding.recyclerSearchTask.setLayoutManager(new LinearLayoutManager(SearchTaskFragment.this.requireActivity()));
        binding.recyclerSearchTask.setHasFixedSize(true);
        binding.recyclerSearchTask.setAdapter(adapter);

        tasksViewModel.getAllTasks().observe(SearchTaskFragment.this.requireActivity(),
                tasks -> adapter.setTasks(tasks));

        adapter.setOnClickListener((task, position) -> {
            Intent intent = new Intent(SearchTaskFragment.this.requireActivity(), EditTaskActivity.class);
            intent.putExtra(EditTaskActivity.EXTRA_ID, task.getId());
            intent.putExtra(EditTaskActivity.EXTRA_TEXT, task.getTitle());
            intent.putExtra(EditTaskActivity.EXTRA_STATUS, task.isStatus());
            intent.putExtra(EditTaskActivity.EXTRA_ADAPTER_POSITION, position);
            intent.putExtra(EditTaskActivity.EXTRA_DATE_COMPLETE, task.getDateComplete());
            intent.putExtra(EditTaskActivity.EXTRA_DATE_CREATE, task.getDateCreate());
            intent.putExtra(EditTaskActivity.EXTRA_NOTE_TASK, task.getNoteTask());
            editTaskResultLauncher.launch(intent);
        });

        Objects.requireNonNull(binding.textInputTaskSearch.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tasksViewModel.findTask(s.toString()).observe(SearchTaskFragment.this.requireActivity(),
                        tasks -> adapter.setTasks(tasks));
            }
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
                        Toast.makeText(SearchTaskFragment.this.requireActivity(), "Task can't update", Toast.LENGTH_SHORT).show();
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
