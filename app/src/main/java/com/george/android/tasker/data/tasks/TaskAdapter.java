package com.george.android.tasker.data.tasks;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.george.android.tasker.R;
import com.george.android.tasker.data.tasks.room.Task;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private List<Task> tasks = new ArrayList<>();
    private onItemClickListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.taskTextView.setText(task.getTitle());
        holder.statusCheckBox.setChecked(task.isStatus());
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    public Task getTaskAt(int position) {
        return tasks.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCheckBox statusCheckBox;
        TextView taskTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            statusCheckBox = itemView.findViewById(R.id.task_status_check_box);
            taskTextView = itemView.findViewById(R.id.task_text_view);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if(listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(tasks.get(position), position);
                }
            });

        }
    }

    public interface onItemClickListener {
        void onItemClick(Task task, int position);
    }

    public void setOnClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

}
