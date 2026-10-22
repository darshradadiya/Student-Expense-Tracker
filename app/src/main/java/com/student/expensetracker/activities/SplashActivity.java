package com.student.expensetracker.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.student.expensetracker.database.AppDatabase;
import com.student.expensetracker.databinding.ActivitySplashBinding;
import com.student.expensetracker.utils.PreferenceHelper;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Apply saved dark mode theme before rendering
        if (PreferenceHelper.isDarkMode(this)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        super.onCreate(savedInstanceState);
        ActivitySplashBinding binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize database asynchronously to pre-seed sample data on first run
        AppDatabase.databaseWriteExecutor.execute(() -> {
            AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
            if (PreferenceHelper.isFirstRun(getApplicationContext())) {
                AppDatabase.populateSampleData(db);
            }
        });

        // 1.5 seconds splash delay for smooth student UX
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (PreferenceHelper.isLoggedIn(SplashActivity.this)) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            } else {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
            finish();
        }, 1500);
    }
}
