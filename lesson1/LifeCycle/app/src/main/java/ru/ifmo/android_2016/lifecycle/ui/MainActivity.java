package ru.ifmo.android_2016.lifecycle.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import ru.ifmo.android_2016.lifecycle.R;

public final class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int MAX_CLICK_COUNT = 3;
    private static final String EXTRA_CLICK_COUNT = "clicks";

    private TextView error;
    private View loginButton;

    int clickCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate");

        error = (TextView)findViewById(R.id.error);
        loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCount++;
                updateUI();
            }
        });

        if (savedInstanceState != null) {
            clickCount = savedInstanceState.getInt(EXTRA_CLICK_COUNT);
            updateUI();
        }
    }

    private void updateUI() {
        boolean disabled = clickCount >= MAX_CLICK_COUNT;
        if (disabled) {
            error.setVisibility(View.VISIBLE);
            error.setText(MAX_CLICK_COUNT + " reached");
        }
        else {
            error.setVisibility(View.INVISIBLE);
        }

        loginButton.setEnabled(!disabled);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.d(TAG, "onSaveInstanceState");

        outState.putInt(EXTRA_CLICK_COUNT, clickCount);
    }
}
