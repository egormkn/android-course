package ru.ifmo.android_2016.broadcastservice;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by alexey.nikitin on 25.10.16.
 */

public final class SimpleService extends Service implements Runnable {
    public static final String ACTION_START_FOREGROUND = "start_foreground";
    public static final String ACTION_STOP_FOREGROUND = "stop_foreground";
    public static final String ACTION_STEP_CHANGED = "action_step_changed";
    public static final String EXTRA_STEP = "step";

    private static final String TAG = SimpleService.class.getSimpleName();

    private Thread thread;
    int step;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent != null ? intent.getAction() : null;
        processAction(action);

        return START_STICKY;
    }

    private void processAction(String action) {
        Log.d(TAG, "onStartCommand: " + action);
        if (action == null) {
            return;
        }

        switch (action) {
            case ACTION_START_FOREGROUND:
                Notification.Builder b = new Notification.Builder(this);
                b.setSmallIcon(R.mipmap.ic_launcher);
                b.setContentTitle("Что-то делаю...");
                b.setContentText("Подробное описание важной деятельности");

                startForeground(1, b.build());
                return;
            case ACTION_STOP_FOREGROUND:
                stopForeground(true);
                return;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");

        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        thread.interrupt();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            Log.d(TAG, "step: " + step);

            Intent intent = new Intent(ACTION_STEP_CHANGED);
            intent.putExtra(EXTRA_STEP, step);
            sendBroadcast(intent);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }

            step++;
        }

        Log.d(TAG, "Exit thread");
    }
}
