package com.student.expensetracker.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.student.expensetracker.database.AppDatabase;
import com.student.expensetracker.database.ExpenseDao;
import com.student.expensetracker.database.ProfileDao;
import com.student.expensetracker.models.CategorySum;
import com.student.expensetracker.models.Expense;
import com.student.expensetracker.models.StudentProfile;

import java.util.List;

/**
 * Repository class abstracting data sources for ViewModels according to MVVM architecture.
 */
public class ExpenseRepository {

    private final ExpenseDao expenseDao;
    private final ProfileDao profileDao;
    private final AppDatabase database;

    public ExpenseRepository(Application application) {
        database = AppDatabase.getDatabase(application);
        expenseDao = database.expenseDao();
        profileDao = database.profileDao();
    }

    // Expense Operations
    public LiveData<List<Expense>> getAllExpenses() {
        return expenseDao.getAllExpenses();
    }

    public LiveData<List<Expense>> getExpensesByCategory(String category) {
        if (category == null || category.equalsIgnoreCase("All")) {
            return expenseDao.getAllExpenses();
        }
        return expenseDao.getExpensesByCategory(category);
    }

    public LiveData<List<Expense>> searchExpenses(String query) {
        return expenseDao.searchExpenses(query);
    }

    public LiveData<List<Expense>> searchExpensesByCategory(String query, String category) {
        if (category == null || category.equalsIgnoreCase("All")) {
            return expenseDao.searchExpenses(query);
        }
        return expenseDao.searchExpensesByCategory(query, category);
    }

    public LiveData<Double> getTotalExpensesThisMonth(String monthPrefix) {
        return expenseDao.getTotalExpensesThisMonth(monthPrefix);
    }

    public LiveData<Integer> getExpensesCountThisMonth(String monthPrefix) {
        return expenseDao.getExpensesCountThisMonth(monthPrefix);
    }

    public LiveData<List<CategorySum>> getCategoryBreakdown(String monthPrefix) {
        return expenseDao.getCategoryBreakdown(monthPrefix);
    }

    public void insertExpense(Expense expense) {
        AppDatabase.databaseWriteExecutor.execute(() -> expenseDao.insert(expense));
    }

    public void updateExpense(Expense expense) {
        AppDatabase.databaseWriteExecutor.execute(() -> expenseDao.update(expense));
    }

    public void deleteExpense(Expense expense) {
        AppDatabase.databaseWriteExecutor.execute(() -> expenseDao.delete(expense));
    }

    public void deleteAllExpenses() {
        AppDatabase.databaseWriteExecutor.execute(expenseDao::deleteAll);
    }

    // Student Profile Operations
    public LiveData<StudentProfile> getStudentProfile() {
        return profileDao.getProfile();
    }

    public void updateStudentProfile(StudentProfile profile) {
        AppDatabase.databaseWriteExecutor.execute(() -> profileDao.update(profile));
    }

    public void updateBalances(double bankBalance, double monthlyBudget) {
        AppDatabase.databaseWriteExecutor.execute(() -> profileDao.updateBalances(bankBalance, monthlyBudget));
    }

    public void resetToSampleData() {
        AppDatabase.databaseWriteExecutor.execute(() -> AppDatabase.populateSampleData(database));
    }

    public interface SyncCallback<T> {
        void onLoaded(T result);
    }

    public void getExpensesForMonthSync(String monthPrefix, SyncCallback<List<Expense>> callback) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<Expense> list = expenseDao.getExpensesForMonthSync(monthPrefix);
            callback.onLoaded(list);
        });
    }

    public void getProfileSync(SyncCallback<StudentProfile> callback) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            StudentProfile profile = profileDao.getProfileSync();
            callback.onLoaded(profile);
        });
    }
}
