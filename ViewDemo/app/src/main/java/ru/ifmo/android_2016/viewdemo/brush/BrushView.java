package ru.ifmo.android_2016.viewdemo.brush;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Parcel;
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
    private final Paint paint = new Paint();
    private Figure currentFigure;

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
        Parcelable superState = super.onSaveInstanceState();
        return new BrushState(superState, figures);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);

        if (!(state instanceof BrushState)) {
            super.onRestoreInstanceState(state);
            return;
        }


        BrushState ss = (BrushState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        if (ss.figures != null) {
            figures.addAll(ss.figures);
        }
    }
}

class BrushState extends View.BaseSavedState {
    final List<Figure> figures;

    public BrushState(Parcelable superState, List<Figure> figures) {
        super(superState);
        this.figures = figures;
    }

    public BrushState(Parcel source) {
        super(source);
        this.figures = source.createTypedArrayList(Figure.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeTypedList(figures);
    }

    public static final Creator<BrushState> CREATOR = new Creator<BrushState>() {
        @Override
        public BrushState createFromParcel(Parcel source) {
            return new BrushState(source);
        }

        @Override
        public BrushState[] newArray(int size) {
            return new BrushState[size];
        }
    };
}

class Figure implements Parcelable {
    static final Random r = new Random();

    final List<Point> points;
    final int color;

    Figure() {
        this.color = r.nextInt() | 0xFF000000;
        this.points = new ArrayList<>();
    }

    protected Figure(Parcel in) {
        color = in.readInt();
        points = in.createTypedArrayList(Point.CREATOR);
    }

    void addMotionEvent(MotionEvent ev) {
        points.add(new Point((int)ev.getX(), (int)ev.getY()));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(color);
        dest.writeTypedList(points);
    }

    public static final Creator<Figure> CREATOR = new Creator<Figure>() {
        @Override
        public Figure createFromParcel(Parcel in) {
            return new Figure(in);
        }

        @Override
        public Figure[] newArray(int size) {
            return new Figure[size];
        }
    };
}