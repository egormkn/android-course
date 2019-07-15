package ru.ifmo.android_2016.viewdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import ru.ifmo.android_2016.viewdemo.brush.BrushActivity;
import ru.ifmo.android_2016.viewdemo.group.ContainersActivity;

public final class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onRecyclerClick(View view) {
        Log.d(TAG, "onRecyclerClick");
        startActivity(new Intent(this, RecyclerActivity.class));
    }

    public void onBrushClick(View view) {
        Log.d(TAG, "onBrushClick");
        startActivity(new Intent(this, BrushActivity.class));
    }

    public void onContainersClick(View view) {
        Log.d(TAG, "onContainersClick");
        startActivity(new Intent(this, ContainersActivity.class));
    }
}
