package ru.ifmo.android_2016.lesson2.loadimagedemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.annotation.WorkerThread;
import android.view.View;

/**
 * Правильная релизация загрузки картинки в фоне с AsyncTask
 */
public class ExampleActivity5 extends BaseLoadImageActivity {

    // Выполняющийся таск загрузки файла
    private GetImageTask getImageTask;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(false);
        progressBar.setProgress(0);
        progressBar.setMax(100);

        if (savedInstanceState != null) {
            // Пытаемся получить ранее запущенный таск
            getImageTask = (GetImageTask) getLastCustomNonConfigurationInstance();
        }
        if (getImageTask == null) {
            // Создаем новый таск, только если не было ранее запущенного таска
            getImageTask = new GetImageTask(this);
            getImageTask.execute(TEST_IMAGE_URL);
        } else {
            // Передаем в ранее запущенный таск текущий объект Activity
            getImageTask.attachActivity(this);
        }
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        // Этот метод вызывается при смене конфигурации, когда текущий объект
        // Activity уничтожается. Объект, который мы вернем, не будет уничтожен,
        // и его можно будет использовать в новом объекте Activity
        return getImageTask;
    }

    static class GetImageTask extends AsyncTask<String, Integer, Bitmap>
            implements ProgressCallback {

        // Context приложения (Не Activity!) для доступа к файлам
        private Context appContext;
        // Текущий объект Activity, храним для обновления отображения
        private ExampleActivity5 activity;

        // Прогресс загрузки от 0 до 100
        private int progress;
        // Загруженная картинка
        private @Nullable Bitmap bitmap;

        GetImageTask(ExampleActivity5 activity) {
            this.appContext = activity.getApplicationContext();
            this.activity = activity;
        }

        /**
         * Этот метод вызывается, когда новый объект Activity подключается к
         * данному таску после смены конфигурации.
         *
         * @param activity новый объект Activity
         */
        @UiThread
        void attachActivity(ExampleActivity5 activity) {
            this.activity = activity;
            updateView();
        }

        /**
         * Вызываем на UI потоке для обновления отображения прогресса и
         * состояния в текущей активности.
         */
        @UiThread
        void updateView() {
            if (activity != null && !activity.isFinishing()) {
                activity.imageView.setImageBitmap(bitmap);
                if (bitmap != null) {
                    activity.progressBar.setVisibility(View.GONE);
                } else {
                    activity.progressBar.setProgress(progress);
                    activity.progressBar.setVisibility(View.VISIBLE);
                }
            }
        }

        /**
         * Вызывается в UI потоке из execute() до начала выполнения таска.
         */
        @Override
        @UiThread
        protected void onPreExecute() {
            updateView();
        }

        @Override
        @WorkerThread
        protected Bitmap doInBackground(String... params) {
            final String url = params[0];
            return downloadAndDecodeImage(appContext, url, this /*progressCallback*/);
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
                this.progress = values[values.length - 1];
                updateView();
            }
        }

        @Override
        @UiThread
        protected void onPostExecute(Bitmap bitmap) {
            this.bitmap = bitmap;
            updateView();
        }
    }
}
