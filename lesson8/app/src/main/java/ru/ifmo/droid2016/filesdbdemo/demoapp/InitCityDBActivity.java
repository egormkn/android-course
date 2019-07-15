package ru.ifmo.droid2016.filesdbdemo.demoapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import ru.ifmo.droid2016.filesdbdemo.db.CityDBHelper;
import ru.ifmo.droid2016.filesdbdemo.db.CityFileImporter_JsonReader;
import ru.ifmo.droid2016.filesdbdemo.util.CancelHandle;
import ru.ifmo.droid2016.filesdbdemo.util.ProgressCallback;

/**
 * Экран, выполняющий инициализацию базы данны городов. Данные читаются из ранее
 * скачанного файла (см. DownloadActivity) и записываются в базу данных.
 */
public class InitCityDBActivity extends ProgressTaskActivity {

    @Override
    protected ProgressTask createTask() {
        return new InitCityDBTask(this);
    }

    static class InitCityDBTask extends ProgressTask {

        InitCityDBTask(ProgressTaskActivity activity) {
            super(activity);
        }

        @Override
        protected void runTask(@NonNull CancelHandle cancelHandle) throws IOException {
            String fileName = new DemoPreferences(appContext).getCitiesFileName();
            if (TextUtils.isEmpty(fileName)) {
                throw new FileNotFoundException("File name is null");
            }
            File file = new File(appContext.getExternalFilesDir(null), fileName);
            importCitites(appContext, file, this, cancelHandle);

            if (!isCancelled()) {
                new DemoPreferences(appContext).saveDbIsReady(true);
            } else {
                CityDBHelper.getInstance(appContext).dropDb();
            }
        }

        @Override
        protected void onCancelled(TaskState taskState) {
            super.onCancelled(taskState);
            new DemoPreferences(appContext).saveDbIsReady(false);
            CityDBHelper.getInstance(appContext).dropDb();
        }
    }

    /**
     * Импортирует города из файла в базу данных.
     */
    static void importCitites(Context context,
                              File file,
                              ProgressCallback progressCallback,
                              CancelHandle cancelHandle) throws IOException {
        SQLiteDatabase db = CityDBHelper.getInstance(context).getWritableDatabase();
        new CityFileImporter_JsonReader(db).importCities(file, progressCallback, cancelHandle);
    }

}
