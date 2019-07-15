package ru.ifmo.android_2016.lesson2.loadimagedemo;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.annotation.WorkerThread;
import android.view.View;

/**
 * Простейшая реализация загрузки картинки в фоне с AsyncTask.
 */
public class ExampleActivity3 extends BaseLoadImageActivity {

    @Override
    @UiThread
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);

        new GetImageTask().execute(TEST_IMAGE_URL);
    }

    class GetImageTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        @WorkerThread
        protected Bitmap doInBackground(String... params) {
            final String url = params[0];
            return downloadAndDecodeImage(ExampleActivity3.this, url);
        }

        @Override
        @UiThread
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
            progressBar.setVisibility(View.GONE);
        }
    }
}
