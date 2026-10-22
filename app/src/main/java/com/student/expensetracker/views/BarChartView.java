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
 * Custom horizontal bar chart view to show category-wise expense comparison.
 */
public class BarChartView extends View {

    private final Paint barPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint bgBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint amountPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF rectF = new RectF();

    private List<CategorySum> data = new ArrayList<>();
    private double maxAmount = 1.0;

    private static final Map<String, Integer> CATEGORY_COLORS = new HashMap<>();

    static {
        CATEGORY_COLORS.put("Food", Color.parseColor("#FF7043"));
        CATEGORY_COLORS.put("Travel", Color.parseColor("#26A69A"));
        CATEGORY_COLORS.put("Books", Color.parseColor("#43A047"));
        CATEGORY_COLORS.put("Entertainment", Color.parseColor("#AB47BC"));
        CATEGORY_COLORS.put("Shopping", Color.parseColor("#FFA726"));
        CATEGORY_COLORS.put("Other", Color.parseColor("#78909C"));
    }

    public BarChartView(Context context) {
        super(context);
        init();
    }

    public BarChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BarChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        bgBarPaint.setColor(Color.parseColor("#F0F0F0"));
        bgBarPaint.setStyle(Paint.Style.FILL);

        textPaint.setColor(Color.parseColor("#333333"));
        textPaint.setTextSize(34f);
        textPaint.setFakeBoldText(true);

        amountPaint.setColor(Color.parseColor("#666666"));
        amountPaint.setTextSize(30f);
        amountPaint.setTextAlign(Paint.Align.RIGHT);
    }

    public void setData(List<CategorySum> categorySums) {
        this.data = categorySums != null ? categorySums : new ArrayList<>();
        this.maxAmount = 1.0;
        for (CategorySum item : this.data) {
            if (item.totalAmount > maxAmount) {
                maxAmount = item.totalAmount;
            }
        }
        requestLayout();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int rows = Math.max(1, data.size());
        int rowHeight = 110; // Height per category row
        int height = rows * rowHeight + 40;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (data.isEmpty()) {
            textPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("No expenses to display in chart", getWidth() / 2f, 60, textPaint);
            textPaint.setTextAlign(Paint.Align.LEFT);
            return;
        }

        int width = getWidth();
        int leftMargin = 30;
        int rightMargin = 30;
        float barLeft = leftMargin;
        float barRightMax = width - rightMargin;
        float availableBarWidth = barRightMax - barLeft;

        float currentY = 50f;
        float barHeight = 26f;

        for (CategorySum item : data) {
            // 1. Draw Category Name on Left, Amount on Right
            textPaint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText(item.category, leftMargin, currentY, textPaint);

            canvas.drawText(DateTimeUtils.formatCurrency(item.totalAmount), barRightMax, currentY, amountPaint);

            // 2. Draw background track bar
            float barTop = currentY + 16f;
            float barBottom = barTop + barHeight;
            rectF.set(barLeft, barTop, barRightMax, barBottom);
            canvas.drawRoundRect(rectF, 13f, 13f, bgBarPaint);

            // 3. Draw filled bar proportional to maxAmount
            float fillRatio = (float) (item.totalAmount / maxAmount);
            float filledWidth = Math.max(barHeight, availableBarWidth * fillRatio);

            Integer color = CATEGORY_COLORS.get(item.category);
            if (color == null) color = Color.parseColor("#78909C");
            barPaint.setColor(color);

            rectF.set(barLeft, barTop, barLeft + filledWidth, barBottom);
            canvas.drawRoundRect(rectF, 13f, 13f, barPaint);

            currentY += 105f;
        }
    }
}
