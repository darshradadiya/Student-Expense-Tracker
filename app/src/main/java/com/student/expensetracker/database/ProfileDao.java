package com.student.expensetracker.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.student.expensetracker.models.StudentProfile;

@Dao
public interface ProfileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(StudentProfile profile);

    @Update
    void update(StudentProfile profile);

    @Query("SELECT * FROM student_profile WHERE id = 1 LIMIT 1")
    LiveData<StudentProfile> getProfile();

    @Query("SELECT * FROM student_profile WHERE id = 1 LIMIT 1")
    StudentProfile getProfileSync();

    @Query("UPDATE student_profile SET initial_bank_balance = :bankBalance, monthly_budget = :monthlyBudget WHERE id = 1")
    void updateBalances(double bankBalance, double monthlyBudget);
}
