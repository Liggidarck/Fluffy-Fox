package com.george.android.tasker.data.tasks.folder;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.george.android.tasker.R;

import java.util.ArrayList;
import java.util.List;

public class TaskFolderAdapter extends RecyclerView.Adapter<TaskFolderAdapter.ViewHolder> {

    private List<TaskFolder> taskFolders = new ArrayList<>();
    private onItemClickListener listener;
    private onLongClickListener onLongClickListener;
    public static final String TAG = "TaskFolderAdapter";

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_folder_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TaskFolder taskFolder = taskFolders.get(position);
        holder.taskFolderName.setText(taskFolder.getNameFolder());
    }

    @Override
    public int getItemCount() {
        return taskFolders.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setTaskFolders(List<TaskFolder> taskFolders) {
        this.taskFolders = taskFolders;
        notifyDataSetChanged();
    }

    public TaskFolder getTaskFolderAt(int position) {
        return taskFolders.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView taskFolderName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            taskFolderName = itemView.findViewById(R.id.taskFolderName);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if(listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(taskFolders.get(position), position);
                }
            });

            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if(onLongClickListener != null && position != RecyclerView.NO_POSITION) {
                    onLongClickListener.onItemLongClick(taskFolders.get(position), position);
                }
                return true;
            });
        }
    }

    public interface onItemClickListener {
        void onItemClick(TaskFolder taskFolder, int position);
    }

    public interface onLongClickListener {
        void onItemLongClick(TaskFolder taskFolder, int position);
    }

    public void setOnClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnLongClickListener(onLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

}
