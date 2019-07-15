package ru.ifmo.android_2016.lesson2.loadimagedemo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.view.View;

/**
 * Попытка скачать файл прямо в UI потоке (теперь выключаем StrictMode, чтобы получилось)
 */
public class ExampleActivity2 extends BaseLoadImageActivity {

    @Override
    @UiThread
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ВНИМАНИЕ: это очень плохая идея -- выполнять сетевые запросы в основном потоке.
        // Обычно Android просто не дает это сделать -- бросает NetworkOnMainThreadException.
        // Чтобы продемонстировать, как тормозит UI, мы можем выключить проверку потока, которую
        // делает система при выполнении сетевых запросов.
        //
        // Если закоментировать эту строчку, то можно будет увидеть в логах
        // NetworkOnMainThreadException.
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

        final Bitmap bitmap = downloadAndDecodeImage(this, TEST_IMAGE_URL);
        imageView.setImageBitmap(bitmap);

        progressBar.setVisibility(View.GONE);
    }
}
