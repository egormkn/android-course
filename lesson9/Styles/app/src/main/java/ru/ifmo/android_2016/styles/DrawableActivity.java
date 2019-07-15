package ru.ifmo.android_2016.styles;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class DrawableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawable);

        View view = findViewById(R.id.view);
        view.setBackground(new MyDrawable());
    }

    public void onButtonClick(View view) {
        findViewById(R.id.view).setVisibility(View.VISIBLE);
    }

    private class MyDrawable extends Drawable {
        private Paint paintCircle = new Paint();
        private Paint paintRect = new Paint();

        int x, y;
        int dx = 4, dy = 4;

        public MyDrawable() {
            super();

            paintCircle.setColor(Color.RED);
            paintRect.setColor(Color.BLACK);
            paintRect.setStyle(Paint.Style.STROKE);
        }

        @Override
        public void setBounds(int left, int top, int right, int bottom) {
            super.setBounds(left, top, right, bottom);
            x = (left + right) / 2;
            y = (top + bottom) / 2;
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.drawRect(getBounds(), paintRect);
            canvas.drawCircle(x, y, 10, paintCircle);
            step();
        }

        private void step() {
            x += dx;
            y += dy;

            if (x < 0) {
                x = 0;
                dx = -dx;
            }
            if (y < 0) {
                y = 0;
                dy = -dy;
            }
            if (x > getBounds().width()) {
                x = getBounds().width();
                dx = -dx;
            }
            if (y > getBounds().height()) {
                y = getBounds().height();
                dy = -dy;
            }

            invalidateSelf();
        }

        @Override
        public void setAlpha(int alpha) {
        }

        @Override
        public void setColorFilter(ColorFilter colorFilter) {
        }

        @Override
        public int getOpacity() {
            return PixelFormat.OPAQUE;
        }
    }
}
