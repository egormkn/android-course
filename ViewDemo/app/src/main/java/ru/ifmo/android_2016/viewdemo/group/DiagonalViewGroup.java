package ru.ifmo.android_2016.viewdemo.group;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by alexey.nikitin on 11.10.16.
 */

public final class DiagonalViewGroup extends ViewGroup {
    public DiagonalViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /* Размеры, доступные для этого контейнера. Мы займём их полностью */
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int childCount = getChildCount();
        if (childCount < 0) {
            return;
        }

        /* Размеры одного дочернего элемента */
        int oneChildWidth = width / childCount;
        int oneChildHeight = height / childCount;

        /* Создаём спецификации для размеров дочерних элементов */
        int oneChildWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                oneChildWidth, MeasureSpec.EXACTLY);
        int oneChildHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                oneChildHeight, MeasureSpec.EXACTLY);

        /* Обмеряем дочерние элементы */
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);

            child.measure(oneChildWidthMeasureSpec, oneChildHeightMeasureSpec);
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        int x = 0, y = 0;
        for (int i = 0; i < count; i++) {
            View childAt = getChildAt(i);

            childAt.layout(x, y,
                    x + childAt.getMeasuredWidth(),
                    y + childAt.getMeasuredHeight());

            x += childAt.getMeasuredWidth();
            y += childAt.getMeasuredHeight();
        }
    }
}
