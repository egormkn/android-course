package ru.ifmo.android_2016.lesson2.loadimagedemo;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.annotation.WorkerThread;
import android.view.View;

/**
 * Простейшая реализация загрузки картинки в фоне с AsyncTask + отображение прогресса
 */
public class ExampleActivity4 extends BaseLoadImageActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(false);
        progressBar.setProgress(0);
        progressBar.setMax(100);

        new GetImageTask().execute(TEST_IMAGE_URL);
    }

    class GetImageTask extends AsyncTask<String, Integer, Bitmap> implements ProgressCallback {

        @Override
        @WorkerThread
        protected Bitmap doInBackground(String... params) {
            final String url = params[0];
            return downloadAndDecodeImage(ExampleActivity4.this, url, this /*progressCallback*/);
        }

        // Метод ProgressCallback, вызывается в фоновом потоке из downloadFile
        @Override
        @WorkerThread
        public void onProgressChanged(int progress) {
            publishProgress(progress);
        }

        @Override
        @UiThread
        protected void onProgressUpdate(Integer... values) {
            if (values.length > 0) {
                int progress = values[values.length - 1];
                progressBar.setProgress(progress);
            }
        }

        @Override
        @UiThread
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
            progressBar.setVisibility(View.GONE);
        }
    }
}
