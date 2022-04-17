package com.george.android.tasker.ui.passwords;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.george.android.tasker.R;
import com.george.android.tasker.data.passwords.PasswordAdapter;
import com.george.android.tasker.databinding.ActivityAddEditPasswordBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class AddEditPasswordActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "com.george.android.tasker.ui.passwords.EXTRA_ID";
    public static final String EXTRA_URL = "com.george.android.tasker.ui.passwords.EXTRA_URL";
    public static final String EXTRA_EMAIL = "com.george.android.tasker.ui.passwords.EXTRA_EMAIL";
    public static final String EXTRA_PASSWORD = "com.george.android.tasker.ui.passwords.EXTRA_PASSWORD";
    public static final String EXTRA_ADAPTER_POSITION = "com.george.android.tasker.ui.passwords.EXTRA_ADAPTER_POSITION";

    ActivityAddEditPasswordBinding binding;
    PasswordAdapter passwordAdapter = new PasswordAdapter();

    PasswordsViewModel passwordsViewModel;

    String url, email, password;
    int adapterPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddEditPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String defaultEmail = sharedPreferences.getString("user_email", "Не указано");

        passwordsViewModel = new ViewModelProvider(this).get(PasswordsViewModel.class);
        passwordsViewModel.getAllPasswords().observe(AddEditPasswordActivity.this, passwords -> passwordAdapter.setPasswords(passwords));

        setSupportActionBar(binding.toolbarAddEditPasswords);
        binding.toolbarAddEditPasswords.setNavigationOnClickListener(v -> savePassword());

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            url = intent.getStringExtra(EXTRA_URL);
            email = intent.getStringExtra(EXTRA_EMAIL);
            password = intent.getStringExtra(EXTRA_PASSWORD);
            adapterPosition = intent.getIntExtra(EXTRA_ADAPTER_POSITION, -1);

            Objects.requireNonNull(binding.textInputUrl.getEditText()).setText(url);
            Objects.requireNonNull(binding.textInputLogin.getEditText()).setText(email);
            Objects.requireNonNull(binding.textInputPassword.getEditText()).setText(password);
        } else {
            if (!defaultEmail.equals("Не указано"))
                Objects.requireNonNull(binding.textInputLogin.getEditText()).setText(defaultEmail);
        }

        binding.textInputUrl.setEndIconOnClickListener(v -> {
            if (validateWeb()) {
                String url = Objects.requireNonNull(binding.textInputUrl.getEditText()).getText().toString();
                v.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            } else {
                Snackbar.make(v, "Ошибка! Пустой адрес", Snackbar.LENGTH_LONG).show();
            }
        });

        binding.textInputLogin.setEndIconOnClickListener(v -> {
            String login = Objects.requireNonNull(binding.textInputLogin.getEditText()).getText().toString();
            if(!login.trim().isEmpty()) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", login);
                assert clipboard != null;
                clipboard.setPrimaryClip(clip);
                Snackbar.make(v, "Логин скопирован", Snackbar.LENGTH_LONG).show();
            } else {
                Snackbar.make(v, "Пустой логин не копируется", Snackbar.LENGTH_LONG).show();
            }
        });

    }

    private void savePassword() {
        String url = Objects.requireNonNull(binding.textInputUrl.getEditText()).getText().toString();
        String email = Objects.requireNonNull(binding.textInputLogin.getEditText()).getText().toString();
        String password = Objects.requireNonNull(binding.textInputPassword.getEditText()).getText().toString();

        if (url.trim().isEmpty() | email.trim().isEmpty() | password.trim().isEmpty()) {
            finish();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_URL, url);
        data.putExtra(EXTRA_EMAIL, email);
        data.putExtra(EXTRA_PASSWORD, password);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    public boolean validateWeb() {
        String check = Objects.requireNonNull(binding.textInputUrl.getEditText()).getText().toString().trim();
        return !check.isEmpty();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_password_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_password_item:
                if (adapterPosition != -1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddEditPasswordActivity.this);
                    builder.setTitle("Внимание!")
                            .setMessage("Вы уверены что хотите удалить пароль?")
                            .setPositiveButton("ок", (dialog, id) -> passwordsViewModel.delete(passwordAdapter.getPasswordAt(adapterPosition)))
                            .setNegativeButton("Отмена", (dialog, id) -> dialog.dismiss());
                    builder.create().show();

                    Toast.makeText(AddEditPasswordActivity.this, "Пароль удален", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Такой пароль удалить невозможно", Toast.LENGTH_SHORT).show();
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}