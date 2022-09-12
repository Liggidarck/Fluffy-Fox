package com.george.android.tasker.ui.tasks.task;

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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.george.android.tasker.data.model.Task;
import com.george.android.tasker.data.viewmodel.TasksViewModel;
import com.george.android.tasker.databinding.FragmentTasksBinding;
import com.george.android.tasker.ui.adapters.TaskAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TasksFragment extends Fragment {

    FragmentTasksBinding binding;
    TasksViewModel tasksViewModel;

    TaskAdapter taskAdapter = new TaskAdapter();
    List<Task> tasks = new ArrayList<>();

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

        binding.recyclerTasks.setLayoutManager(new LinearLayoutManager(TasksFragment.this.requireActivity()));
        binding.recyclerTasks.setHasFixedSize(true);
        binding.recyclerTasks.setAdapter(taskAdapter);

        tasksViewModel.getFoldersTasks(folderId).observe(TasksFragment.this.requireActivity(), tasks -> {
            this.tasks = tasks;
            taskAdapter.setTasks(tasks);
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(binding.recyclerTasks);

        binding.buttonAddTask.setOnClickListener(v -> {
            AddTaskBottomSheet addTaskBottomSheet = new AddTaskBottomSheet();

            Bundle bundle = new Bundle();
            bundle.putInt("folderID", folderId);
            addTaskBottomSheet.setArguments(bundle);

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
            intent.putExtra(EditTaskActivity.EXTRA_FOLDER_ID, task.getFolderId());
            intent.putExtra(EditTaskActivity.EXTRA_POSITION, task.getPosition());
            editTaskResultLauncher.launch(intent);
        });

        taskAdapter.setOnClickSateListener((task, position) -> {
            boolean currentState = task.isStatus();
            String dateComplete = task.getDateComplete();
            String dateCreate = task.getDateCreate();
            String noteTask = task.getNoteTask();

            Task updateStatus;
            if (currentState) {
                updateStatus = new Task(task.getTitle(), false, dateComplete,
                        dateCreate, noteTask, folderId, task.getPosition());
            } else {
                updateStatus = new Task(task.getTitle(), true, dateComplete,
                        dateCreate, noteTask, folderId, task.getPosition());
            }
            updateStatus.setId(task.getId());
            tasksViewModel.update(updateStatus);
        });

        return root;
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                              @NonNull RecyclerView.ViewHolder target) {

            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();

            if (fromPosition < toPosition) {
                for (int position = fromPosition; position < toPosition; position++) {
                    Collections.swap(tasks, position, position + 1);

                    int order1 = tasks.get(position).getPosition();
                    int order2 = tasks.get(position + 1).getPosition();
                    tasks.get(position).setPosition(order2);
                    tasks.get(position + 1).setPosition(order1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(tasks, i, i - 1);

                    int order1 = tasks.get(i).getPosition();
                    int order2 = tasks.get(i - 1).getPosition();
                    tasks.get(i).setPosition(order2);
                    tasks.get(i - 1).setPosition(order1);
                }
            }
            taskAdapter.notifyItemMoved(fromPosition, toPosition);
            return true;
        }

        @Override
        public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            Log.d(TAG, "clearView: start update");
            tasksViewModel.updatePosition(tasks);
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };

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
                    int folderId = intent.getIntExtra(EditTaskActivity.EXTRA_FOLDER_ID, -1);
                    int position = intent.getIntExtra(EditTaskActivity.EXTRA_POSITION, -1);

                    Task task = new Task(textTask, statusTask, dateComplete, dateCreate, noteTask, folderId, position);
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