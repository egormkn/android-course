package ru.ifmo.android_2016.broadcastservice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by alexey.nikitin on 25.10.16.
 */

public final class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
    }

    public void onNetClick(View view) {
        startActivity(new Intent(this, ConnectivityActivity.class));
    }

    public void onServiceClick(View view) {
        startActivity(new Intent(this, ServiceActivity.class));
    }
}
