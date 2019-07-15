package ru.ifmo.android_2016.styles;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void on9PngClick(View view) {
        startActivity(new Intent(this, NinePatchActivity.class));
    }

    public void onDrawableClick(View view) {
        startActivity(new Intent(this, DrawableActivity.class));
    }

    public void onAttrsClick(View view) {
        startActivity(new Intent(this, AttrsActivity.class));
    }

    public void onThemeClick(View view) {
        startActivity(new Intent(this, ThemeActivity.class));
    }
}
