package com.george.android.tasker.ui.notes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.george.android.tasker.R;
import com.george.android.tasker.data.notes.NoteAdapter;
import com.george.android.tasker.data.notes.room.Note;
import com.george.android.tasker.data.notes.room.NoteDatabase;
import com.george.android.tasker.databinding.FragmentNoteSearchBinding;

import java.util.List;
import java.util.Objects;

public class SearchNoteFragment extends Fragment {

    public static final String TAG = "SearchNoteFragment";

    FragmentNoteSearchBinding binding;
    NoteAdapter noteAdapter = new NoteAdapter();
    private NoteViewModel noteViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNoteSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        binding.recyclerViewSearchNotes.setLayoutManager(new LinearLayoutManager(SearchNoteFragment.this.getActivity()));
        binding.recyclerViewSearchNotes.setHasFixedSize(true);
        binding.recyclerViewSearchNotes.setAdapter(noteAdapter);

        binding.searchNoteToolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());
        binding.searchNoteToolbar.inflateMenu(R.menu.search_note_menu);
        binding.searchNoteToolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.calendar_item:
                    Log.d(TAG, "onCreateView: Calendar click");
                    return true;
                default:
                    return false;
            }
        });

        Objects.requireNonNull(binding.textInputNoteSearch.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d(TAG, "afterTextChanged: " + editable.toString());
                LiveData<List<Note>> notesList = NoteDatabase
                        .getInstance(SearchNoteFragment.this.getActivity())
                        .noteDao()
                        .findNote(editable.toString());
                notesList.observe(SearchNoteFragment.this.requireActivity(), notes -> noteAdapter.setNotes(notes));
            }
        });

        noteAdapter.setOnClickItemListener(note -> {
            Intent intent = new Intent(SearchNoteFragment.this.getActivity(), AddEditNoteActivity.class);
            intent.putExtra(AddEditNoteActivity.EXTRA_ID, note.getId());
            intent.putExtra(AddEditNoteActivity.EXTRA_TITLE, note.getTitle());
            intent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION, note.getDescription());
            intent.putExtra(AddEditNoteActivity.EXTRA_DATE_CREATE, note.getDateCreate());
            editNoteResultLauncher.launch(intent);
        });

        return root;
    }

    ActivityResultLauncher<Intent> editNoteResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        assert intent != null;
                        int id = intent.getIntExtra(AddEditNoteActivity.EXTRA_ID, -1);
                        if (id == -1) {
                            Toast.makeText(SearchNoteFragment.this.getActivity(), "Note can't be updated", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String title = intent.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
                        String description = intent.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
                        String dateCreate = intent.getStringExtra(AddEditNoteActivity.EXTRA_DATE_CREATE);

                        Note note = new Note(title, description, dateCreate);
                        note.setId(id);
                        noteViewModel.update(note);
                    }
                }
            }
    );

}
