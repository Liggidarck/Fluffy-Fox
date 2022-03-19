package com.george.android.tasker.ui.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.george.android.tasker.MainActivity;
import com.george.android.tasker.R;
import com.george.android.tasker.data.notes.NoteRepository;
import com.george.android.tasker.databinding.ActivityAddEditNoteBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddEditNoteActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "com.george.android.tasker.ui.notes.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.george.android.tasker.ui.notes.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.george.android.tasker.ui.notes.EXTRA_DESCRIPTION";
    public static final String EXTRA_DATE_CREATE = "com.george.android.tasker.ui.notes.EXTRA_DATE_CREATE";

    ActivityAddEditNoteBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddEditNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.addEditNoteToolbar);
        binding.addEditNoteToolbar.setNavigationOnClickListener(v -> saveNote());

        Intent intent = getIntent();
        if(intent.hasExtra(EXTRA_ID)){
            binding.editTextNoteTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            binding.editTextNoteDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            binding.textViewNoteDateCreate.setText("Дата создания: " + intent.getStringExtra(EXTRA_DATE_CREATE));
        } else {
            binding.textViewNoteDateCreate.setText("Дата создания: " + getDate());
        }
    }

    private void saveNote() {
        String title = binding.editTextNoteTitle.getText().toString();
        String description = binding.editTextNoteDescription.getText().toString();
        String dateCreate = getDate();

        if(title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(AddEditNoteActivity.this, "ERROR!!!", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_DATE_CREATE, dateCreate);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    String getDate() {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        return df.format(date);
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
                Toast.makeText(AddEditNoteActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();

                return true;
            case R.id.share_note_item:
                Toast.makeText(AddEditNoteActivity.this, "Note shared", Toast.LENGTH_SHORT).show();

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}