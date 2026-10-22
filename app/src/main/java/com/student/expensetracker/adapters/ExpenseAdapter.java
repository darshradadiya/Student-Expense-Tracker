package com.student.expensetracker.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.student.expensetracker.R;
import com.student.expensetracker.databinding.ItemExpenseBinding;
import com.student.expensetracker.models.Expense;
import com.student.expensetracker.utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    public interface OnExpenseActionListener {
        void onEdit(Expense expense);
        void onDelete(Expense expense);
        void onItemClick(Expense expense);
    }

    private List<Expense> expenses = new ArrayList<>();
    private final OnExpenseActionListener listener;

    public ExpenseAdapter(OnExpenseActionListener listener) {
        this.listener = listener;
    }

    public void setExpenses(List<Expense> newExpenses) {
        final List<Expense> oldExpenses = this.expenses;
        this.expenses = newExpenses != null ? newExpenses : new ArrayList<>();

        DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return oldExpenses.size();
            }

            @Override
            public int getNewListSize() {
                return expenses.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return oldExpenses.get(oldItemPosition).getId() == expenses.get(newItemPosition).getId();
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                Expense oldItem = oldExpenses.get(oldItemPosition);
                Expense newItem = expenses.get(newItemPosition);
                return oldItem.getTitle().equals(newItem.getTitle())
                        && oldItem.getAmount() == newItem.getAmount()
                        && oldItem.getCategory().equals(newItem.getCategory())
                        && oldItem.getDate().equals(newItem.getDate());
            }
        }).dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemExpenseBinding binding = ItemExpenseBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ExpenseViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        holder.bind(expenses.get(position));
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    class ExpenseViewHolder extends RecyclerView.ViewHolder {

        private final ItemExpenseBinding binding;

        ExpenseViewHolder(ItemExpenseBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Expense expense) {
            binding.tvExpenseTitle.setText(expense.getTitle());
            binding.tvExpenseAmount.setText("- " + DateTimeUtils.formatCurrency(expense.getAmount()));
            binding.tvExpenseDateCategory.setText(
                    DateTimeUtils.formatDateForDisplay(expense.getDate()) + " • " + expense.getCategory());

            if (expense.getNotes() != null && !expense.getNotes().trim().isEmpty()) {
                binding.tvExpenseNotes.setVisibility(View.VISIBLE);
                binding.tvExpenseNotes.setText(expense.getNotes());
            } else {
                binding.tvExpenseNotes.setVisibility(View.GONE);
            }

            // Category iconography and themed badge colors
            applyCategoryStyle(expense.getCategory());

            binding.btnEdit.setOnClickListener(v -> {
                if (listener != null) listener.onEdit(expense);
            });

            binding.btnDelete.setOnClickListener(v -> {
                if (listener != null) listener.onDelete(expense);
            });

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) listener.onItemClick(expense);
            });
        }

        private void applyCategoryStyle(String category) {
            if (category == null) category = "Other";
            int iconRes;
            int bgRes;
            int iconColor;

            switch (category) {
                case "Food":
                    iconRes = R.drawable.ic_food;
                    bgRes = R.drawable.bg_badge_food;
                    iconColor = Color.parseColor("#FF7043");
                    break;
                case "Travel":
                    iconRes = R.drawable.ic_travel;
                    bgRes = R.drawable.bg_badge_travel;
                    iconColor = Color.parseColor("#26A69A");
                    break;
                case "Books":
                    iconRes = R.drawable.ic_books;
                    bgRes = R.drawable.bg_badge_books;
                    iconColor = Color.parseColor("#43A047");
                    break;
                case "Entertainment":
                    iconRes = R.drawable.ic_entertainment;
                    bgRes = R.drawable.bg_badge_entertainment;
                    iconColor = Color.parseColor("#AB47BC");
                    break;
                case "Shopping":
                    iconRes = R.drawable.ic_shopping;
                    bgRes = R.drawable.bg_badge_shopping;
                    iconColor = Color.parseColor("#FFA726");
                    break;
                default:
                    iconRes = R.drawable.ic_other;
                    bgRes = R.drawable.bg_badge_other;
                    iconColor = Color.parseColor("#78909C");
                    break;
            }

            binding.ivCategoryIcon.setImageResource(iconRes);
            binding.iconContainer.setBackgroundResource(bgRes);
            binding.ivCategoryIcon.setColorFilter(iconColor);
        }
    }
}
