package com.george.android.tasker.ui.passwords;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.george.android.tasker.R;
import com.george.android.tasker.SettingsActivity;
import com.george.android.tasker.data.passwords.PasswordAdapter;
import com.george.android.tasker.data.passwords.room.Password;
import com.george.android.tasker.databinding.FragmentPasswordsBinding;
import com.george.android.tasker.ui.tasks.TasksFragment;
import com.google.android.material.snackbar.Snackbar;

public class PasswordsFragment extends Fragment {

    private FragmentPasswordsBinding binding;
    PasswordsViewModel passwordsViewModel;

    PasswordAdapter passwordAdapter = new PasswordAdapter();

    @SuppressLint("NonConstantResourceId")
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPasswordsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        passwordsViewModel = new ViewModelProvider(this).get(PasswordsViewModel.class);

        binding.passwordRecyclerView.setLayoutManager(new LinearLayoutManager(PasswordsFragment.this.requireActivity()));
        binding.passwordRecyclerView.setHasFixedSize(true);
        binding.passwordRecyclerView.setAdapter(passwordAdapter);

        passwordsViewModel.getAllPasswords().observe(PasswordsFragment.this.requireActivity(), passwords -> passwordAdapter.setPasswords(passwords));

        binding.buttonAddPassword.setOnClickListener(v -> {
            Intent intent = new Intent(PasswordsFragment.this.requireActivity(), AddEditPasswordActivity.class);
            addPasswordResultLauncher.launch(intent);
        });

        passwordAdapter.setOnClickItemListener((password, position) -> {
            Intent intent = new Intent(PasswordsFragment.this.requireActivity(), AddEditPasswordActivity.class);
            intent.putExtra(AddEditPasswordActivity.EXTRA_ID, password.getId());
            intent.putExtra(AddEditPasswordActivity.EXTRA_URL, password.getUrl());
            intent.putExtra(AddEditPasswordActivity.EXTRA_EMAIL, password.getEmail());
            intent.putExtra(AddEditPasswordActivity.EXTRA_PASSWORD, password.getPassword());
            intent.putExtra(AddEditPasswordActivity.EXTRA_ADAPTER_POSITION, position);
            editPasswordResultLauncher.launch(intent);
        });

        passwordAdapter.setOnCopyClickListener((password, position) -> {
            Vibrator vibrator = (Vibrator) requireActivity().getSystemService(Context.VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(10, VibrationEffect.EFFECT_TICK));
            } else {
                vibrator.vibrate(100);
            }

            ClipboardManager clipboard = (ClipboardManager) requireActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("", password.getEmail() + " " + password.getPassword());
            clipboard.setPrimaryClip(clip);

            Snackbar.make(binding.coordinatorPassword, "Логин и пароль скопированны", Snackbar.LENGTH_SHORT).show();
        });

        binding.buttonGeneratePassword.setOnClickListener(v -> {
            NavController passwordController =
                    Navigation.findNavController(PasswordsFragment.this.requireActivity(),
                            R.id.nav_host_fragment_activity_main);
            passwordController.navigate(R.id.action_navigation_password_to_navigation_generator_password, null, getNavOptions());
        });

        binding.buttonGeneratePassword.setOnLongClickListener(view -> {
            String password = PasswordsViewModel.randomPassword(16, true, true);

            ClipboardManager clipboard = (ClipboardManager) requireActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("", password);
            assert clipboard != null;
            clipboard.setPrimaryClip(clip);
            Snackbar.make(view, "Пароль сгенерирован и скопирован", Snackbar.LENGTH_SHORT).setAction("done", null).show();

            return true;
        });

        binding.toolbarPasswords.inflateMenu(R.menu.password_menu);
        binding.toolbarPasswords.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.search_password:
                    NavController BinController = Navigation.findNavController(PasswordsFragment.this.requireActivity(),
                            R.id.nav_host_fragment_activity_main);
                    BinController.navigate(R.id.action_navigation_password_to_searchPasswordFragment);
                    return true;
                case R.id.password_settings:
                    startActivity(new Intent(PasswordsFragment.this.requireActivity(), SettingsActivity.class));
                    return true;
                default:
                    return false;
            }
        });

        return root;
    }

    protected NavOptions getNavOptions() {
        return new NavOptions.Builder()
                .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
                .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
                .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
                .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
                .build();
    }

    ActivityResultLauncher<Intent> addPasswordResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intent = result.getData();
                    assert intent != null;

                    String url = intent.getStringExtra(AddEditPasswordActivity.EXTRA_URL);
                    String email = intent.getStringExtra(AddEditPasswordActivity.EXTRA_EMAIL);
                    String password = intent.getStringExtra(AddEditPasswordActivity.EXTRA_PASSWORD);
                    Password save = new Password(url, email, password);

                    passwordsViewModel.insert(save);
                }
            }
    );

    ActivityResultLauncher<Intent> editPasswordResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intent = result.getData();
                    assert intent != null;

                    int id = intent.getIntExtra(AddEditPasswordActivity.EXTRA_ID, -1);
                    if (id == -1) {
                        Toast.makeText(PasswordsFragment.this.requireActivity(), "Password can't update", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String url = intent.getStringExtra(AddEditPasswordActivity.EXTRA_URL);
                    String email = intent.getStringExtra(AddEditPasswordActivity.EXTRA_EMAIL);
                    String password = intent.getStringExtra(AddEditPasswordActivity.EXTRA_PASSWORD);

                    Password password_obj = new Password(url, email, password);
                    password_obj.setId(id);
                    passwordsViewModel.update(password_obj);
                }
            }
    );

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}