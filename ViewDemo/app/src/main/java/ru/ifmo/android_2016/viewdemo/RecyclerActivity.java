package ru.ifmo.android_2016.viewdemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RecyclerActivity extends Activity {
    private static final String TAG = RecyclerActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RecyclerView.Adapter<VH>() {
            @Override
            public VH onCreateViewHolder(ViewGroup parent, int viewType) {
                TextView tv = new TextView(parent.getContext());
                VH vh = new VH(tv);
                Log.d(TAG, "Create #" + vh.order);
                return vh;
            }

            @Override
            public void onBindViewHolder(VH holder, int position) {
                Log.d(TAG, "Use #" + holder.order + ", position " + position);
                ((TextView)holder.itemView).setText("Item #" + position);
            }

            @Override
            public int getItemCount() {
                return 10_000;
            }

            @Override
            public void onViewRecycled(VH holder) {
                super.onViewRecycled(holder);
                Log.d(TAG, "Recycle #" + holder.order);
            }
        });
    }

    static int holdersCount;

    static class VH extends RecyclerView.ViewHolder {
        final int order = holdersCount++;

        public VH(View itemView) {
            super(itemView);
        }
    }
}
