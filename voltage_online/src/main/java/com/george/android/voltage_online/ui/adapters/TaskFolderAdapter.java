package com.george.android.voltage_online.ui.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.george.android.voltage_online.R;
import com.george.android.voltage_online.model.Folder;

import java.util.ArrayList;
import java.util.List;

public class TaskFolderAdapter extends RecyclerView.Adapter<TaskFolderAdapter.ViewHolder> {

    private List<Folder> folders = new ArrayList<>();
    private onItemClickListener listener;
    private onLongClickListener onFolderClickListener;
    public static final String TAG = "TaskFolderAdapter";

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_folder_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Folder folder = folders.get(position);
        holder.taskFolderName.setText(folder.getNameFolder());
    }

    @Override
    public int getItemCount() {
        return folders.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setTaskFolders(List<Folder> folders) {
        this.folders = folders;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView taskFolderName;
        final RelativeLayout folderTaskIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            taskFolderName = itemView.findViewById(R.id.taskFolderName);
            folderTaskIcon = itemView.findViewById(R.id.folderTaskIcon);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if(listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(folders.get(position), position);
                }
            });

            folderTaskIcon.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if(onFolderClickListener != null && position != RecyclerView.NO_POSITION) {
                    onFolderClickListener.onFolderClick(folders.get(position), position);
                }
            });
        }
    }

    public interface onItemClickListener {
        void onItemClick(Folder folder, int position);
    }

    public interface onLongClickListener {
        void onFolderClick(Folder folder, int position);
    }

    public void setOnClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnFolderClickListener(onLongClickListener onFolderClickListener) {
        this.onFolderClickListener = onFolderClickListener;
    }

}
