package com.student.expensetracker.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entity representing student information and balance settings.
 */
@Entity(tableName = "student_profile")
public class StudentProfile {

    @PrimaryKey
    private int id = 1; // Single student user record

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "college")
    private String college;

    @ColumnInfo(name = "course")
    private String course;

    @ColumnInfo(name = "student_id")
    private String studentId;

    @ColumnInfo(name = "initial_bank_balance")
    private double initialBankBalance;

    @ColumnInfo(name = "monthly_budget")
    private double monthlyBudget;

    public StudentProfile(String name, String email, String college, String course, String studentId, double initialBankBalance, double monthlyBudget) {
        this.id = 1;
        this.name = name;
        this.email = email;
        this.college = college;
        this.course = course;
        this.studentId = studentId;
        this.initialBankBalance = initialBankBalance;
        this.monthlyBudget = monthlyBudget;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public double getInitialBankBalance() {
        return initialBankBalance;
    }

    public void setInitialBankBalance(double initialBankBalance) {
        this.initialBankBalance = initialBankBalance;
    }

    public double getMonthlyBudget() {
        return monthlyBudget;
    }

    public void setMonthlyBudget(double monthlyBudget) {
        this.monthlyBudget = monthlyBudget;
    }
}
