package com.george.android.tasker.ui.passwords;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.george.android.tasker.databinding.FragmentPasswordGeneratorBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;
import java.util.Random;

public class PasswordGeneratorFragment extends Fragment {

    FragmentPasswordGeneratorBinding generatorBinding;

    int password_length = 16;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        generatorBinding = FragmentPasswordGeneratorBinding.inflate(inflater, container, false);
        View root = generatorBinding.getRoot();

        generatorBinding.toolbarGenerator.setNavigationOnClickListener(v -> requireActivity().onBackPressed());

        generatorBinding.generatePassword.setOnClickListener(v -> {
            String checkPasswordLength = Objects.requireNonNull(generatorBinding.lengthPassword.getText()).toString();
            if(generatorBinding.checkBoxSymbols.isChecked()) {
                if(checkPasswordLength.isEmpty()) {
                    generatorBinding.lengthPassword.setError("Это поле не может быть пустым");
                }
                else if(Integer.parseInt(generatorBinding.lengthPassword.getText().toString()) > 128 ) {
                    generatorBinding.lengthPassword.setError("Неверное значение. Максимум 128");
                } else {
                    password_length = Integer.parseInt(generatorBinding.lengthPassword.getText().toString());
                    generatorBinding.passwordText.setText(getRandomPasswordSymbols(password_length));
                    generatorBinding.lengthPassword.setError(null);
                }
            } else {
                if(checkPasswordLength.isEmpty()) {
                    generatorBinding.lengthPassword.setError("Это поле не может быть пустым");
                }
                else if(Integer.parseInt(generatorBinding.lengthPassword.getText().toString()) > 128) {
                    generatorBinding.lengthPassword.setError("Неверное значение. Максимум 128");
                } else {
                    password_length = Integer.parseInt(generatorBinding.lengthPassword.getText().toString());
                    generatorBinding.passwordText.setText(getRandomPassword(password_length));
                    generatorBinding.lengthPassword.setError(null);
                }
            }
        });

        generatorBinding.copyPassword.setOnClickListener(this::onCopyBtnClick);

        return root;
    }

    //Генератор паролей с символами
    @SuppressWarnings("SpellCheckingInspection")
    public static String getRandomPasswordSymbols(int passwordLength) {
        final String charsPassword ="1234567890qwertyuiopasdfghjklzxcvbnm%*)?@#$~QWERTYUIOPASDFGHJKLZXCVBNM%*)?@#$~1234567890";
        StringBuilder result = new StringBuilder();
        while(passwordLength > 0) {
            Random rand = new Random();
            result.append(charsPassword.charAt(rand.nextInt(charsPassword.length())));
            passwordLength--;
        }
        return result.toString();
    }

    //Генератор паролей без символов
    @SuppressWarnings("SpellCheckingInspection")
    public static String getRandomPassword(int passwordLength) {
        final String noSymbolsCharsPassword ="1234567980qwertyuiopasdfghjklzxcvbnm1234567890QWERTYUIOPASDFGHJKLZXCVBNM";
        StringBuilder result = new StringBuilder();
        while(passwordLength > 0) {
            Random rand = new Random();
            result.append(noSymbolsCharsPassword.charAt(rand.nextInt(noSymbolsCharsPassword.length())));
            passwordLength--;
        }
        return result.toString();
    }


    public void onCopyBtnClick(View view) {
        if(Objects.requireNonNull(generatorBinding.passwordText.getText()).toString().equals("Сгенерируй меня!")) {
            Snackbar.make(view, "Сгенерируй пароль что бы его скопировать", Snackbar.LENGTH_SHORT).setAction("error", null).show();
        } else {
            ClipboardManager clipboard = (ClipboardManager) requireActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("", generatorBinding.passwordText.getText().toString());
            assert clipboard != null;
            clipboard.setPrimaryClip(clip);
            Snackbar.make(view, "Пароль скопирован", Snackbar.LENGTH_SHORT).setAction("done", null).show();
        }
    }

}
