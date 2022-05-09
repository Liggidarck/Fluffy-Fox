package com.george.android.tasker.data.tasks;

import android.annotation.SuppressLint;
import android.graphics.Paint;
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
import java.util.Collections;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private List<Task> tasks = new ArrayList<>();
    private onItemClickListener listener;
    private onItemClickListener stateListener;

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

        if(task.isStatus()) {
            holder.taskTextView.setPaintFlags(holder.taskTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.taskTextView.setPaintFlags(0);
        }
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    public void removeTask(int position) {
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

            statusCheckBox.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if(stateListener != null && position != RecyclerView.NO_POSITION) {
                    stateListener.onItemClick(tasks.get(position), position);
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

    public void setOnClickSateListener(onItemClickListener listener) {
        this.stateListener = listener;
    }

}
