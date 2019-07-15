package ru.ifmo.droid2016.filesdbdemo.demoapp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.annotation.WorkerThread;
import android.util.Log;

import ru.ifmo.droid2016.filesdbdemo.util.CancelHandle;
import ru.ifmo.droid2016.filesdbdemo.util.ProgressCallback;

/**
 * Таск, выполняющий скачивание файла в фоновом потоке.
 */
public abstract class ProgressTask extends AsyncTask<Void, Integer, TaskState>
        implements ProgressCallback {

    // Context приложения (Не Activity!) для доступа к файлам
    protected final Context appContext;
    // Текущий объект Activity, храним для обновления отображения
    private ProgressTaskActivity activity;

    // Текущее состояние загрузки
    private volatile TaskState state = TaskState.NEW;
    // Прогресс загрузки от 0 до 100
    private int progress;

    // Момент времени, когда началось выполнение задачи
    private long executionStartTs;

    // Время, потраченное на выполнение задачи, либо -1, если импорт еще не завершен
    private long executionTimeMs = -1;

    ProgressTask(ProgressTaskActivity activity) {
        this.appContext = activity.getApplicationContext();
        this.activity = activity;
    }

    /**
     * Метод, выполняющий основную работу в фоновом потоке.
     *
     * @param cancelHandle - реализация метода должна регулярно проверять этот cancelHandle
     *                       и завершать работу, если таск отменен.
     *
     * @throws Exception
     */
    @WorkerThread
    protected abstract void runTask(@NonNull CancelHandle cancelHandle) throws Exception;

    TaskState getState() {
        return state;
    }

    int getProgress() {
        return progress;
    }

    long getExecutionTimeMs() {
        return executionTimeMs;
    }

    /**
     * Этот метод вызывается, когда новый объект Activity подключается к
     * данному таску после смены конфигурации.
     *
     * @param activity новый объект Activity
     */
    void attachActivity(ProgressTaskActivity activity) {
        this.activity = activity;
        activity.updateView(this);
    }


    /**
     * Вызывается в UI потоке из execute() до начала выполнения таска.
     */
    @Override
    protected void onPreExecute() {
        if (activity != null) {
            activity.updateView(this);
        }
    }

    /**
     * Скачивание файла в фоновом потоке. Возвращает результат:
     *      0 -- если файл успешно скачался
     *      1 -- если произошла ошибка
     */
    @Override
    @WorkerThread
    protected final TaskState doInBackground(Void... ignore) {
        progress = 0;
        state = TaskState.RUNNING;
        publishProgress(0);

        executionStartTs = System.currentTimeMillis();
        try {
            final CancelHandle cancelHandle = new CancelHandle() {
                @Override
                public boolean isCanceled() {
                    return ProgressTask.this.isCancelled();
                }
            };
            runTask(cancelHandle);

            executionTimeMs = System.currentTimeMillis() - executionStartTs;
            state = TaskState.DONE;

        } catch (Exception e) {
            Log.e(LOG_TAG, "Error downloading file: " + e, e);
            state = TaskState.ERROR;
        }
        return state;
    }

    // Метод ProgressCallback, вызывается в фоновом потоке из downloadFile
    @Override
    @WorkerThread
    public void onProgressChanged(int progress) {
        publishProgress(progress);
    }

    // Метод AsyncTask, вызывается в UI потоке в результате вызова publishProgress
    @Override
    @UiThread
    protected void onProgressUpdate(Integer... values) {
        if (values.length > 0) {
            int progress = values[values.length - 1];
            this.progress = progress;
            if (activity != null) {
                activity.updateView(this);
            }
        }
    }

    @Override
    @UiThread
    protected void onPostExecute(TaskState state) {
        // Проверяем код, который вернул doInBackground и показываем текст в зависимости
        // от результата
        this.state = state;
        if (state == TaskState.DONE) {
            progress = 100;
        }
        if (activity != null) {
            activity.updateView(this);
        }
    }

    @Override
    @UiThread
    protected void onCancelled(TaskState taskState) {
        this.state = TaskState.CANCELED;
        if (activity != null) {
            activity.updateView(this);
        }
    }

    protected final String LOG_TAG = getClass().getSimpleName();
}
