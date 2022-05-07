package com.george.android.tasker.ui.tasks;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.george.android.tasker.data.tasks.room.Task;
import com.george.android.tasker.databinding.AddTaskBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

public class AddTaskBottomSheet extends BottomSheetDialogFragment {

    AddTaskBottomSheetBinding binding;
    TasksViewModel tasksViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = AddTaskBottomSheetBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        Objects.requireNonNull(getDialog()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        showSoftKeyboard(binding.textTaskInput);

        tasksViewModel = new ViewModelProvider(this).get(TasksViewModel.class);

        binding.addTaskBtn.setOnClickListener(v -> saveTask());

        Objects.requireNonNull(binding.textTaskInput.getEditText()).setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                saveTask();
                return true;
            }
            return false;
        });

        binding.textTaskInput.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.textTaskInput.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    public void saveTask() {
        String taskText = Objects.requireNonNull(binding.textTaskInput.getEditText()).getText().toString();
        if (!taskText.isEmpty()) {
            Task task = new Task(taskText, false);
            tasksViewModel.insert(task);
            dismiss();
        } else {
            binding.textTaskInput.setError("Пустая задача не добавляется");
        }
    }

    public void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
