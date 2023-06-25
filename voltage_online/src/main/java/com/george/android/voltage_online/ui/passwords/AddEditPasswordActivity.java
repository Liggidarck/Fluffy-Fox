package com.george.android.voltage_online.ui.passwords;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.george.android.voltage_online.R;
import com.george.android.voltage_online.databinding.ActivityAddEditPasswordBinding;
import com.george.android.voltage_online.model.Password;
import com.george.android.voltage_online.viewmodel.PasswordsViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class AddEditPasswordActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "EXTRA_ID";
    public static final String EXTRA_URL = "EXTRA_URL";
    public static final String EXTRA_EMAIL = "EXTRA_EMAIL";
    public static final String EXTRA_PASSWORD = "EXTRA_PASSWORD";
    private ActivityAddEditPasswordBinding binding;
    private PasswordsViewModel passwordsViewModel;
    private long passwordId;
    public static final String TAG = AddEditPasswordActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Tasker);
        super.onCreate(savedInstanceState);
        binding = ActivityAddEditPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String defaultEmail = sharedPreferences.getString("user_email", "Не указано");

        passwordsViewModel = new ViewModelProvider(this).get(PasswordsViewModel.class);

        setSupportActionBar(binding.toolbarAddEditPasswords);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            passwordId = intent.getLongExtra(EXTRA_ID, -1);
            String url = intent.getStringExtra(EXTRA_URL);
            String email = intent.getStringExtra(EXTRA_EMAIL);
            String password = intent.getStringExtra(EXTRA_PASSWORD);

            Log.d(TAG, "onCreate: passwordId: " + passwordId);

            Objects.requireNonNull(binding.textInputUrl.getEditText()).setText(url);
            Objects.requireNonNull(binding.textInputLogin.getEditText()).setText(email);
            Objects.requireNonNull(binding.textInputPassword.getEditText()).setText(password);
            binding.toolbarAddEditPasswords.setNavigationOnClickListener(v -> updatePassword());
        } else {
            binding.toolbarAddEditPasswords.setNavigationOnClickListener(v -> savePassword());
            if (!defaultEmail.equals("Не указано"))
                Objects.requireNonNull(binding.textInputLogin.getEditText()).setText(defaultEmail);
        }

        binding.textInputUrl.setEndIconOnClickListener(v -> {
            if (validateWeb()) {
                String url = Objects.requireNonNull(binding.textInputUrl.getEditText()).getText().toString();
                Log.d(TAG, "onCreate: " + url);
                if (!url.contains("https")) {
                    url = "https://" + url;
                }

                v.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            } else {
                Snackbar.make(v, "Ошибка! Пустой адрес", Snackbar.LENGTH_LONG).show();
            }
        });

        binding.textInputLogin.setEndIconOnClickListener(v -> {
            String login = Objects.requireNonNull(binding.textInputLogin.getEditText()).getText().toString();
            if (!login.trim().isEmpty()) {
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

    private void updatePassword() {
        String url = Objects.requireNonNull(binding.textInputUrl.getEditText()).getText().toString();
        String email = Objects.requireNonNull(binding.textInputLogin.getEditText()).getText().toString();
        String textPassword = Objects.requireNonNull(binding.textInputPassword.getEditText()).getText().toString();

        if (url.trim().isEmpty() | email.trim().isEmpty() | textPassword.trim().isEmpty()) {
            finish();
            return;
        }

        Password password = new Password(url, email, textPassword);
        passwordsViewModel.updatePassword(passwordId, password).observe(this, message -> onBackPressed());
    }

    private void savePassword() {
        String url = Objects.requireNonNull(binding.textInputUrl.getEditText()).getText().toString();
        String email = Objects.requireNonNull(binding.textInputLogin.getEditText()).getText().toString();
        String textPassword = Objects.requireNonNull(binding.textInputPassword.getEditText()).getText().toString();

        Log.d(TAG, "savePassword: url:" + url);

        if (url.trim().isEmpty() | email.trim().isEmpty() | textPassword.trim().isEmpty()) {
            finish();
            return;
        }

        Password password = new Password(url, email, textPassword);
        passwordsViewModel.createPassword(password).observe(this, _password -> onBackPressed());
    }

    public boolean validateWeb() {
        String check = Objects.requireNonNull(binding.textInputUrl.getEditText()).getText().toString().trim();
        return !check.isEmpty();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.delete_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete_item) {
            AlertDialog.Builder builder = new AlertDialog.Builder(AddEditPasswordActivity.this);
            builder.setTitle("Внимание!")
                    .setMessage("Вы уверены что хотите удалить пароль?")
                    .setPositiveButton("ок", (dialog, id) ->
                            passwordsViewModel.deletePassword(passwordId).observe(this, message -> {
                                Toast.makeText(AddEditPasswordActivity.this, "Пароль удален", Toast.LENGTH_SHORT).show();
                                finish();
                                onBackPressed();
                            }))
                    .setNegativeButton("Отмена", (dialog, id) -> dialog.dismiss());
            builder.create().show();
        }

        return super.onOptionsItemSelected(item);
    }


}