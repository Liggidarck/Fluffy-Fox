package com.george.android.tasker.ui.tasks.task;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.george.android.tasker.data.tasks.task.Task;
import com.george.android.tasker.databinding.AddTaskBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class AddTaskBottomSheet extends BottomSheetDialogFragment {

    AddTaskBottomSheetBinding binding;
    TasksViewModel tasksViewModel;

    public static final String TAG = "AddTaskBottomSheet";
    int folderID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = AddTaskBottomSheetBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        Objects.requireNonNull(getDialog()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        showSoftKeyboard(binding.textTaskInput);

        assert this.getArguments() != null;
        folderID = this.getArguments().getInt("folderID");
        Log.d(TAG, "onCreateView: folderID: " + folderID);

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

    void saveTask() {
        String taskText = Objects.requireNonNull(binding.textTaskInput.getEditText()).getText().toString();

        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String dateCreate = dateFormat.format(currentDate);

        Log.d(TAG, "saveTask: " + dateCreate);

        if (!taskText.isEmpty()) {
            Task task = new Task(taskText, false, null, dateCreate, null, folderID);
            tasksViewModel.insert(task);
            dismiss();
        } else {
            binding.textTaskInput.setError("Пустая задача не добавляется");
        }
    }

    void showSoftKeyboard(View view) {
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
