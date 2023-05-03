package com.george.android.voltage_online.ui.notes;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.george.android.voltage_online.R;
import com.george.android.voltage_online.databinding.ActivityAddEditNoteBinding;
import com.george.android.voltage_online.model.Note;
import com.george.android.voltage_online.utils.KeyboardUtils;
import com.george.android.voltage_online.viewmodel.NoteViewModel;

import java.util.ArrayList;
import java.util.List;

public class AddEditNoteActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "com.george.android.tasker.ui.notes.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.george.android.tasker.ui.notes.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.george.android.tasker.ui.notes.EXTRA_DESCRIPTION";
    public static final String EXTRA_ADAPTER_POSITION = "com.george.android.tasker.ui.notes.EXTRA_ADAPTER_POSITION";
    public static final String EXTRA_POSITION = "com.george.android.tasker.ui.notes.EXTRA_POSITION";

    ActivityAddEditNoteBinding binding;

    NoteViewModel noteViewModel;

    int adapterPosition = -1;
    int noteId;
    int position;
    String title, description;
    List<Note> noteList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Tasker);
        super.onCreate(savedInstanceState);
        binding = ActivityAddEditNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        KeyboardUtils utils = new KeyboardUtils();

        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        binding.addEditNoteToolbar.setTitle("");
        setSupportActionBar(binding.addEditNoteToolbar);
        binding.addEditNoteToolbar.setNavigationOnClickListener(v -> saveNote());

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            noteId = intent.getIntExtra(EXTRA_ID, -1);
            title = intent.getStringExtra(EXTRA_TITLE);
            description = intent.getStringExtra(EXTRA_DESCRIPTION);
            position = intent.getIntExtra(EXTRA_POSITION, -1);
            binding.editTextNoteTitle.setText(title);
            binding.editTextNoteDescription.setText(description);
            adapterPosition = intent.getIntExtra(EXTRA_ADAPTER_POSITION, -1);
        } else {
            utils.showSoftKeyboard(binding.editTextNoteDescription, AddEditNoteActivity.this);
        }


        noteViewModel.getAllNotes().observe(this, notes -> noteList = notes);
    }

    private void saveNote() {
        String title = binding.editTextNoteTitle.getText().toString();
        String description = binding.editTextNoteDescription.getText().toString();

        if (title.trim().isEmpty() & description.trim().isEmpty()) {
            finish();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_POSITION, position);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        int position = getIntent().getIntExtra(EXTRA_POSITION, -1);

        if (position == -1) {
            data.putExtra(EXTRA_POSITION, noteList.size());
        }

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

                    AlertDialog.Builder builder = new AlertDialog.Builder(AddEditNoteActivity.this);
                    builder.setTitle("Внимание!")
                            .setMessage("Вы уверены что хотите удалить задачу?")
                            .setPositiveButton("ок", (dialog, id) -> {
//                                        binViewModel.insert(new BinNote(title, description));
//                                        noteViewModel.delete(noteId);
                                        Toast.makeText(AddEditNoteActivity.this, "Заметка удалена", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                            )
                            .setNegativeButton("Отмена", (dialog, id) -> dialog.dismiss());
                    builder.create().show();

                } else {
                    Toast.makeText(AddEditNoteActivity.this, "Note can't deleted", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.share_note_item:
                if (title == null & description == null) {
                    Toast.makeText(this, "Empty note can't shared", Toast.LENGTH_SHORT).show();
                } else {
                    String sharingData = title + "\n" + description;

                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, sharingData);
                    sendIntent.setType("text/plain");

                    Intent shareIntent = Intent.createChooser(sendIntent, null);
                    startActivity(shareIntent);
                }
                return true;
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