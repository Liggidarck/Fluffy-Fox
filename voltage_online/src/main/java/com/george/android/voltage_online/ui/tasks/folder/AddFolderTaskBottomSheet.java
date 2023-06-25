package com.george.android.voltage_online.ui.tasks.folder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.george.android.voltage_online.databinding.AddFolderTaskBottomSheetBinding;
import com.george.android.voltage_online.model.Folder;
import com.george.android.voltage_online.utils.KeyboardUtils;
import com.george.android.voltage_online.viewmodel.FolderViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

public class AddFolderTaskBottomSheet extends BottomSheetDialogFragment {

    private AddFolderTaskBottomSheetBinding binding;
    private FolderViewModel folderViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = AddFolderTaskBottomSheetBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        KeyboardUtils utils = new KeyboardUtils();

        Objects.requireNonNull(getDialog()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        utils.showSoftKeyboard(binding.textTaskFolderInput, AddFolderTaskBottomSheet.this.requireActivity());

        folderViewModel = new ViewModelProvider(this).get(FolderViewModel.class);

        binding.addFolderTask.setOnClickListener(v -> saveFolder());

        return view;
    }

    private void saveFolder() {
        String nameFolder = Objects.requireNonNull(binding.textTaskFolderInput.getEditText()).getText().toString();

        if (!nameFolder.isEmpty()) {
            Folder folder = new Folder(nameFolder);
            folderViewModel.createFolder(folder);
            dismiss();
            requireActivity().recreate();
            return;
        }

        binding.textTaskFolderInput.setError("Папка без названия");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
