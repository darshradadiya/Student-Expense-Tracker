package com.student.expensetracker.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.student.expensetracker.models.CategorySum;
import com.student.expensetracker.models.Expense;
import com.student.expensetracker.models.StudentProfile;
import com.student.expensetracker.repository.ExpenseRepository;
import com.student.expensetracker.utils.DateTimeUtils;

import java.util.List;

public class ExpenseViewModel extends AndroidViewModel {

    private final ExpenseRepository repository;
    private final String currentMonthPrefix;

    private final MutableLiveData<String> selectedCategory = new MutableLiveData<>("All");
    private final MutableLiveData<String> searchQuery = new MutableLiveData<>("");
    private final MediatorLiveData<List<Expense>> filteredExpenses = new MediatorLiveData<>();

    private LiveData<List<Expense>> currentSourceLiveData;

    public ExpenseViewModel(@NonNull Application application) {
        super(application);
        repository = new ExpenseRepository(application);
        currentMonthPrefix = DateTimeUtils.getCurrentMonthPrefix();

        filteredExpenses.addSource(selectedCategory, cat -> updateFilter());
        filteredExpenses.addSource(searchQuery, query -> updateFilter());

        updateFilter();
    }

    private void updateFilter() {
        if (currentSourceLiveData != null) {
            filteredExpenses.removeSource(currentSourceLiveData);
        }

        String cat = selectedCategory.getValue();
        String query = searchQuery.getValue();

        if (query != null && !query.trim().isEmpty()) {
            if (cat != null && !cat.equalsIgnoreCase("All")) {
                currentSourceLiveData = repository.searchExpensesByCategory(query.trim(), cat);
            } else {
                currentSourceLiveData = repository.searchExpenses(query.trim());
            }
        } else {
            if (cat != null && !cat.equalsIgnoreCase("All")) {
                currentSourceLiveData = repository.getExpensesByCategory(cat);
            } else {
                currentSourceLiveData = repository.getAllExpenses();
            }
        }

        filteredExpenses.addSource(currentSourceLiveData, expenses -> filteredExpenses.setValue(expenses));
    }

    public LiveData<List<Expense>> getFilteredExpenses() {
        return filteredExpenses;
    }

    public void setCategoryFilter(String category) {
        selectedCategory.setValue(category);
    }

    public void setSearchQuery(String query) {
        searchQuery.setValue(query);
    }

    public LiveData<Double> getTotalExpensesThisMonth() {
        return repository.getTotalExpensesThisMonth(currentMonthPrefix);
    }

    public LiveData<Integer> getExpensesCountThisMonth() {
        return repository.getExpensesCountThisMonth(currentMonthPrefix);
    }

    public LiveData<List<CategorySum>> getCategoryBreakdown() {
        return repository.getCategoryBreakdown(currentMonthPrefix);
    }

    public LiveData<StudentProfile> getStudentProfile() {
        return repository.getStudentProfile();
    }

    public LiveData<List<Expense>> getAllExpenses() {
        return repository.getAllExpenses();
    }

    public void insert(Expense expense) {
        repository.insertExpense(expense);
    }

    public void update(Expense expense) {
        repository.updateExpense(expense);
    }

    public void delete(Expense expense) {
        repository.deleteExpense(expense);
    }

    public void getExpensesForMonthSync(ExpenseRepository.SyncCallback<List<Expense>> callback) {
        repository.getExpensesForMonthSync(currentMonthPrefix, callback);
    }

    public void getProfileSync(ExpenseRepository.SyncCallback<StudentProfile> callback) {
        repository.getProfileSync(callback);
    }
}
