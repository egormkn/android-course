package ru.ifmo.droid2016.worldcam.worldcamdemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Collections;
import java.util.List;

import ru.ifmo.droid2016.worldcam.worldcamdemo.model.Webcam;

/**
 * Created by dmitry.trunin on 03.10.2016.
 */

public class WebcamsRecyclerAdapter
        extends RecyclerView.Adapter<WebcamsRecyclerAdapter.WebcamViewHolder> {

    private final Context context;
    private final LayoutInflater layoutInflater;

    @NonNull
    private List<Webcam> webcams = Collections.emptyList();

    public WebcamsRecyclerAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setWebcams(@NonNull List<Webcam> webcams) {
        this.webcams = webcams;
        notifyDataSetChanged();
    }

    @Override
    public WebcamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return WebcamViewHolder.newInstance(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(WebcamViewHolder holder, int position) {
        final Webcam webcam = webcams.get(position);
        holder.titleView.setText(webcam.title);
        holder.imageView.setImageURI(webcam.imageUrl);
    }

    @Override
    public int getItemCount() {
        return webcams.size();
    }

    static class WebcamViewHolder extends RecyclerView.ViewHolder {

        final SimpleDraweeView imageView;
        final TextView titleView;

        private WebcamViewHolder(View itemView) {
            super(itemView);
            imageView = (SimpleDraweeView) itemView.findViewById(R.id.webcam_image);
            titleView = (TextView) itemView.findViewById(R.id.webcam_title);
        }

        static WebcamViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent) {
            final View view = layoutInflater.inflate(R.layout.item_webcam, parent, false);
            return new WebcamViewHolder(view);
        }
    }
}
