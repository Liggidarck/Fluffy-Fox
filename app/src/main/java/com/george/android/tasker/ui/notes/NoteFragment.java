package com.george.android.tasker.ui.notes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.george.android.tasker.MainActivity;
import com.george.android.tasker.data.notes.NoteAdapter;
import com.george.android.tasker.data.notes.room.Note;
import com.george.android.tasker.databinding.FragmentHomeBinding;

public class NoteFragment extends Fragment {
    private FragmentHomeBinding binding;
    private NoteViewModel noteViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        NoteAdapter adapter = new NoteAdapter();

        binding.buttonAddNote.setOnClickListener(v -> {
            Intent intent = new Intent(NoteFragment.this.getContext(), AddEditNoteActivity.class);
            addNoteResultLauncher.launch(intent);
        });

        binding.recyclerViewNotes.setLayoutManager(new LinearLayoutManager(NoteFragment.this.getActivity()));
        binding.recyclerViewNotes.setHasFixedSize(true);
        binding.recyclerViewNotes.setAdapter(adapter);

        noteViewModel.getAllNotes().observe(NoteFragment.this.requireActivity(), adapter::setNotes);

        return root;
    }

    ActivityResultLauncher<Intent> addNoteResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == Activity.RESULT_OK ) {
                    Intent intent = result.getData();
                    assert intent != null;
                    String title = intent.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
                    String description = intent.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);

                    Note note = new Note(title, description);
                    noteViewModel.insert(note);
                }
            }
    );

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}