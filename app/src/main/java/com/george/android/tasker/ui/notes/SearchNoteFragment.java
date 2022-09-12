package com.george.android.tasker.ui.notes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.george.android.tasker.data.model.Note;
import com.george.android.tasker.ui.adapters.NoteAdapter;
import com.george.android.tasker.databinding.FragmentNoteSearchBinding;
import com.george.android.tasker.data.viewmodel.NoteViewModel;

import java.util.Objects;

public class SearchNoteFragment extends Fragment {

    FragmentNoteSearchBinding binding;
    NoteAdapter noteAdapter = new NoteAdapter();
    NoteViewModel noteViewModel;

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

        noteViewModel.getAllNotes().observe(SearchNoteFragment.this.requireActivity(),
                notes -> noteAdapter.setNotes(notes));

        Objects.requireNonNull(binding.textInputNoteSearch.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                noteViewModel.findNote(s.toString()).observe(SearchNoteFragment.this.requireActivity(),
                        notes -> noteAdapter.setNotes(notes));
            }
        });

        noteAdapter.setOnClickItemListener((note, position) -> {
            Intent intent = new Intent(SearchNoteFragment.this.getActivity(), AddEditNoteActivity.class);
            intent.putExtra(AddEditNoteActivity.EXTRA_ID, note.getId());
            intent.putExtra(AddEditNoteActivity.EXTRA_TITLE, note.getTitle());
            intent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION, note.getDescription());
            intent.putExtra(AddEditNoteActivity.EXTRA_ADAPTER_POSITION, position);
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

                        Note note = new Note(title, description, 0);
                        note.setId(id);
                        noteViewModel.update(note);
                    }
                }
            }
    );

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
