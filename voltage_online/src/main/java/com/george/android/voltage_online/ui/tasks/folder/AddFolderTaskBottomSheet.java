package com.george.android.voltage_online.ui.tasks.folder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;


import com.george.android.voltage_online.viewmodel.TasksFolderViewModel;
import com.george.android.voltage_online.databinding.AddFolderTaskBottomSheetBinding;
import com.george.android.voltage_online.model.TaskFolder;
import com.george.android.voltage_online.utils.KeyboardUtils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;
import java.util.Objects;

public class AddFolderTaskBottomSheet extends BottomSheetDialogFragment {

    AddFolderTaskBottomSheetBinding binding;
    TasksFolderViewModel tasksFolderViewModel;
    List<TaskFolder> taskFolders;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = AddFolderTaskBottomSheetBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        KeyboardUtils utils = new KeyboardUtils();

        Objects.requireNonNull(getDialog()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        utils.showSoftKeyboard(binding.textTaskFolderInput, AddFolderTaskBottomSheet.this.requireActivity());

        tasksFolderViewModel = new ViewModelProvider(this).get(TasksFolderViewModel.class);

        binding.addFolderTask.setOnClickListener(v -> saveFolder());

//        tasksFolderViewModel.getAllFolders().observe(this, folders -> taskFolders = folders);


        return view;
    }

    void saveFolder() {
        String nameFolder = Objects.requireNonNull(binding.textTaskFolderInput.getEditText()).getText().toString();

        if (!nameFolder.isEmpty()) {
            TaskFolder taskFolder = new TaskFolder(nameFolder);
//            tasksFolderViewModel.insert(taskFolder);
            dismiss();
        } else {
            binding.textTaskFolderInput.setError("Папка без названия");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
