package ru.ifmo.droid2016.filesdbdemo.demoapp;

import android.support.annotation.NonNull;

import java.io.IOException;

import ru.ifmo.droid2016.filesdbdemo.db.CityDBHelper;
import ru.ifmo.droid2016.filesdbdemo.util.CancelHandle;
import ru.ifmo.droid2016.filesdbdemo.util.FileUtils;

/**
 * Экран, выполняющий очистку: удаляет все файлы, сохраненные настройки и БД
 */
public class CleanActivity extends ProgressTaskActivity {

    @Override
    protected ProgressTask createTask() {
        return new CleanTask(this);
    }

    static class CleanTask extends ProgressTask {

        CleanTask(ProgressTaskActivity activity) {
            super(activity);
        }

        @Override
        protected void runTask(@NonNull CancelHandle cancelHandle) throws IOException {
            new DemoPreferences(appContext).clearSync();
            FileUtils.cleanExternalFilesDir(appContext);
            CityDBHelper.getInstance(appContext).dropDb();
        }
    }
}
