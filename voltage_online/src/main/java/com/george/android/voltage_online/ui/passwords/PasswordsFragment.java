package com.george.android.voltage_online.ui.passwords;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.george.android.voltage_online.R;
import com.george.android.voltage_online.databinding.FragmentPasswordsBinding;
import com.george.android.voltage_online.ui.SettingsActivity;
import com.george.android.voltage_online.ui.adapters.PasswordAdapter;
import com.george.android.voltage_online.viewmodel.PasswordsViewModel;
import com.google.android.material.snackbar.Snackbar;

public class PasswordsFragment extends Fragment {

    private FragmentPasswordsBinding binding;
    private PasswordsViewModel passwordsViewModel;
    private final PasswordAdapter passwordAdapter = new PasswordAdapter();
    public static final String TAG = PasswordsFragment.class.getSimpleName();

    @SuppressLint("NonConstantResourceId")
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPasswordsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        passwordsViewModel = new ViewModelProvider(this).get(PasswordsViewModel.class);

        binding.passwordRecyclerView.setLayoutManager(new LinearLayoutManager(PasswordsFragment.this.requireActivity()));
        binding.passwordRecyclerView.setHasFixedSize(true);
        binding.passwordRecyclerView.setAdapter(passwordAdapter);

        binding.buttonAddPassword.setOnClickListener(v -> {
            Intent intent = new Intent(PasswordsFragment.this.requireActivity(), AddEditPasswordActivity.class);
            startActivity(intent);
        });

        passwordAdapter.setOnClickItemListener((password, position) -> {

            Log.d(TAG, "onCreateView: passwordId: " + password.getId());

            Intent intent = new Intent(PasswordsFragment.this.requireActivity(), AddEditPasswordActivity.class);
            intent.putExtra(AddEditPasswordActivity.EXTRA_ID, password.getId());
            intent.putExtra(AddEditPasswordActivity.EXTRA_URL, password.getUrl());
            intent.putExtra(AddEditPasswordActivity.EXTRA_EMAIL, password.getEmail());
            intent.putExtra(AddEditPasswordActivity.EXTRA_PASSWORD, password.getPassword());
            startActivity(intent);
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
            if (item.getItemId() == R.id.password_settings) {
                startActivity(new Intent(PasswordsFragment.this.requireActivity(), SettingsActivity.class));
                return true;
            }
            return false;
        });

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        passwordsViewModel
                .getAllPasswords()
                .observe(PasswordsFragment.this.requireActivity(), passwordAdapter::setPasswords);

    }

    protected NavOptions getNavOptions() {
        return new NavOptions.Builder()
                .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
                .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
                .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
                .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
                .build();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}