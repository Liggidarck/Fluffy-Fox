package com.george.android.tasker.ui.passwords;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.george.android.tasker.ui.adapters.PasswordAdapter;
import com.george.android.tasker.data.model.Password;
import com.george.android.tasker.data.viewmodel.PasswordsViewModel;
import com.george.android.tasker.databinding.FragmentPasswordSearchBinding;

import java.util.Objects;

public class SearchPasswordFragment extends Fragment {

    FragmentPasswordSearchBinding binding;
    PasswordsViewModel passwordsViewModel;
    PasswordAdapter passwordAdapter = new PasswordAdapter();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPasswordSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        passwordsViewModel = new ViewModelProvider(SearchPasswordFragment.this.requireActivity()).get(PasswordsViewModel.class);

        binding.searchPasswordToolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());

        binding.recyclerSearchPassword.setLayoutManager(new LinearLayoutManager(SearchPasswordFragment.this.requireActivity()));
        binding.recyclerSearchPassword.setHasFixedSize(true);
        binding.recyclerSearchPassword.setAdapter(passwordAdapter);

        passwordsViewModel.getAllPasswords().observe(SearchPasswordFragment.this.requireActivity(),
                passwords -> passwordAdapter.setPasswords(passwords));

        passwordAdapter.setOnClickItemListener((password, position) -> {
            Intent intent = new Intent(SearchPasswordFragment.this.requireActivity(), AddEditPasswordActivity.class);
            intent.putExtra(AddEditPasswordActivity.EXTRA_ID, password.getId());
            intent.putExtra(AddEditPasswordActivity.EXTRA_URL, password.getUrl());
            intent.putExtra(AddEditPasswordActivity.EXTRA_EMAIL, password.getEmail());
            intent.putExtra(AddEditPasswordActivity.EXTRA_PASSWORD, password.getPassword());
            intent.putExtra(AddEditPasswordActivity.EXTRA_ADAPTER_POSITION, position);
            editPasswordResultLauncher.launch(intent);
        });

        Objects.requireNonNull(binding.textInputPasswordSearch.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                passwordsViewModel.findPassword(s.toString()).observe(SearchPasswordFragment.this.requireActivity(),
                        passwords -> passwordAdapter.setPasswords(passwords));
            }
        });

        return root;
    }

    ActivityResultLauncher<Intent> editPasswordResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intent = result.getData();
                    assert intent != null;

                    int id = intent.getIntExtra(AddEditPasswordActivity.EXTRA_ID, -1);
                    if (id == -1) {
                        Toast.makeText(SearchPasswordFragment.this.requireActivity(), "Password can't update", Toast.LENGTH_SHORT).show();
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
