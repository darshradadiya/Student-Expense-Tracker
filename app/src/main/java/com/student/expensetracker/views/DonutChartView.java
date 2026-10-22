package com.student.expensetracker.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.student.expensetracker.models.CategorySum;
import com.student.expensetracker.utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Custom Donut Chart View to visually display category-wise student expenses.
 */
public class DonutChartView extends View {

    private final Paint arcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint subTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF arcRect = new RectF();

    private List<CategorySum> data = new ArrayList<>();
    private double totalAmount = 0.0;

    private static final Map<String, Integer> CATEGORY_COLORS = new HashMap<>();

    static {
        CATEGORY_COLORS.put("Food", Color.parseColor("#FF7043"));
        CATEGORY_COLORS.put("Travel", Color.parseColor("#26A69A"));
        CATEGORY_COLORS.put("Books", Color.parseColor("#43A047"));
        CATEGORY_COLORS.put("Entertainment", Color.parseColor("#AB47BC"));
        CATEGORY_COLORS.put("Shopping", Color.parseColor("#FFA726"));
        CATEGORY_COLORS.put("Other", Color.parseColor("#78909C"));
    }

    public DonutChartView(Context context) {
        super(context);
        init();
    }

    public DonutChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DonutChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setStrokeCap(Paint.Cap.ROUND);

        textPaint.setColor(Color.parseColor("#212121"));
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setFakeBoldText(true);

        subTextPaint.setColor(Color.parseColor("#757575"));
        subTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    public void setData(List<CategorySum> categorySums) {
        this.data = categorySums != null ? categorySums : new ArrayList<>();
        this.totalAmount = 0.0;
        for (CategorySum item : this.data) {
            this.totalAmount += item.totalAmount;
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        if (width == 0 || height == 0) return;

        int size = Math.min(width, height);
        float strokeWidth = size * 0.14f;
        arcPaint.setStrokeWidth(strokeWidth);

        float padding = strokeWidth / 2f + 16f;
        float centerX = width / 2f;
        float centerY = height / 2f;
        arcRect.set(centerX - (size / 2f) + padding,
                    centerY - (size / 2f) + padding,
                    centerX + (size / 2f) - padding,
                    centerY + (size / 2f) - padding);

        if (totalAmount <= 0 || data.isEmpty()) {
            // Draw empty gray ring
            arcPaint.setColor(Color.parseColor("#E0E0E0"));
            canvas.drawArc(arcRect, 0, 360, false, arcPaint);

            textPaint.setTextSize(size * 0.09f);
            canvas.drawText("₹0", centerX, centerY, textPaint);

            subTextPaint.setTextSize(size * 0.05f);
            canvas.drawText("No Expenses", centerX, centerY + (size * 0.08f), subTextPaint);
            return;
        }

        float startAngle = -90f;
        for (CategorySum item : data) {
            if (item.totalAmount <= 0) continue;

            float sweepAngle = (float) ((item.totalAmount / totalAmount) * 360f);
            Integer color = CATEGORY_COLORS.get(item.category);
            if (color == null) color = Color.parseColor("#78909C");

            arcPaint.setColor(color);
            // Draw slice with slight gap if multiple
            float drawSweep = data.size() > 1 ? Math.max(1f, sweepAngle - 3f) : sweepAngle;
            canvas.drawArc(arcRect, startAngle, drawSweep, false, arcPaint);
            startAngle += sweepAngle;
        }

        // Center Total Text
        textPaint.setTextSize(size * 0.10f);
        canvas.drawText(DateTimeUtils.formatCurrencyNoDecimals(totalAmount), centerX, centerY, textPaint);

        subTextPaint.setTextSize(size * 0.05f);
        canvas.drawText("Total Spent", centerX, centerY + (size * 0.08f), subTextPaint);
    }
}
