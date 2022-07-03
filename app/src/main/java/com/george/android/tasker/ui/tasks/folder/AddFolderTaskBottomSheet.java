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
import com.george.android.tasker.databinding.AddFolderTaskBottomSheetBinding;
import com.george.android.tasker.utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

public class AddFolderTaskBottomSheet extends BottomSheetDialogFragment {

    AddFolderTaskBottomSheetBinding binding;
    TasksFolderViewModel tasksFolderViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = AddFolderTaskBottomSheetBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        Utils utils = new Utils();

        Objects.requireNonNull(getDialog()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        utils.showSoftKeyboard(binding.textTaskFolderInput, AddFolderTaskBottomSheet.this.requireActivity());

        tasksFolderViewModel = new ViewModelProvider(this).get(TasksFolderViewModel.class);

        binding.addFolderTask.setOnClickListener(v -> saveFolder());


        return view;
    }

    void saveFolder() {
        String nameFolder = Objects.requireNonNull(binding.textTaskFolderInput.getEditText()).getText().toString();

        if(!nameFolder.isEmpty()) {
            TaskFolder taskFolder = new TaskFolder(nameFolder);
            tasksFolderViewModel.insert(taskFolder);
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
