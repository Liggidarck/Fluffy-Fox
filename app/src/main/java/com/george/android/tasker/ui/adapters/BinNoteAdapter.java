package com.george.android.tasker.ui.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.george.android.tasker.R;
import com.george.android.tasker.data.model.BinNote;

import java.util.ArrayList;
import java.util.List;

public class BinNoteAdapter extends RecyclerView.Adapter<BinNoteAdapter.BinNoteHolder> {

    private List<BinNote> binNotes = new ArrayList<>();
    private onItemClickListener listener;

    @NonNull
    @Override
    public BinNoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_title_item, parent, false);
        return new BinNoteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BinNoteHolder holder, int position) {
        BinNote binNote = binNotes.get(position);
        holder.textViewTitle.setText(binNote.getTitle());
        holder.textViewDescription.setText(binNote.getDescription());
    }

    @Override
    public int getItemCount() {
        return binNotes.size();
    }

    public BinNote getBinNoteAt(int position) {
        return binNotes.get(position);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setBinNotes(List<BinNote> binNotes) {
        this.binNotes = binNotes;
        notifyDataSetChanged();
    }

    class BinNoteHolder extends RecyclerView.ViewHolder {
        private final TextView textViewTitle;
        private final TextView textViewDescription;

        public BinNoteHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(binNotes.get(position), position);
                }
            });
        }
    }

    public interface onItemClickListener {
        void onItemClick(BinNote note, int position);
    }

    public void setOnClickItemListener(onItemClickListener listener) {
        this.listener = listener;
    }

}
