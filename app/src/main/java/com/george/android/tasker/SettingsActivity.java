package com.george.android.tasker;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.george.android.tasker.databinding.SettingsActivityBinding;

public class SettingsActivity extends AppCompatActivity {

    SettingsActivityBinding settingsActivityBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingsActivityBinding = SettingsActivityBinding.inflate(getLayoutInflater());
        setContentView(settingsActivityBinding.getRoot());

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }

        settingsActivityBinding.toolbarSetting.setNavigationOnClickListener(v -> onBackPressed());
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }
}