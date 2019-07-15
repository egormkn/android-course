package ru.ifmo.android_2016.lesson2.loadimagedemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.annotation.WorkerThread;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.View;

/**
 * Реализация фоновой загрузки картинки при помощи Loader
 */
public class ExampleActivity6 extends BaseLoadImageActivity
        implements LoaderManager.LoaderCallbacks<Bitmap> {

    @Override
    @UiThread
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);

        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Bitmap> onCreateLoader(int id, Bundle args) {
        return new GetImageLoader(this, TEST_IMAGE_URL);
    }

    @Override
    public void onLoadFinished(Loader loader, Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        imageView.setImageBitmap(null);
        progressBar.setVisibility(View.GONE);
    }


    static class GetImageLoader extends AsyncTaskLoader<Bitmap> {

        private final Context appContext;
        private final String url;

        GetImageLoader(@NonNull Context context,
                       @NonNull String url) {
            super(context);
            this.appContext = context.getApplicationContext();
            this.url = url;
        }

        @Override
        @WorkerThread
        public Bitmap loadInBackground() {
            return downloadAndDecodeImage(appContext, url);
        }

        @Override
        @UiThread
        protected void onStartLoading() {
            forceLoad();
        }
    }
}
