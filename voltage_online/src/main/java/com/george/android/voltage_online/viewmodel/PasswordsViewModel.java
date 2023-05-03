package com.george.android.voltage_online.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.george.android.voltage_online.model.Password;

import java.util.List;
import java.util.Random;

public class PasswordsViewModel extends AndroidViewModel {
    public PasswordsViewModel(@NonNull Application application) {
        super(application);
    }


    public MutableLiveData<List<Password>> getAllPasswords() {
        return null;
    }

    public static String randomPassword(int length, boolean isSymbols, boolean isNumbers) {
        String chars = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
        String numbers = "1234567890";
        String symbols = "%*)?@#$~";
        String password;

        if(isSymbols & isNumbers)
            password =  numbers + chars + symbols;
        else if(!isSymbols & isNumbers)
            password = numbers + chars + numbers;
        else if(isSymbols)
            password = symbols + chars + symbols;
        else
            password = chars;

        StringBuilder result = new StringBuilder();
        while (length > 0) {
            Random rand = new Random();
            result.append(password.charAt(rand.nextInt(password.length())));
            length--;
        }

        return result.toString();
    }

}
