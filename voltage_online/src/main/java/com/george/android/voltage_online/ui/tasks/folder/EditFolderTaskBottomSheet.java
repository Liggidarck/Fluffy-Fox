package com.george.android.voltage_online.ui.tasks.folder;

import static java.util.Objects.requireNonNull;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.george.android.voltage_online.R;
import com.george.android.voltage_online.databinding.EditFolderTaskBottomSheetBinding;
import com.george.android.voltage_online.model.Folder;
import com.george.android.voltage_online.utils.KeyboardUtils;
import com.george.android.voltage_online.viewmodel.FolderViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class EditFolderTaskBottomSheet extends BottomSheetDialogFragment {

    private EditFolderTaskBottomSheetBinding binding;
    private FolderViewModel folderViewModel;
    private int folderId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = EditFolderTaskBottomSheetBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        KeyboardUtils utils = new KeyboardUtils();

        requireNonNull(getDialog()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        utils.showSoftKeyboard(binding.nameFolderTextLayout, EditFolderTaskBottomSheet.this.requireActivity());

        folderViewModel = new ViewModelProvider(this).get(FolderViewModel.class);

        assert getArguments() != null;
        folderId = getArguments().getInt("folderId");
        String name = getArguments().getString("name");

        requireNonNull(binding.nameFolderTextLayout.getEditText()).setText(name);

        binding.deleteFolderTaskBtn.setOnClickListener(this::deleteFolder);
        binding.saveFolderTaskBtn.setOnClickListener(this::updateFolder);

        return root;
    }


    private void deleteFolder(View v) {
        new AlertDialog.Builder(EditFolderTaskBottomSheet.this.requireActivity())
                .setTitle(R.string.warning_text)
                .setMessage(R.string.conform_delete_folder_tasks)
                .setPositiveButton("Да", (dialog, which) -> {
                    folderViewModel.deleteFolderAndTasks(folderId);
                    dialog.dismiss();
                    dismiss();
                })
                .setNegativeButton("Отмена", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void updateFolder(View v) {
        String name = requireNonNull(binding.nameFolderTextLayout.getEditText()).getText().toString();
        Folder folder = new Folder(name);
        folderViewModel.updateFolder(folderId, folder);
        dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
