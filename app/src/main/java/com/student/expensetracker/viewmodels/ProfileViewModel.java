package com.student.expensetracker.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.student.expensetracker.models.StudentProfile;
import com.student.expensetracker.repository.ExpenseRepository;

public class ProfileViewModel extends AndroidViewModel {

    private final ExpenseRepository repository;
    private final LiveData<StudentProfile> studentProfile;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        repository = new ExpenseRepository(application);
        studentProfile = repository.getStudentProfile();
    }

    public LiveData<StudentProfile> getStudentProfile() {
        return studentProfile;
    }

    public void updateBalances(double initialBankBalance, double monthlyBudget) {
        repository.updateBalances(initialBankBalance, monthlyBudget);
    }

    public void updateProfile(StudentProfile profile) {
        repository.updateStudentProfile(profile);
    }

    public void resetToSampleData() {
        repository.resetToSampleData();
    }
}
