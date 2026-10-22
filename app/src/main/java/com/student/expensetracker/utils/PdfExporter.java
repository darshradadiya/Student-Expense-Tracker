package com.student.expensetracker.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.student.expensetracker.models.Expense;
import com.student.expensetracker.models.StudentProfile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Native PDF generator for exporting Monthly Student Expense Reports.
 */
public class PdfExporter {

    public static File generateMonthlyReport(Context context, StudentProfile profile, List<Expense> expenses, double totalSpent, double budget) {
        PdfDocument document = new PdfDocument();
        int pageWidth = 595;  // Standard A4 width in points
        int pageHeight = 842; // Standard A4 height in points

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        // 1. Header Banner (Green)
        paint.setColor(Color.parseColor("#1B5E20"));
        canvas.drawRect(0, 0, pageWidth, 85, paint);

        // Header Title
        paint.setColor(Color.WHITE);
        paint.setTextSize(22);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("STUDENT EXPENSE TRACKER", 30, 42, paint);

        paint.setTextSize(12);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText("Monthly Financial Report • " + DateTimeUtils.getCurrentMonthDisplayName(), 30, 64, paint);

        // 2. Student Info Card
        paint.setColor(Color.parseColor("#F5F7FA"));
        canvas.drawRoundRect(25, 100, pageWidth - 25, 175, 12, 12, paint);

        paint.setColor(Color.parseColor("#2E7D32"));
        paint.setTextSize(13);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("STUDENT DETAILS", 40, 122, paint);

        paint.setColor(Color.parseColor("#212121"));
        paint.setTextSize(11);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        String name = profile != null ? profile.getName() : "Rahul Patel";
        String id = profile != null ? profile.getStudentId() : "STU001";
        String college = profile != null ? profile.getCollege() : "ABC College";
        String course = profile != null ? profile.getCourse() : "BCA";

        canvas.drawText("Name: " + name + "  (ID: " + id + ")", 40, 142, paint);
        canvas.drawText("Course: " + course + "  |  College: " + college, 40, 160, paint);

        // 3. Financial Summary Box
        paint.setColor(Color.parseColor("#FFF3E0"));
        canvas.drawRoundRect(25, 190, pageWidth - 25, 255, 12, 12, paint);

        paint.setColor(Color.parseColor("#E65100"));
        paint.setTextSize(12);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("FINANCIAL SUMMARY", 40, 210, paint);

        paint.setColor(Color.parseColor("#212121"));
        paint.setTextSize(10);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

        double savings = budget - totalSpent;
        canvas.drawText("Monthly Budget: " + DateTimeUtils.formatCurrency(budget), 40, 230, paint);
        canvas.drawText("Total Spent: " + DateTimeUtils.formatCurrency(totalSpent), 200, 230, paint);
        canvas.drawText("Total Saved: " + DateTimeUtils.formatCurrency(Math.max(0, savings)), 380, 230, paint);

        // 4. Expenses Table Header
        int y = 285;
        paint.setColor(Color.parseColor("#2E7D32"));
        canvas.drawRect(25, y - 18, pageWidth - 25, y + 10, paint);

        paint.setColor(Color.WHITE);
        paint.setTextSize(11);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("#", 35, y, paint);
        canvas.drawText("Date", 65, y, paint);
        canvas.drawText("Title / Note", 155, y, paint);
        canvas.drawText("Category", 370, y, paint);
        canvas.drawText("Amount (₹)", 480, y, paint);

        // 5. Table Rows
        y += 28;
        paint.setTextSize(10);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

        int index = 1;
        if (expenses != null && !expenses.isEmpty()) {
            for (Expense exp : expenses) {
                if (y > pageHeight - 60) break; // Keep within single page or standard layout

                // Alternating row background
                if (index % 2 == 0) {
                    paint.setColor(Color.parseColor("#FAFAFA"));
                    canvas.drawRect(25, y - 14, pageWidth - 25, y + 8, paint);
                }

                paint.setColor(Color.parseColor("#212121"));
                canvas.drawText(String.valueOf(index++), 35, y, paint);
                canvas.drawText(DateTimeUtils.formatDateForDisplay(exp.getDate()), 65, y, paint);

                String title = exp.getTitle();
                if (title.length() > 28) title = title.substring(0, 25) + "...";
                canvas.drawText(title, 155, y, paint);

                canvas.drawText(exp.getCategory(), 370, y, paint);

                paint.setColor(Color.parseColor("#D32F2F"));
                canvas.drawText(DateTimeUtils.formatCurrency(exp.getAmount()), 480, y, paint);

                // Row bottom divider
                paint.setColor(Color.parseColor("#EEEEEE"));
                canvas.drawLine(25, y + 8, pageWidth - 25, y + 8, paint);

                y += 24;
            }
        } else {
            paint.setColor(Color.GRAY);
            canvas.drawText("No expenses recorded for this month.", 40, y + 10, paint);
            y += 30;
        }

        // 6. Total Row
        y += 8;
        paint.setColor(Color.parseColor("#E8F5E9"));
        canvas.drawRect(25, y - 14, pageWidth - 25, y + 14, paint);

        paint.setColor(Color.parseColor("#1B5E20"));
        paint.setTextSize(11);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("TOTAL EXPENSES", 40, y, paint);
        canvas.drawText(DateTimeUtils.formatCurrency(totalSpent), 480, y, paint);

        // 7. Footer
        paint.setColor(Color.parseColor("#9E9E9E"));
        paint.setTextSize(9);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
        canvas.drawText("Generated by Student Expense Tracker • Mobile Application Development (MAD) Project", 80, pageHeight - 25, paint);

        document.finishPage(page);

        // Save file
        File reportsDir = new File(context.getExternalFilesDir(null), "Reports");
        if (!reportsDir.exists()) {
            reportsDir.mkdirs();
        }

        String fileName = "Student_Expense_Report_" + System.currentTimeMillis() + ".pdf";
        File file = new File(reportsDir, fileName);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            document.writeTo(fos);
            document.close();
            fos.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            document.close();
            return null;
        }
    }

    public static void openPdfFile(Context context, File file) {
        if (file == null || !file.exists()) {
            Toast.makeText(context, "Error: PDF file not found", Toast.LENGTH_SHORT).show();
            return;
        }

        Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            context.startActivity(Intent.createChooser(intent, "Open Monthly Report PDF"));
        } catch (Exception e) {
            Toast.makeText(context, "Report saved to: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        }
    }
}
