package com.student.expensetracker.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.student.expensetracker.R;
import com.student.expensetracker.databinding.ActivityMainBinding;
import com.student.expensetracker.fragments.DashboardFragment;
import com.student.expensetracker.fragments.ExpensesFragment;
import com.student.expensetracker.fragments.ProfileFragment;
import com.student.expensetracker.fragments.ReportsFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private final Fragment dashboardFragment = new DashboardFragment();
    private final Fragment expensesFragment = new ExpensesFragment();
    private final Fragment reportsFragment = new ReportsFragment();
    private final Fragment profileFragment = new ProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        if (savedInstanceState == null) {
            loadFragment(dashboardFragment, getString(R.string.app_name));
        }

        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_dashboard) {
                loadFragment(dashboardFragment, getString(R.string.app_name));
                return true;
            } else if (itemId == R.id.nav_expenses) {
                loadFragment(expensesFragment, getString(R.string.all_expenses));
                return true;
            } else if (itemId == R.id.nav_reports) {
                loadFragment(reportsFragment, getString(R.string.monthly_summary_title));
                return true;
            } else if (itemId == R.id.nav_profile) {
                loadFragment(profileFragment, getString(R.string.profile_title));
                return true;
            }
            return false;
        });
    }

    private void loadFragment(Fragment fragment, String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    public void navigateToTab(int tabMenuItemId) {
        binding.bottomNavigation.setSelectedItemId(tabMenuItemId);
    }
}
