package com.george.android.tasker.ui.notes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.ViewModelProvider;

import com.george.android.tasker.R;
import com.george.android.tasker.data.notes.NoteAdapter;
import com.george.android.tasker.databinding.ActivityAddEditNoteBinding;
import com.google.android.material.snackbar.Snackbar;

public class AddEditNoteActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "com.george.android.tasker.ui.notes.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.george.android.tasker.ui.notes.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.george.android.tasker.ui.notes.EXTRA_DESCRIPTION";
    public static final String EXTRA_ADAPTER_POSITION = "com.george.android.tasker.ui.notes.EXTRA_ADAPTER_POSITION";

    ActivityAddEditNoteBinding binding;
    NoteAdapter noteAdapter = new NoteAdapter();
    NoteViewModel noteViewModel;

    int adapterPosition = -1;
    String title, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddEditNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(AddEditNoteActivity.this, noteAdapter::setNotes);

        binding.addEditNoteToolbar.setTitle("");
        setSupportActionBar(binding.addEditNoteToolbar);
        binding.addEditNoteToolbar.setNavigationOnClickListener(v -> saveNote());

        Intent intent = getIntent();
        if(intent.hasExtra(EXTRA_ID)){
            title = intent.getStringExtra(EXTRA_TITLE);
            description = intent.getStringExtra(EXTRA_DESCRIPTION);
            binding.editTextNoteTitle.setText(title);
            binding.editTextNoteDescription.setText(description);
            adapterPosition = intent.getIntExtra(EXTRA_ADAPTER_POSITION, -1);
        } else {
            showSoftKeyboard(binding.editTextNoteDescription);
        }

    }

    public void showSoftKeyboard(View view) {
        if(view.requestFocus()){
            InputMethodManager imm =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view,InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void saveNote() {
        String title = binding.editTextNoteTitle.getText().toString();
        String description = binding.editTextNoteDescription.getText().toString();

        if(title.trim().isEmpty() & description.trim().isEmpty()) {
            finish();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_note_item:
                if (adapterPosition != -1) {
                    Toast.makeText(AddEditNoteActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
                    noteViewModel.delete(noteAdapter.getNoteAt(adapterPosition));
                    finish();
                } else {
                    Toast.makeText(AddEditNoteActivity.this, "Note can't deleted", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.share_note_item:
                if(title == null & description == null) {
                    Toast.makeText(this, "Empty note can't shared", Toast.LENGTH_SHORT).show();
                } else {
                    String sharing_data = title + "\n" + description;

                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, sharing_data);
                    sendIntent.setType("text/plain");

                    Intent shareIntent = Intent.createChooser(sendIntent, null);
                    startActivity(shareIntent);
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        saveNote();
        super.onBackPressed();
    }
}