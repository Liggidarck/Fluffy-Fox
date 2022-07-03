package com.george.android.tasker.ui.tasks.folder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.george.android.tasker.data.tasks.folder.TaskFolder;
import com.george.android.tasker.data.tasks.folder.TaskFolderAdapter;
import com.george.android.tasker.databinding.EditFolderTaskBottomSheetBinding;
import com.george.android.tasker.ui.tasks.task.TasksViewModel;
import com.george.android.tasker.utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

public class EditFolderTaskBottomSheet extends BottomSheetDialogFragment {

    EditFolderTaskBottomSheetBinding binding;
    TasksFolderViewModel tasksFolderViewModel;
    TasksViewModel tasksViewModel;

    TaskFolderAdapter taskFolderAdapter = new TaskFolderAdapter();

    int folderId, position;
    String name;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = EditFolderTaskBottomSheetBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Utils utils = new Utils();

        Objects.requireNonNull(getDialog()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        utils.showSoftKeyboard(binding.nameFolderTextLayout, EditFolderTaskBottomSheet.this.requireActivity());

        tasksFolderViewModel = new ViewModelProvider(this).get(TasksFolderViewModel.class);
        tasksViewModel = new ViewModelProvider(this).get(TasksViewModel.class);

        tasksFolderViewModel.getAllFolders().observe(EditFolderTaskBottomSheet.this.requireActivity(),
                taskFolders -> taskFolderAdapter.setTaskFolders(taskFolders));

        assert getArguments() != null;
        folderId = getArguments().getInt("folderId");
        position = getArguments().getInt("position");
        name = getArguments().getString("name");

        Objects.requireNonNull(binding.nameFolderTextLayout.getEditText()).setText(name);

        binding.deleteFolderTaskBtn.setOnClickListener(v -> {
            //Удаляем папку
            TaskFolder taskFolder = taskFolderAdapter.getTaskFolderAt(position);
            tasksFolderViewModel.delete(taskFolder);

            //Удаляем все элементы из папки
            tasksViewModel.deleteTasksFolder(folderId);

            dismiss();
        });

        binding.saveFolderTaskBtn.setOnClickListener(v -> {
            String name = binding.nameFolderTextLayout.getEditText().getText().toString();
            TaskFolder taskFolder = new TaskFolder(name);
            taskFolder.setFolderId(folderId);
            tasksFolderViewModel.update(taskFolder);
            dismiss();
        });

        return root;
    }


}
