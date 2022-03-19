package com.george.android.tasker.ui.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.george.android.tasker.databinding.ActivityAddEditNoteBinding;

public class AddEditNoteActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "com.george.android.tasker.ui.notes.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.george.android.tasker.ui.notes.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.george.android.tasker.ui.notes.EXTRA_DESCRIPTION";

    ActivityAddEditNoteBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddEditNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.addEditNoteToolbar.setNavigationOnClickListener(v -> saveNote());
    }

    private void saveNote() {
        String title = binding.editTextNoteTitle.getText().toString();
        String description = binding.editTextNoteDescription.getText().toString();

        if(title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(AddEditNoteActivity.this, "ERROR!!!", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }
}