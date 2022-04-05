package com.george.android.tasker.data.passwords;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.george.android.tasker.R;
import com.george.android.tasker.data.passwords.room.Password;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PasswordAdapter extends RecyclerView.Adapter<PasswordAdapter.ViewHolder> {

    private List<Password> passwords = new ArrayList<>();
    private onItemClickListener listener;
    private onItemClickListener copyListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.password_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Password password = passwords.get(position);
        holder.textViewUrl.setText(password.getUrl());
        holder.textViewEmail.setText(password.getEmail());
    }

    @Override
    public int getItemCount() {
        return passwords.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setPasswords(List<Password> passwords) {
        this.passwords = passwords;
        notifyDataSetChanged();
    }

    public Password getPasswordAt(int position) {
        return passwords.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewUrl;
        TextView textViewEmail;
        CircleImageView imageLogoPassword;
        ImageView imageViewCopyPassword;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUrl = itemView.findViewById(R.id.view_url_title);
            textViewEmail = itemView.findViewById(R.id.view_email);
            imageLogoPassword = itemView.findViewById(R.id.view_logo_password);
            imageViewCopyPassword = itemView.findViewById(R.id.view_copy);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(passwords.get(position), position);
                }
            });

            imageViewCopyPassword.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if(copyListener != null && position != RecyclerView.NO_POSITION) {
                    copyListener.onItemClick(passwords.get(position), position);
                }
            });

        }
    }

    public interface onItemClickListener {
        void onItemClick(Password password, int position);
    }

    public void setOnClickItemListener(onItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnCopyClickListener(onItemClickListener listener) {
        this.copyListener = listener;
    }

}
