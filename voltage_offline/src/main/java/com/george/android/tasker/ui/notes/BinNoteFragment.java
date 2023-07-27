package com.george.android.tasker.ui.notes;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.george.android.tasker.R;
import com.george.android.tasker.data.model.Note;
import com.george.android.tasker.data.model.BinNote;
import com.george.android.tasker.ui.adapters.BinNoteAdapter;
import com.george.android.tasker.databinding.FragmentBinNoteBinding;
import com.george.android.tasker.data.viewmodel.NoteViewModel;

public class BinNoteFragment extends Fragment {

    public static final String TAG = "BinNoteFragment";
    FragmentBinNoteBinding binding;
    BinNoteAdapter binNoteAdapter = new BinNoteAdapter();

    NoteViewModel noteViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBinNoteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        binding.binNoteToolbar.inflateMenu(R.menu.note_bin_menu);
        binding.binNoteToolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());
        binding.binNoteToolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.clear_bin_note_item) {
                Log.d(TAG, "onCreateView: clear bin");
                AlertDialog.Builder builder = new AlertDialog.Builder(BinNoteFragment.this.requireActivity());
                builder.setTitle("Внимание!")
                        .setMessage("Вы уверены что хотите очистить корзину?")
                        .setPositiveButton("ок", (dialog, id) -> {
                            dialog.cancel();
                        })
                        .setNegativeButton("Отмена", (dialog, id) -> dialog.dismiss());
                builder.create().show();
                return true;
            }
            return false;
        });

        binding.recyclerViewBinNote.setLayoutManager(new LinearLayoutManager(BinNoteFragment.this.requireActivity()));
        binding.recyclerViewBinNote.setHasFixedSize(true);
        binding.recyclerViewBinNote.setAdapter(binNoteAdapter);


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                BinNote binNote = binNoteAdapter.getBinNoteAt(viewHolder.getAdapterPosition());
                String title = binNote.getTitle();
                String description = binNote.getDescription();

                Note note = new Note(title, description);
                noteViewModel.insert(note);
            }
        }).attachToRecyclerView(binding.recyclerViewBinNote);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
