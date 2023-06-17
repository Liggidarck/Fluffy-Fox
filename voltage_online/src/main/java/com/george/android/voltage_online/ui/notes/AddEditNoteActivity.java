package com.george.android.voltage_online.ui.notes;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.george.android.voltage_online.ui.MainActivity;
import com.george.android.voltage_online.utils.KeyboardUtils;
import com.george.android.voltage_online.viewmodel.NoteViewModel;

public class AddEditNoteActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "com.george.android.voltage_online.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.george.android.voltage_online.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.george.android.voltage_online.EXTRA_DESCRIPTION";

    public static final String TAG = AddEditNoteActivity.class.getSimpleName();

    private ActivityAddEditNoteBinding binding;

    private NoteViewModel noteViewModel;

    private int adapterPosition = -1;
    private long noteId;
    private String title, description;

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

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            noteId = intent.getLongExtra(EXTRA_ID, -1);
            title = intent.getStringExtra(EXTRA_TITLE);
            description = intent.getStringExtra(EXTRA_DESCRIPTION);

            binding.editTextNoteTitle.setText(title);
            binding.editTextNoteDescription.setText(description);

            binding.addEditNoteToolbar.setNavigationOnClickListener(v -> updateNote());

            return;
        }

        utils.showSoftKeyboard(binding.editTextNoteDescription, AddEditNoteActivity.this);
        binding.addEditNoteToolbar.setNavigationOnClickListener(v -> saveNote());


    }

    private void updateNote() {
        String title = binding.editTextNoteTitle.getText().toString();
        String description = binding.editTextNoteDescription.getText().toString();
        Note updateNote = new Note(title, description);

        noteViewModel.updateNote(noteId, updateNote).observe(this, message -> {
            Log.d(TAG, "updateNote: " + message.getMessage());
            startActivity(new Intent(this, MainActivity.class));
        });

    }

    private void saveNote() {
        String title = binding.editTextNoteTitle.getText().toString();
        String description = binding.editTextNoteDescription.getText().toString();

        if (title.trim().isEmpty() & description.trim().isEmpty()) {
            onBackPressed();
            return;
        }

        noteViewModel.createNote(new Note(title, description)).observe(this, note -> {
            startActivity(new Intent(this, MainActivity.class));
        });
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
                AlertDialog.Builder builder = new AlertDialog.Builder(AddEditNoteActivity.this);
                builder.setTitle("Внимание!")
                        .setMessage("Вы уверены что хотите удалить заметку?")
                        .setPositiveButton("ок", (dialog, id) -> {
                                    noteViewModel.deleteNote(noteId).observe(this, message -> {
                                        Toast.makeText(AddEditNoteActivity.this, "Заметка удалена", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(this, MainActivity.class));
                                    });
                                }
                        )
                        .setNegativeButton("Отмена", (dialog, id) -> dialog.dismiss());
                builder.create().show();

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