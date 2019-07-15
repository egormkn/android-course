package ru.ifmo.android_2016.lesson2.loadimagedemo;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Используем этот специальный Application для отладки.
 */
public class DebugApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Включаем инструмент отладки Stetho: http://facebook.github.io/stetho/
        Stetho.initializeWithDefaults(this);
    }
}
