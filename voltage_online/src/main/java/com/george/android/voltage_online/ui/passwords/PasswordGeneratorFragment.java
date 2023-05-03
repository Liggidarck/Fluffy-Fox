package com.george.android.voltage_online.ui.passwords;


import static com.george.android.voltage_online.viewmodel.PasswordsViewModel.randomPassword;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.george.android.voltage_online.databinding.FragmentPasswordGeneratorBinding;
import com.google.android.material.snackbar.Snackbar;

public class PasswordGeneratorFragment extends Fragment {

    FragmentPasswordGeneratorBinding generatorBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        generatorBinding = FragmentPasswordGeneratorBinding.inflate(inflater, container, false);
        View root = generatorBinding.getRoot();
        generatorBinding.toolbarGenerator.setNavigationOnClickListener(v -> requireActivity().onBackPressed());

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity().getBaseContext());
        String maxValue = sharedPreferences.getString("generator_passwords_length", "32");

        generatorBinding.lengthPasswordSlider.setStepSize(1);
        generatorBinding.lengthPasswordSlider.setValueFrom(1);
        generatorBinding.lengthPasswordSlider.setValueTo(Float.parseFloat(maxValue));
        generatorBinding.lengthPasswordSlider.setValue(16);

        generatorBinding.passwordText.setText(randomPassword(16, true, true));

        generatorBinding.generatePassword.setOnClickListener(v -> {
            int length = (int) generatorBinding.lengthPasswordSlider.getValue();
            boolean isSymbols = generatorBinding.checkBoxSymbols.isChecked();
            boolean isNumbers = generatorBinding.checkBoxNumbers.isChecked();
            generatorBinding.passwordText.setText(randomPassword(length, isSymbols, isNumbers));
        });

        generatorBinding.lengthPasswordSlider.addOnChangeListener((slider, value, fromUser) -> {
            boolean isSymbols = generatorBinding.checkBoxSymbols.isChecked();
            boolean isNumbers = generatorBinding.checkBoxNumbers.isChecked();

            generatorBinding.passwordText.setText(randomPassword((int) value, isSymbols, isNumbers));

            Vibrator vibrator = (Vibrator) requireActivity().getSystemService(Context.VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(10, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(100);
            }

        });

        generatorBinding.copyPassword.setOnClickListener(this::onCopyBtnClick);

        return root;
    }

    public void onCopyBtnClick(View view) {
        ClipboardManager clipboard = (ClipboardManager) requireActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("", generatorBinding.passwordText.getText().toString());
        assert clipboard != null;
        clipboard.setPrimaryClip(clip);
        Snackbar.make(view, "Пароль скопирован", Snackbar.LENGTH_SHORT).setAction("done", null).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        generatorBinding = null;
    }
}
