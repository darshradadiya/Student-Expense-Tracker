package com.student.expensetracker.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.student.expensetracker.R;
import com.student.expensetracker.databinding.FragmentReportsBinding;
import com.student.expensetracker.models.CategorySum;
import com.student.expensetracker.models.StudentProfile;
import com.student.expensetracker.utils.DateTimeUtils;
import com.student.expensetracker.utils.PdfExporter;
import com.student.expensetracker.viewmodels.ExpenseViewModel;
import com.student.expensetracker.viewmodels.ProfileViewModel;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportsFragment extends Fragment {

    private FragmentReportsBinding binding;
    private ExpenseViewModel expenseViewModel;
    private ProfileViewModel profileViewModel;

    private StudentProfile studentProfile;
    private double currentBudget = 12500.0;
    private double currentTotalSpent = 0.0;
    private List<CategorySum> currentCategorySums;

    private static final Map<String, Integer> CATEGORY_COLORS = new HashMap<>();

    static {
        CATEGORY_COLORS.put("Food", Color.parseColor("#FF7043"));
        CATEGORY_COLORS.put("Travel", Color.parseColor("#26A69A"));
        CATEGORY_COLORS.put("Books", Color.parseColor("#43A047"));
        CATEGORY_COLORS.put("Entertainment", Color.parseColor("#AB47BC"));
        CATEGORY_COLORS.put("Shopping", Color.parseColor("#FFA726"));
        CATEGORY_COLORS.put("Other", Color.parseColor("#78909C"));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentReportsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        expenseViewModel = new ViewModelProvider(requireActivity()).get(ExpenseViewModel.class);
        profileViewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);

        binding.tvCurrentMonthBadge.setText(DateTimeUtils.getCurrentMonthDisplayName());

        setupListeners();
        observeData();
    }

    private void setupListeners() {
        binding.btnExportPdf.setOnClickListener(v -> exportMonthlyPdf());
    }

    private void observeData() {
        // Observe Profile for Monthly Budget
        profileViewModel.getStudentProfile().observe(getViewLifecycleOwner(), profile -> {
            if (profile != null) {
                studentProfile = profile;
                currentBudget = profile.getMonthlyBudget();
                binding.tvSummaryBudget.setText(DateTimeUtils.formatCurrency(currentBudget));
                updateFinancialSummary();
            }
        });

        // Observe Total Spent This Month
        expenseViewModel.getTotalExpensesThisMonth().observe(getViewLifecycleOwner(), total -> {
            currentTotalSpent = total != null ? total : 0.0;
            binding.tvSummarySpent.setText(DateTimeUtils.formatCurrency(currentTotalSpent));
            updateFinancialSummary();
        });

        // Observe Category Breakdown for Donut Chart & Bar Chart
        expenseViewModel.getCategoryBreakdown().observe(getViewLifecycleOwner(), categorySums -> {
            currentCategorySums = categorySums;
            binding.donutChartView.setData(categorySums);
            binding.barChartView.setData(categorySums);
            populateCategoryBreakdownList(categorySums);
        });
    }

    private void updateFinancialSummary() {
        // Formula: Savings = Budget - Total Expenses
        double savings = currentBudget - currentTotalSpent;
        double remaining = Math.max(0, savings);

        binding.tvSummaryRemaining.setText(DateTimeUtils.formatCurrency(remaining));
        binding.tvSummarySaved.setText(DateTimeUtils.formatCurrency(savings));

        if (currentBudget > 0) {
            int percent = (int) Math.round((currentTotalSpent / currentBudget) * 100);
            binding.pbBudgetUsage.setProgress(Math.min(100, percent));

            if (percent >= 100) {
                binding.pbBudgetUsage.setProgressTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#D32F2F")));
                binding.tvBudgetUsagePercent.setText("Budget Exceeded (" + percent + "% used) • Overspent by " + DateTimeUtils.formatCurrency(currentTotalSpent - currentBudget));
                binding.tvBudgetUsagePercent.setTextColor(Color.parseColor("#D32F2F"));
            } else if (percent >= 80) {
                binding.pbBudgetUsage.setProgressTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#FF9800")));
                binding.tvBudgetUsagePercent.setText("Warning: " + percent + "% of budget used • Savings: " + DateTimeUtils.formatCurrency(savings));
                binding.tvBudgetUsagePercent.setTextColor(Color.parseColor("#E65100"));
            } else {
                binding.pbBudgetUsage.setProgressTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#2E7D32")));
                binding.tvBudgetUsagePercent.setText("Formula: Savings = Budget − Total Expenses (" + percent + "% used)");
                binding.tvBudgetUsagePercent.setTextColor(Color.parseColor("#757575"));
            }
        }
    }

    private void populateCategoryBreakdownList(List<CategorySum> list) {
        binding.layoutCategoryBreakdown.removeAllViews();
        if (list == null || list.isEmpty() || currentTotalSpent <= 0) {
            TextView emptyTv = new TextView(requireContext());
            emptyTv.setText("No category expenses recorded for this month.");
            emptyTv.setTextColor(Color.parseColor("#9E9E9E"));
            emptyTv.setTextSize(13);
            emptyTv.setPadding(0, 16, 0, 16);
            binding.layoutCategoryBreakdown.addView(emptyTv);
            return;
        }

        for (CategorySum item : list) {
            double percent = (item.totalAmount / currentTotalSpent) * 100.0;

            LinearLayout row = new LinearLayout(requireContext());
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setGravity(Gravity.CENTER_VERTICAL);
            row.setPadding(0, 10, 0, 10);

            // Colored Dot
            FrameLayout dot = new FrameLayout(requireContext());
            int dotSize = (int) (12 * getResources().getDisplayMetrics().density);
            LinearLayout.LayoutParams dotParams = new LinearLayout.LayoutParams(dotSize, dotSize);
            dotParams.setMarginEnd((int) (12 * getResources().getDisplayMetrics().density));
            dot.setLayoutParams(dotParams);
            android.graphics.drawable.GradientDrawable gd = new android.graphics.drawable.GradientDrawable();
            gd.setShape(android.graphics.drawable.GradientDrawable.OVAL);
            Integer color = CATEGORY_COLORS.get(item.category);
            gd.setColor(color != null ? color : Color.GRAY);
            dot.setBackground(gd);
            row.addView(dot);

            // Category Name & Percentage
            TextView tvName = new TextView(requireContext());
            LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
            tvName.setLayoutParams(nameParams);
            tvName.setText(item.category + " (" + String.format("%.1f", percent) + "%)");
            tvName.setTextColor(Color.parseColor("#333333"));
            tvName.setTextSize(14);
            tvName.setTypeface(null, android.graphics.Typeface.BOLD);
            row.addView(tvName);

            // Total Amount
            TextView tvAmount = new TextView(requireContext());
            tvAmount.setText(DateTimeUtils.formatCurrency(item.totalAmount));
            tvAmount.setTextColor(Color.parseColor("#666666"));
            tvAmount.setTextSize(14);
            row.addView(tvAmount);

            binding.layoutCategoryBreakdown.addView(row);
        }
    }

    private void exportMonthlyPdf() {
        Toast.makeText(requireContext(), "Generating Monthly Report PDF...", Toast.LENGTH_SHORT).show();

        expenseViewModel.getExpensesForMonthSync(expenses -> {
            if (getActivity() == null) return;
            getActivity().runOnUiThread(() -> {
                File pdfFile = PdfExporter.generateMonthlyReport(
                        requireContext(),
                        studentProfile,
                        expenses,
                        currentTotalSpent,
                        currentBudget
                );

                if (pdfFile != null) {
                    Toast.makeText(requireContext(), "PDF Exported: " + pdfFile.getName(), Toast.LENGTH_SHORT).show();
                    PdfExporter.openPdfFile(requireContext(), pdfFile);
                } else {
                    Toast.makeText(requireContext(), "Failed to generate PDF report", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
