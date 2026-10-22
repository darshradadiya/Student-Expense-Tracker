package com.student.expensetracker.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.student.expensetracker.models.CategorySum;
import com.student.expensetracker.models.Expense;

import java.util.List;

@Dao
public interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Expense expense);

    @Update
    void update(Expense expense);

    @Delete
    void delete(Expense expense);

    @Query("DELETE FROM expenses")
    void deleteAll();

    @Query("SELECT * FROM expenses ORDER BY timestamp DESC")
    LiveData<List<Expense>> getAllExpenses();

    @Query("SELECT * FROM expenses WHERE category = :category ORDER BY timestamp DESC")
    LiveData<List<Expense>> getExpensesByCategory(String category);

    @Query("SELECT * FROM expenses WHERE title LIKE '%' || :query || '%' OR notes LIKE '%' || :query || '%' ORDER BY timestamp DESC")
    LiveData<List<Expense>> searchExpenses(String query);

    @Query("SELECT * FROM expenses WHERE category = :category AND (title LIKE '%' || :query || '%' OR notes LIKE '%' || :query || '%') ORDER BY timestamp DESC")
    LiveData<List<Expense>> searchExpensesByCategory(String query, String category);

    @Query("SELECT COALESCE(SUM(amount), 0.0) FROM expenses WHERE date LIKE :monthPrefix || '%'")
    LiveData<Double> getTotalExpensesThisMonth(String monthPrefix);

    @Query("SELECT COUNT(*) FROM expenses WHERE date LIKE :monthPrefix || '%'")
    LiveData<Integer> getExpensesCountThisMonth(String monthPrefix);

    @Query("SELECT COALESCE(SUM(amount), 0.0) FROM expenses")
    LiveData<Double> getLifetimeTotalExpenses();

    @Query("SELECT category, COALESCE(SUM(amount), 0.0) as totalAmount FROM expenses WHERE date LIKE :monthPrefix || '%' GROUP BY category ORDER BY totalAmount DESC")
    LiveData<List<CategorySum>> getCategoryBreakdown(String monthPrefix);

    @Query("SELECT * FROM expenses WHERE date LIKE :monthPrefix || '%' ORDER BY timestamp DESC")
    List<Expense> getExpensesForMonthSync(String monthPrefix);

    @Query("SELECT * FROM expenses ORDER BY timestamp DESC")
    List<Expense> getAllExpensesSync();
}
