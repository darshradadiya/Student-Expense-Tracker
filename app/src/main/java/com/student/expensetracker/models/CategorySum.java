package com.student.expensetracker.models;

import androidx.room.ColumnInfo;

/**
 * Data holder class for category spending aggregation queries.
 */
public class CategorySum {

    @ColumnInfo(name = "category")
    public String category;

    @ColumnInfo(name = "totalAmount")
    public double totalAmount;

    public CategorySum(String category, double totalAmount) {
        this.category = category;
        this.totalAmount = totalAmount;
    }
}
