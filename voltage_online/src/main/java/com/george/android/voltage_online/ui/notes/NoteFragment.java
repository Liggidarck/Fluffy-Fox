package com.george.android.voltage_online.ui.notes;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.george.android.voltage_online.R;
import com.george.android.voltage_online.databinding.FragmentNoteBinding;
import com.george.android.voltage_online.model.Note;
import com.george.android.voltage_online.ui.adapters.NoteAdapter;
import com.george.android.voltage_online.viewmodel.NoteViewModel;

import java.util.ArrayList;
import java.util.List;

public class NoteFragment extends Fragment {

    public static final String TAG = "NoteFragment";
    private FragmentNoteBinding binding;

    private NoteViewModel noteViewModel;

    List<Note> noteList = new ArrayList<>();
    NoteAdapter noteAdapter = new NoteAdapter();

    @SuppressLint("NonConstantResourceId")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNoteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.toolbarNotes.inflateMenu(R.menu.note_menu);

        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        binding.recyclerViewNotes.setLayoutManager(new LinearLayoutManager(NoteFragment.this.getActivity()));
        binding.recyclerViewNotes.setHasFixedSize(true);
        binding.recyclerViewNotes.setAdapter(noteAdapter);

        binding.buttonAddNote.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), AddEditNoteActivity.class);
            startActivity(intent);
        });

        getNotes();

        noteAdapter.setOnClickItemListener((note, position) -> {
            Intent intent = new Intent(NoteFragment.this.getActivity(), AddEditNoteActivity.class);
            Log.d(TAG, "onCreateView: noteID: " + note.getId());
            intent.putExtra(AddEditNoteActivity.EXTRA_ID, note.getId());
            intent.putExtra(AddEditNoteActivity.EXTRA_TITLE, note.getTitle());
            intent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION, note.getDescription());
            startActivity(intent);
            requireActivity().finish();
        });

        binding.toolbarNotes.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.delete_all_note_item:
                    Log.d(TAG, "onCreateView: delete all notes");
                    AlertDialog.Builder builder = new AlertDialog.Builder(NoteFragment.this.requireActivity());
                    builder.setTitle("Внимание!")
                            .setMessage("Вы уверены что хотите удалить все заметки?")
                            .setPositiveButton("ок", (dialog, id) -> {
                                dialog.cancel();
//                                noteViewModel.deleteAllNote();
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

    private void getNotes() {
        noteViewModel.getAllNotes().observe(requireActivity(), listNotes -> {
            noteAdapter.setNotes(listNotes);
            noteList = listNotes;

            try {
                if (listNotes.size() == 0) {
                    binding.emptyView.setVisibility(View.VISIBLE);
                } else {
                    binding.emptyView.setVisibility(View.INVISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}