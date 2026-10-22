package com.student.expensetracker.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.student.expensetracker.adapters.ExpenseAdapter;
import com.student.expensetracker.databinding.FragmentExpensesBinding;
import com.student.expensetracker.models.Expense;
import com.student.expensetracker.viewmodels.ExpenseViewModel;

public class ExpensesFragment extends Fragment implements ExpenseAdapter.OnExpenseActionListener {

    private FragmentExpensesBinding binding;
    private ExpenseViewModel viewModel;
    private ExpenseAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentExpensesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(ExpenseViewModel.class);

        setupRecyclerView();
        setupSearchAndFilter();
        setupFab();
        observeExpenses();
    }

    private void setupRecyclerView() {
        adapter = new ExpenseAdapter(this);
        binding.rvExpenses.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvExpenses.setAdapter(adapter);
    }

    private void setupSearchAndFilter() {
        // Search text watcher
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s != null ? s.toString() : "";
                binding.btnClearSearch.setVisibility(query.isEmpty() ? View.GONE : View.VISIBLE);
                viewModel.setSearchQuery(query);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        binding.btnClearSearch.setOnClickListener(v -> binding.etSearch.setText(""));

        // Category Filter Chips
        binding.chipGroupFilter.setOnCheckedChangeListener((group, checkedId) -> {
            String category = "All";
            if (checkedId == R.id.filterFood) category = "Food";
            else if (checkedId == R.id.filterTravel) category = "Travel";
            else if (checkedId == R.id.filterBooks) category = "Books";
            else if (checkedId == R.id.filterEntertainment) category = "Entertainment";
            else if (checkedId == R.id.filterShopping) category = "Shopping";
            else if (checkedId == R.id.filterOther) category = "Other";

            viewModel.setCategoryFilter(category);
        });
    }

    private void setupFab() {
        binding.fabAddExpense.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), AddEditExpenseActivity.class));
        });
    }

    private void observeExpenses() {
        viewModel.getFilteredExpenses().observe(getViewLifecycleOwner(), expenses -> {
            if (expenses != null && !expenses.isEmpty()) {
                adapter.setExpenses(expenses);
                binding.rvExpenses.setVisibility(View.VISIBLE);
                binding.layoutEmptyState.setVisibility(View.GONE);
                binding.tvExpensesCount.setText("Showing " + expenses.size() + " expense" + (expenses.size() == 1 ? "" : "s"));
            } else {
                adapter.setExpenses(null);
                binding.rvExpenses.setVisibility(View.GONE);
                binding.layoutEmptyState.setVisibility(View.VISIBLE);
                binding.tvExpensesCount.setText("No expenses match filter");
            }
        });
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
                    viewModel.delete(expense);
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
