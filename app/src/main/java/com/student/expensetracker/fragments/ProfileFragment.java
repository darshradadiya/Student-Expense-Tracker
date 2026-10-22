package com.student.expensetracker.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.student.expensetracker.R;
import com.student.expensetracker.activities.LoginActivity;
import com.student.expensetracker.databinding.FragmentProfileBinding;
import com.student.expensetracker.models.StudentProfile;
import com.student.expensetracker.utils.DateTimeUtils;
import com.student.expensetracker.utils.PreferenceHelper;
import com.student.expensetracker.viewmodels.ProfileViewModel;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ProfileViewModel viewModel;
    private StudentProfile currentProfile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);

        setupDarkModeSwitch();
        setupListeners();
        observeProfile();
    }

    private void setupDarkModeSwitch() {
        boolean isDark = PreferenceHelper.isDarkMode(requireContext());
        binding.switchDarkMode.setChecked(isDark);

        binding.switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            PreferenceHelper.setDarkMode(requireContext(), isChecked);
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });
    }

    private void setupListeners() {
        binding.btnUpdateBalances.setOnClickListener(v -> showUpdateBalancesDialog());

        binding.btnResetData.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Reset to Sample Data")
                    .setMessage("This will reset student info, bank balance (₹20,000), monthly budget (₹12,500), and sample expenses.")
                    .setPositiveButton("Reset", (dialog, which) -> {
                        viewModel.resetToSampleData();
                        Toast.makeText(requireContext(), "Database reset to sample student data", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        binding.btnLogout.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to log out?")
                    .setPositiveButton("Logout", (dialog, which) -> {
                        PreferenceHelper.setLoggedIn(requireContext(), false);
                        Intent intent = new Intent(requireContext(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        if (getActivity() != null) {
                            getActivity().finish();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    private void observeProfile() {
        viewModel.getStudentProfile().observe(getViewLifecycleOwner(), profile -> {
            if (profile != null) {
                currentProfile = profile;
                binding.tvProfileName.setText(profile.getName());
                binding.tvProfileId.setText("ID: " + profile.getStudentId());
                binding.tvProfileCollege.setText(profile.getCollege());
                binding.tvProfileCourse.setText(profile.getCourse());
                binding.tvProfileEmail.setText(profile.getEmail());

                binding.tvProfileBankBalance.setText(DateTimeUtils.formatCurrency(profile.getInitialBankBalance()));
                binding.tvProfileMonthlyBudget.setText(DateTimeUtils.formatCurrency(profile.getMonthlyBudget()));
            }
        });
    }

    private void showUpdateBalancesDialog() {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_balance_budget, null);
        EditText etBank = dialogView.findViewById(R.id.etBankBalance);
        EditText etBudget = dialogView.findViewById(R.id.etMonthlyBudget);

        if (currentProfile != null) {
            etBank.setText(String.valueOf(currentProfile.getInitialBankBalance()));
            etBudget.setText(String.valueOf(currentProfile.getMonthlyBudget()));
        }

        new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    String bankStr = etBank.getText() != null ? etBank.getText().toString().trim() : "";
                    String budgetStr = etBudget.getText() != null ? etBudget.getText().toString().trim() : "";

                    if (!bankStr.isEmpty() && !budgetStr.isEmpty()) {
                        try {
                            double bank = Double.parseDouble(bankStr);
                            double budget = Double.parseDouble(budgetStr);
                            viewModel.updateBalances(bank, budget);
                            Toast.makeText(requireContext(), "Balances updated", Toast.LENGTH_SHORT).show();
                        } catch (NumberFormatException e) {
                            Toast.makeText(requireContext(), "Please enter valid numbers", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
