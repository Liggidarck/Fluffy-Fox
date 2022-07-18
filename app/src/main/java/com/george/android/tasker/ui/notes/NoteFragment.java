package com.george.android.tasker.ui.notes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.george.android.tasker.R;
import com.george.android.tasker.data.model.Note;
import com.george.android.tasker.ui.adapters.NoteAdapter;
import com.george.android.tasker.data.model.BinNote;
import com.george.android.tasker.databinding.FragmentNoteBinding;
import com.george.android.tasker.data.viewmodel.NoteBinViewModel;
import com.george.android.tasker.data.viewmodel.NoteViewModel;
import com.google.android.material.snackbar.Snackbar;

public class NoteFragment extends Fragment {

    public static final String TAG = "NoteFragment";
    private FragmentNoteBinding binding;

    private NoteViewModel noteViewModel;
    private NoteBinViewModel binViewModel;

    NoteAdapter noteAdapter = new NoteAdapter();

    @SuppressLint("NonConstantResourceId")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNoteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.toolbarNotes.inflateMenu(R.menu.note_menu);

        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        binViewModel = new ViewModelProvider(this).get(NoteBinViewModel.class);

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
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder
                    viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Note note = noteAdapter.getNoteAt(viewHolder.getAdapterPosition());
                String title = note.getTitle();
                String description = note.getDescription();
                BinNote binNote = new BinNote(title, description);

                binViewModel.insert(binNote);
                noteViewModel.delete(noteAdapter.getNoteAt(viewHolder.getAdapterPosition()));

                Snackbar.make(binding.fragmentNoteCoordinator,
                                "Заметка " + title + " удалена", Snackbar.LENGTH_SHORT)
                        .show();
            }
        }).attachToRecyclerView(binding.recyclerViewNotes);

        noteAdapter.setOnClickItemListener((note, position) -> {
            Intent intent = new Intent(NoteFragment.this.getActivity(), AddEditNoteActivity.class);
            intent.putExtra(AddEditNoteActivity.EXTRA_ID, note.getId());
            intent.putExtra(AddEditNoteActivity.EXTRA_TITLE, note.getTitle());
            intent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION, note.getDescription());
            intent.putExtra(AddEditNoteActivity.EXTRA_ADAPTER_POSITION, position);
            editNoteResultLauncher.launch(intent);
        });

        noteAdapter.setOnLongClickItemListener((note, position) -> {
            Log.d(TAG, "note: " + note.getDescription());
        });

        binding.toolbarNotes.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.search_note_item:
                    Log.d(TAG, "onCreateView: search");
                    NavController searchController =
                            Navigation.findNavController(NoteFragment.this.requireActivity(),
                                    R.id.nav_host_fragment_activity_main);
                    searchController.navigate(R.id.action_navigation_note_to_navigation_note_search);
                    return true;
                case R.id.delete_all_note_item:
                    Log.d(TAG, "onCreateView: delete all notes");
                    AlertDialog.Builder builder = new AlertDialog.Builder(NoteFragment.this.requireActivity());
                    builder.setTitle("Внимание!")
                            .setMessage("Вы уверены что хотите удалить все заметки?")
                            .setPositiveButton("ок", (dialog, id) -> {
                                dialog.cancel();
                                noteViewModel.deleteAllNote();
                            })
                            .setNegativeButton("Отмена", (dialog, id) -> dialog.dismiss());
                    builder.create().show();
                    return true;
                case R.id.recycle_bin_note:
                    Log.d(TAG, "onCreateView: bin open");
                    NavController BinController =
                            Navigation.findNavController(NoteFragment.this.requireActivity(),
                                    R.id.nav_host_fragment_activity_main);
                    BinController.navigate(R.id.action_navigation_note_to_navigation_note_bin);
                    return true;
                default:
                    return false;
            }

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

                    Note note = new Note(title, description);
                    noteViewModel.insert(note);
                }
            }
    );

    ActivityResultLauncher<Intent> editNoteResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
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

                    Note note = new Note(title, description);
                    note.setId(id);
                    noteViewModel.update(note);
                }
            }
    );

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}