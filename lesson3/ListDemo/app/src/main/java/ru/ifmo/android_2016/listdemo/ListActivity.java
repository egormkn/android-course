package ru.ifmo.android_2016.listdemo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexey.nikitin on 01.11.15.
 */
public final class ListActivity extends Activity {
    private static final String TAG = ListActivity.class.getSimpleName();
    private final List<String> items = new ArrayList<>();
    private SimpleRecyclerAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");

        setContentView(R.layout.activity_list);

        for (int i = 0; i < 10000; i++) {
            items.add("Item #" + i);
        }

        recyclerView = (RecyclerView)findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecorator(this));
        adapter = new SimpleRecyclerAdapter(this, items);
        recyclerView.setAdapter(adapter);
    }

    public void addClicked(View view) {
        items.add(0, "Item #" + adapter.getItemCount());
        adapter.notifyDataSetChanged();
    }

    private class SimpleRecyclerAdapter extends RecyclerView.Adapter<SimpleRecyclerAdapter.ViewHolder> {
        private final List<String> items;
        private final LayoutInflater li;

        private SimpleRecyclerAdapter(Context context, List<String> items) {
            li = LayoutInflater.from(context);
            this.items = items;
            setHasStableIds(true);
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @Override
        public SimpleRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new SimpleRecyclerAdapter.ViewHolder(li.inflate(R.layout.item_list, parent, false));
        }

        @Override
        public void onBindViewHolder(SimpleRecyclerAdapter.ViewHolder holder, int position) {
            String str = items.get(position);
            holder.firstLine.setText(str + " first");
            holder.secondLine.setText(str + " second");
        }

        @Override
        public long getItemId(int position) {
            return items.get(position).hashCode();
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView firstLine;
            final TextView secondLine;

            public ViewHolder(View itemView) {
                super(itemView);

                firstLine = (TextView)itemView.findViewById(R.id.fist_line);
                secondLine = (TextView)itemView.findViewById(R.id.second_line);
            }
        }
    }
}
