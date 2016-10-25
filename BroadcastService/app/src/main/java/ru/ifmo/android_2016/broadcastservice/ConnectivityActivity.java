package ru.ifmo.android_2016.broadcastservice;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by alexey.nikitin on 25.10.16.
 */
public final class ConnectivityActivity extends Activity {
    private static final String TAG = ConnectivityActivity.class.getSimpleName();

    private NetworkListener receiver;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_connectivity);
        text = (TextView) findViewById(R.id.text);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (receiver == null) {
            receiver = new NetworkListener();
            registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
    }

    class NetworkListener extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            if (noConnectivity) {
                text.setText("Нет сети");
                Log.d(TAG, "No connection");
                return;
            }

            // 0 - мобильная сеть, 1 - Wi-Fi. Смотри константы ConnectivityManager.TYPE_*
            int networkType =
                    intent.getIntExtra(ConnectivityManager.EXTRA_NETWORK_TYPE, 0);
            Log.d(TAG, "Network type: " + networkType);
            text.setText(networkType == ConnectivityManager.TYPE_MOBILE ? "Мобильная" : "Wi-Fi");
        }
    }
}
