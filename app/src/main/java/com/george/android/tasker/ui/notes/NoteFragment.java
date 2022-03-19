package com.george.android.tasker.ui.notes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
        NoteAdapter noteAdapter = new NoteAdapter();

        binding.buttonAddNote.setOnClickListener(v -> {
            Intent intent = new Intent(NoteFragment.this.getContext(), AddEditNoteActivity.class);
            addNoteResultLauncher.launch(intent);
        });

        binding.recyclerViewNotes.setLayoutManager(new LinearLayoutManager(NoteFragment.this.getActivity()));
        binding.recyclerViewNotes.setHasFixedSize(true);
        binding.recyclerViewNotes.setAdapter(noteAdapter);

        noteViewModel.getAllNotes().observe(NoteFragment.this.requireActivity(), noteAdapter::setNotes);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(noteAdapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(NoteFragment.this.getActivity(), "Note deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(binding.recyclerViewNotes);

        noteAdapter.setOnClickItemListener(note -> {
            Intent intent = new Intent(NoteFragment.this.getActivity(), AddEditNoteActivity.class);
            intent.putExtra(AddEditNoteActivity.EXTRA_ID, note.getId());
            intent.putExtra(AddEditNoteActivity.EXTRA_TITLE, note.getTitle());
            intent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION, note.getDescription());
            intent.putExtra(AddEditNoteActivity.EXTRA_DATE_CREATE, note.getDateCreate());
            editNoteResultLauncher.launch(intent);
        });

        return root;
    }

    ActivityResultLauncher<Intent> addNoteResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intent = result.getData();
                    assert intent != null;
                    String title = intent.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
                    String description = intent.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
                    String dateCreate = intent.getStringExtra(AddEditNoteActivity.EXTRA_DATE_CREATE);

                    Note note = new Note(title, description, dateCreate);
                    noteViewModel.insert(note);
                }
            }
    );

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
                            Toast.makeText(NoteFragment.this.getActivity(), "Note can't be updated", Toast.LENGTH_SHORT).show();
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}