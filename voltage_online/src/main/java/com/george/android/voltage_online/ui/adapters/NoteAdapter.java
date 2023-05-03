package com.george.android.voltage_online.ui.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.george.android.voltage_online.R;
import com.george.android.voltage_online.model.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Note> notes = new ArrayList<>();
    private onItemClickListener listener;

    public static final String TAG = NoteAdapter.class.getSimpleName();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return new NoteTitleHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.note_title_item, parent, false));
            case 2:
                return new NoteNotTitleHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.note_not_title_item, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Note currentNote = notes.get(position);
        switch (holder.getItemViewType()) {
            case 0:
                NoteTitleHolder noteTitleHolder = (NoteTitleHolder) holder;
                noteTitleHolder.textViewTitle.setText(currentNote.getTitle());
                noteTitleHolder.textViewDescription.setText(currentNote.getDescription());
                break;

            case 2:
                NoteNotTitleHolder noteNotTitleHolder = (NoteNotTitleHolder) holder;
                noteNotTitleHolder.textViewDescriptionNoTitle.setText(currentNote.getDescription());
                break;
        }
    }


    @Override
    public int getItemCount() {
        return notes.size();
    }

    @Override
    public int getItemViewType(int position) {
        Note currentNote = notes.get(position);
        if(currentNote.getTitle().equals(""))
            return 2;
        return 0;
    }

    class NoteTitleHolder extends RecyclerView.ViewHolder {
        private final TextView textViewTitle;
        private final TextView textViewDescription;

        public NoteTitleHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(notes.get(position), position);
                }
            });

        }
    }

    class NoteNotTitleHolder extends RecyclerView.ViewHolder {
        private final TextView textViewDescriptionNoTitle;

        public NoteNotTitleHolder(@NonNull View itemView) {
            super(itemView);
            textViewDescriptionNoTitle = itemView.findViewById(R.id.textViewDescriptionNoTitle);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(notes.get(position), position);
                }
            });
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    public interface onItemClickListener {
        void onItemClick(Note note, int position);
    }

    public void setOnClickItemListener(onItemClickListener listener) {
        this.listener = listener;
    }
}
