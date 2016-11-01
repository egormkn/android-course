package ru.ifmo.droid2016.filesdbdemo.demoapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import ru.ifmo.droid2016.filesdbdemo.util.CancelHandle;
import ru.ifmo.droid2016.filesdbdemo.util.DownloadUtils;
import ru.ifmo.droid2016.filesdbdemo.util.FileUtils;
import ru.ifmo.droid2016.filesdbdemo.util.ProgressCallback;

/**
 * Экран, выполняющий инициализацию при первом запуске приложения. В процессе инициализации
 * скачивается файл с данными, нужными для работы приложения. Пока идет инициализация, показывается
 * индикатор прогресса.
 */
public class DownloadActivity extends ProgressTaskActivity {

    // Урл для скачивания файла с данными, нужными для инициализации БД.
    // GZIP-архив, содержащий список городов в формате JSON.
    private static final String CITIES_GZ_URL =
            "https://www.dropbox.com/s/d99ky6aac6upc73/city_array.json.gz?dl=1";

    @Override
    protected ProgressTask createTask() {
        return new DownloadTask(this);
    }

    static class DownloadTask extends ProgressTask {

        DownloadTask(ProgressTaskActivity activity) {
            super(activity);
        }

        @Override
        protected void runTask(@NonNull CancelHandle cancelHandle) throws IOException {
            File file = downloadFile(appContext, this, cancelHandle);
            new DemoPreferences(appContext).saveCitiesFileName(file.getName());
            Log.d(LOG_TAG, "Downloaded file: " + file);
        }
    }

    /**
     * Скачивает список городов во временный файл.
     */
    static File downloadFile(Context context,
                             ProgressCallback progressCallback,
                             @Nullable CancelHandle cancelHandle) throws IOException {
        File destFile = FileUtils.createExternalFile(context, "cities_json", "gz");
        DownloadUtils.downloadFile(CITIES_GZ_URL, destFile, progressCallback, cancelHandle);
        return destFile;
    }

}
