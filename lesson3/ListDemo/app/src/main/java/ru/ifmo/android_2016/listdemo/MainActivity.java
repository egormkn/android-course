package ru.ifmo.android_2016.listdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

/**
 * Created by alexey.nikitin on 01.11.15.
 */
public final class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
    }

    public void scrollViewClicked(View view) {
        Log.d(TAG, "scrollViewClicked");
        Intent intent = new Intent(this, ScrollActivity.class);
        startActivity(intent);
    }

    public void recyclerViewClicked(View view) {
        Log.d(TAG, "recyclerViewClicked");
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("key", new MyParc());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        final Parcelable key = savedInstanceState.getParcelable("key");
    }

    static class MyParc implements Parcelable {
        public MyParc() {
        }

        protected MyParc(Parcel in) {
            in.readInt();
        }

        public static final Creator<MyParc> CREATOR = new Creator<MyParc>() {
            @Override
            public MyParc createFromParcel(Parcel in) {
                return new MyParc(in);
            }

            @Override
            public MyParc[] newArray(int size) {
                return new MyParc[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(0);
        }
    }
}
