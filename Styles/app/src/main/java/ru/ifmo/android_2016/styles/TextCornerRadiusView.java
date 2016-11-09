package ru.ifmo.android_2016.styles;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by alexey.nikitin on 08.11.16.
 */
public class TextCornerRadiusView extends TextView {
    public TextCornerRadiusView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(
                attrs,
                R.styleable.TextCornerRadiusView,
                0, 0);

        int radius = a.getDimensionPixelSize(R.styleable.TextCornerRadiusView_radius, 0);
        int color = a.getColor(R.styleable.TextCornerRadiusView_strokeColor, 0);

        a.recycle();

        RoundRectShape shape = new RoundRectShape(new float [] {radius, radius, radius, radius, radius, radius, radius, radius}, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(shape);
        shapeDrawable.getPaint().setColor(color);
        shapeDrawable.getPaint().setStrokeWidth(10);
        shapeDrawable.getPaint().setStyle(Paint.Style.STROKE);

        setBackground(new InsetDrawable(shapeDrawable, radius / 2, radius / 2, radius / 2, radius / 2));
    }
}
