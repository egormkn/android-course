package ru.ifmo.android_2016.lesson2.loadimagedemo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.view.View;

/**
 * Попытка скачать файл прямо в UI потоке
 */
public class ExampleActivity1 extends BaseLoadImageActivity {

    @Override
    @UiThread
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bitmap bitmap = downloadAndDecodeImage(this, TEST_IMAGE_URL);
        imageView.setImageBitmap(bitmap);

        progressBar.setVisibility(View.GONE);
    }
}
