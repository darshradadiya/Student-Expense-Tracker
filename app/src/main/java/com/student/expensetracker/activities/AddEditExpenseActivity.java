package com.student.expensetracker.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.student.expensetracker.R;
import com.student.expensetracker.databinding.ActivityAddEditExpenseBinding;
import com.student.expensetracker.models.Expense;
import com.student.expensetracker.utils.DateTimeUtils;
import com.student.expensetracker.utils.NotificationHelper;
import com.student.expensetracker.viewmodels.ExpenseViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddEditExpenseActivity extends AppCompatActivity {

    public static final String EXTRA_EXPENSE = "extra_expense";

    private ActivityAddEditExpenseBinding binding;
    private ExpenseViewModel viewModel;
    private Expense existingExpense;
    private final Calendar calendar = Calendar.getInstance();
    private String selectedIsoDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddEditExpenseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);

        binding.toolbar.setNavigationOnClickListener(v -> finish());

        // Check if editing an existing expense
        if (getIntent().hasExtra(EXTRA_EXPENSE)) {
            existingExpense = (Expense) getIntent().getSerializableExtra(EXTRA_EXPENSE);
        }

        setupDatePicker();

        if (existingExpense != null) {
            binding.toolbar.setTitle(R.string.title_edit_expense);
            binding.btnSaveExpense.setText(R.string.btn_update_expense);
            populateFields(existingExpense);
        } else {
            binding.toolbar.setTitle(R.string.title_add_expense);
            binding.btnSaveExpense.setText(R.string.btn_save_expense);
            selectedIsoDate = DateTimeUtils.getCurrentDateString();
            binding.etDate.setText(DateTimeUtils.formatDateForDisplay(selectedIsoDate));
        }

        binding.btnSaveExpense.setOnClickListener(v -> saveExpense());
    }

    private void setupDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            selectedIsoDate = isoFormat.format(calendar.getTime());
            binding.etDate.setText(DateTimeUtils.formatDateForDisplay(selectedIsoDate));
        };

        binding.etDate.setOnClickListener(v -> {
            new DatePickerDialog(
                    AddEditExpenseActivity.this,
                    dateSetListener,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            ).show();
        });
    }

    private void populateFields(Expense expense) {
        binding.etTitle.setText(expense.getTitle());
        binding.etAmount.setText(String.valueOf(expense.getAmount()));
        selectedIsoDate = expense.getDate();
        binding.etDate.setText(DateTimeUtils.formatDateForDisplay(expense.getDate()));

        if (expense.getNotes() != null) {
            binding.etNotes.setText(expense.getNotes());
        }

        // Select matching category chip
        selectCategoryChip(expense.getCategory());
    }

    private void selectCategoryChip(String category) {
        if (category == null) return;
        switch (category) {
            case "Food":
                binding.chipFood.setChecked(true);
                break;
            case "Travel":
                binding.chipTravel.setChecked(true);
                break;
            case "Books":
                binding.chipBooks.setChecked(true);
                break;
            case "Entertainment":
                binding.chipEntertainment.setChecked(true);
                break;
            case "Shopping":
                binding.chipShopping.setChecked(true);
                break;
            case "Other":
            default:
                binding.chipOther.setChecked(true);
                break;
        }
    }

    private String getSelectedCategory() {
        int checkedId = binding.chipGroupCategory.getCheckedChipId();
        if (checkedId == R.id.chipFood) return "Food";
        if (checkedId == R.id.chipTravel) return "Travel";
        if (checkedId == R.id.chipBooks) return "Books";
        if (checkedId == R.id.chipEntertainment) return "Entertainment";
        if (checkedId == R.id.chipShopping) return "Shopping";
        return "Other";
    }

    private void saveExpense() {
        String title = binding.etTitle.getText() != null ? binding.etTitle.getText().toString().trim() : "";
        String amountStr = binding.etAmount.getText() != null ? binding.etAmount.getText().toString().trim() : "";
        String notes = binding.etNotes.getText() != null ? binding.etNotes.getText().toString().trim() : "";
        String category = getSelectedCategory();

        binding.tilTitle.setError(null);
        binding.tilAmount.setError(null);

        if (TextUtils.isEmpty(title)) {
            binding.tilTitle.setError(getString(R.string.error_enter_title));
            binding.tilTitle.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(amountStr)) {
            binding.tilAmount.setError(getString(R.string.error_enter_amount));
            binding.tilAmount.requestFocus();
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                binding.tilAmount.setError("Amount must be greater than 0");
                binding.tilAmount.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            binding.tilAmount.setError(getString(R.string.error_enter_amount));
            binding.tilAmount.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(selectedIsoDate)) {
            selectedIsoDate = DateTimeUtils.getCurrentDateString();
        }

        if (existingExpense != null) {
            // Update
            existingExpense.setTitle(title);
            existingExpense.setAmount(amount);
            existingExpense.setCategory(category);
            existingExpense.setDate(selectedIsoDate);
            existingExpense.setNotes(notes);

            viewModel.update(existingExpense);
            Toast.makeText(this, "Expense updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            // Insert
            Expense newExpense = new Expense(title, amount, category, selectedIsoDate, notes, System.currentTimeMillis());
            viewModel.insert(newExpense);

            // Trigger notification
            NotificationHelper.showExpenseAddedNotification(this, title, amount, category);
            Toast.makeText(this, "Expense added successfully", Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}
