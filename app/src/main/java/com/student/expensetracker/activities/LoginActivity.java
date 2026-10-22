package com.student.expensetracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.student.expensetracker.R;
import com.student.expensetracker.databinding.ActivityLoginBinding;
import com.student.expensetracker.utils.PreferenceHelper;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Restore saved email if remember me was enabled
        if (PreferenceHelper.isRememberMe(this)) {
            binding.etEmail.setText(PreferenceHelper.getSavedEmail(this));
            binding.cbRememberMe.setChecked(true);
        } else {
            binding.etEmail.setText("rahul.patel@abc.edu");
            binding.etPassword.setText("student123");
        }

        binding.btnLogin.setOnClickListener(v -> performLogin());

        binding.btnDemo.setOnClickListener(v -> {
            binding.etEmail.setText("rahul.patel@abc.edu");
            binding.etPassword.setText("student123");
            Toast.makeText(this, "Sample credentials populated", Toast.LENGTH_SHORT).show();
        });
    }

    private void performLogin() {
        String email = binding.etEmail.getText() != null ? binding.etEmail.getText().toString().trim() : "";
        String password = binding.etPassword.getText() != null ? binding.etPassword.getText().toString().trim() : "";

        // Reset errors
        binding.tilEmail.setError(null);
        binding.tilPassword.setError(null);

        if (TextUtils.isEmpty(email)) {
            binding.tilEmail.setError(getString(R.string.login_error_empty));
            binding.tilEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.setError("Please enter a valid student email");
            binding.tilEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            binding.tilPassword.setError(getString(R.string.login_error_empty));
            binding.tilPassword.requestFocus();
            return;
        }

        if (password.length() < 4) {
            binding.tilPassword.setError("Password must be at least 4 characters");
            binding.tilPassword.requestFocus();
            return;
        }

        // Save session and remember me preferences
        boolean remember = binding.cbRememberMe.isChecked();
        PreferenceHelper.setRememberMe(this, remember, email);
        PreferenceHelper.setLoggedIn(this, true);

        Toast.makeText(this, "Welcome, Rahul Patel!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
