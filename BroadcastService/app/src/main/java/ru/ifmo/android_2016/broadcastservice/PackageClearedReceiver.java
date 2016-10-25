package ru.ifmo.android_2016.broadcastservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.MainThread;
import android.util.Log;

/**
 * Created by alexey.nikitin on 25.10.16.
 */
public final class PackageClearedReceiver extends BroadcastReceiver {
    private static final String TAG = PackageClearedReceiver.class.getSimpleName();

    @MainThread
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: " + intent.getAction());
    }
}
