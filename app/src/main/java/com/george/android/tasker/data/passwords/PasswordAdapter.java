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
import java.util.Locale;

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
        String url = password.getUrl();

        if(url.contains("//")) {
            int index = url.indexOf("//");
            url = url.substring(index + 2);
        }

        if(url.contains("www")) {
            int index = url.indexOf("www");
            url = url.substring(index + 4);
        }

        String charLogo = Character.toString(url.charAt(0));
        charLogo = charLogo.toUpperCase(Locale.ROOT);
        holder.passwordLogoTextView.setText(charLogo);
        holder.textViewUrl.setText(url);
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
        TextView passwordLogoTextView;
        CircleImageView circlePassword;
        ImageView imageViewCopyPassword;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUrl = itemView.findViewById(R.id.view_url_title);
            textViewEmail = itemView.findViewById(R.id.view_email);
            circlePassword = itemView.findViewById(R.id.password_circle);
            passwordLogoTextView = itemView.findViewById(R.id.password_logo_text_view);
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
