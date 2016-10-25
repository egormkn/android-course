package ru.ifmo.android_2016.broadcastservice;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by alexey.nikitin on 25.10.16.
 */

public final class ServiceActivity extends Activity {
    private static final String TAG = ServiceActivity.class.getSimpleName();

    private TextView countText;
    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_service);
        countText = (TextView) findViewById(R.id.count);

        registerReceiver(receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                countText.setText("Step: " + intent.getIntExtra(SimpleService.EXTRA_STEP, 0));
            }
        }, new IntentFilter(SimpleService.ACTION_STEP_CHANGED));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    public void onStartClick(View view) {
        Log.d(TAG, "onStartClick");
        startService(new Intent(this, SimpleService.class));
    }

    public void onStopClick(View view) {
        Log.d(TAG, "onStopClick");
        stopService(new Intent(this, SimpleService.class));
    }

    public void onStartForeground(View view) {
        Log.d(TAG, "onStartForeground");
        Intent intent = new Intent(this, SimpleService.class);
        intent.setAction(SimpleService.ACTION_START_FOREGROUND);
        startService(intent);
    }

    public void onStopForeground(View view) {
        Log.d(TAG, "onStopForeground");
        Intent intent = new Intent(this, SimpleService.class);
        intent.setAction(SimpleService.ACTION_STOP_FOREGROUND);
        startService(intent);
    }
}
