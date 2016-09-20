package ru.ifmo.android_2016.lesson2.loadimagedemo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.example_1).setOnClickListener(this);
        findViewById(R.id.example_2).setOnClickListener(this);
        findViewById(R.id.example_3).setOnClickListener(this);
        findViewById(R.id.example_4).setOnClickListener(this);
        findViewById(R.id.example_5).setOnClickListener(this);
        findViewById(R.id.example_6).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final Class<? extends Activity> activityClass;

        switch (v.getId()) {
            case R.id.example_1: activityClass = ExampleActivity1.class; break;
            case R.id.example_2: activityClass = ExampleActivity2.class; break;
            case R.id.example_3: activityClass = ExampleActivity3.class; break;
            case R.id.example_4: activityClass = ExampleActivity4.class; break;
            case R.id.example_5: activityClass = ExampleActivity5.class; break;
            case R.id.example_6: activityClass = ExampleActivity6.class; break;
            default: activityClass = null;
        }
        if (activityClass != null) {
            startActivity(new Intent(this, activityClass));
        }
    }
}
