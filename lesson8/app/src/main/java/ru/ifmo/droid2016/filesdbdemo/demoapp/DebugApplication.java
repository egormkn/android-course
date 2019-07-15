package ru.ifmo.droid2016.filesdbdemo.demoapp;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Класс Application, прописывается в AndroidManifest.xml
 */
public class DebugApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Инициализируем библиотеку для отладки Stetho
        Stetho.initializeWithDefaults(this);
    }
}
