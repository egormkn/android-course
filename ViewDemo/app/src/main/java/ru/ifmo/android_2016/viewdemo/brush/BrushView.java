package ru.ifmo.android_2016.viewdemo.brush;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by alexey.nikitin on 11.10.16.
 */

public final class BrushView extends View {
    private static final String TAG = BrushView.class.getSimpleName();

    private final List<Figure> figures = new ArrayList<>();
    private Figure currentFigure;
    private Paint paint = new Paint();

    public BrushView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint.setStrokeWidth(
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, context.getResources().getDisplayMetrics()));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "ACTION_DOWN");
                currentFigure = new Figure();
                figures.add(currentFigure);

                currentFigure.addMotionEvent(event);
                return true;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "ACTION_MOVE");
                if (currentFigure != null) {
                    currentFigure.addMotionEvent(event);
                    invalidate();
                }
                return true;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "ACTION_UP");
                if (currentFigure != null) {
                    currentFigure.addMotionEvent(event);
                    currentFigure = null;
                    invalidate();
                }
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (Figure figure : figures) {
            paint.setColor(figure.color);

            for (int i = 1; i < figure.points.size(); i++) {
                Point prev = figure.points.get(i - 1);
                Point next = figure.points.get(i);
                canvas.drawLine(prev.x, prev.y, next.x, next.y, paint);
            }
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }
}

class Figure {
    static final Random r = new Random();

    final List<Point> points = new ArrayList<>();
    final int color;

    Figure() {
        this.color = r.nextInt() | 0xFF000000;
    }

    void addMotionEvent(MotionEvent ev) {
        points.add(new Point((int)ev.getX(), (int)ev.getY()));
    }
}
