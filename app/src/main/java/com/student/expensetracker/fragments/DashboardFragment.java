package com.student.expensetracker.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.student.expensetracker.R;
import com.student.expensetracker.activities.AddEditExpenseActivity;
import com.student.expensetracker.activities.MainActivity;
import com.student.expensetracker.adapters.ExpenseAdapter;
import com.student.expensetracker.databinding.FragmentDashboardBinding;
import com.student.expensetracker.models.Expense;
import com.student.expensetracker.models.StudentProfile;
import com.student.expensetracker.utils.DateTimeUtils;
import com.student.expensetracker.viewmodels.ExpenseViewModel;
import com.student.expensetracker.viewmodels.ProfileViewModel;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment implements ExpenseAdapter.OnExpenseActionListener {

    private FragmentDashboardBinding binding;
    private ExpenseViewModel expenseViewModel;
    private ProfileViewModel profileViewModel;
    private ExpenseAdapter recentExpenseAdapter;

    private double currentBudget = 12500.0;
    private double currentInitialBalance = 20000.0;
    private double currentExpensesThisMonth = 0.0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        expenseViewModel = new ViewModelProvider(requireActivity()).get(ExpenseViewModel.class);
        profileViewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);

        setupRecyclerView();
        setupListeners();
        observeData();
    }

    private void setupRecyclerView() {
        recentExpenseAdapter = new ExpenseAdapter(this);
        binding.rvRecentExpenses.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvRecentExpenses.setAdapter(recentExpenseAdapter);
    }

    private void setupListeners() {
        binding.btnQuickAddExpense.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), AddEditExpenseActivity.class));
        });

        binding.tvViewAllExpenses.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToTab(R.id.nav_expenses);
            }
        });

        binding.btnViewReports.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToTab(R.id.nav_reports);
            }
        });
    }

    private void observeData() {
        // Observe Student Profile
        profileViewModel.getStudentProfile().observe(getViewLifecycleOwner(), profile -> {
            if (profile != null) {
                binding.tvStudentGreeting.setText("Hello, " + profile.getName() + " 👋");
                binding.tvStudentCollege.setText(profile.getCollege() + " • " + profile.getCourse() + " (" + profile.getStudentId() + ")");
                currentBudget = profile.getMonthlyBudget();
                currentInitialBalance = profile.getInitialBankBalance();
                binding.tvInitialBalanceHint.setText("Initial: " + DateTimeUtils.formatCurrency(currentInitialBalance));
                updateCalculations();
            }
        });

        // Observe Expenses for current month
        expenseViewModel.getTotalExpensesThisMonth().observe(getViewLifecycleOwner(), total -> {
            currentExpensesThisMonth = total != null ? total : 0.0;
            binding.tvTotalExpenses.setText(DateTimeUtils.formatCurrency(currentExpensesThisMonth));
            updateCalculations();
        });

        // Observe Transaction Count
        expenseViewModel.getExpensesCountThisMonth().observe(getViewLifecycleOwner(), count -> {
            int txCount = count != null ? count : 0;
            binding.tvTransactionsCount.setText(txCount + " Transaction" + (txCount == 1 ? "" : "s"));
        });

        // Observe Expenses List for recent preview (top 3)
        expenseViewModel.getAllExpenses().observe(getViewLifecycleOwner(), expenses -> {
            if (expenses != null && !expenses.isEmpty()) {
                binding.tvNoRecentExpenses.setVisibility(View.GONE);
                binding.rvRecentExpenses.setVisibility(View.VISIBLE);
                List<Expense> preview = new ArrayList<>();
                int limit = Math.min(3, expenses.size());
                for (int i = 0; i < limit; i++) {
                    preview.add(expenses.get(i));
                }
                recentExpenseAdapter.setExpenses(preview);
            } else {
                binding.tvNoRecentExpenses.setVisibility(View.VISIBLE);
                binding.rvRecentExpenses.setVisibility(View.GONE);
                recentExpenseAdapter.setExpenses(new ArrayList<>());
            }
        });
    }

    private void updateCalculations() {
        // Formula: Current Bank Balance = Initial Bank Balance - Total Expenses
        double currentBankBalance = currentInitialBalance - currentExpensesThisMonth;
        binding.tvBankBalance.setText(DateTimeUtils.formatCurrency(currentBankBalance));

        // Formula: Savings = Monthly Budget - Total Expenses
        double savings = currentBudget - currentExpensesThisMonth;
        binding.tvTotalSavings.setText(DateTimeUtils.formatCurrency(savings));
        binding.tvMonthlyBudget.setText(DateTimeUtils.formatCurrency(currentBudget));

        if (currentBudget > 0) {
            int percentUsed = (int) Math.round((currentExpensesThisMonth / currentBudget) * 100);
            binding.tvBudgetSpentPercent.setText(percentUsed + "% Spent");

            if (percentUsed >= 80) {
                binding.cardBudgetWarning.setVisibility(View.VISIBLE);
                if (percentUsed >= 100) {
                    binding.tvBudgetWarningMsg.setText(
                            String.format("You have exceeded 100%% of your budget! Overspent by %s.",
                                    DateTimeUtils.formatCurrency(currentExpensesThisMonth - currentBudget)));
                } else {
                    binding.tvBudgetWarningMsg.setText(
                            String.format("You have spent %d%% of your monthly budget. Only %s remaining!",
                                    percentUsed, DateTimeUtils.formatCurrency(savings)));
                }
            } else {
                binding.cardBudgetWarning.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onEdit(Expense expense) {
        Intent intent = new Intent(requireContext(), AddEditExpenseActivity.class);
        intent.putExtra(AddEditExpenseActivity.EXTRA_EXPENSE, expense);
        startActivity(intent);
    }

    @Override
    public void onDelete(Expense expense) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Expense")
                .setMessage("Are you sure you want to delete '" + expense.getTitle() + "'?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    expenseViewModel.delete(expense);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onItemClick(Expense expense) {
        onEdit(expense);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
