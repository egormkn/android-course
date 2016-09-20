package ru.ifmo.android_2016.lesson2.loadimagedemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.File;
import java.io.IOException;

/**
 * Базовый класс для активностей, которые используются в примерах.
 * Сожержит ImageView для отображения картинки и ProgressBar для отображения прогресса загрузки
 */
public abstract class BaseLoadImageActivity extends AppCompatActivity {

    protected static final String TEST_IMAGE_URL =
            "https://www.dropbox.com/s/lbmah6p5nosg9vm/image.jpg?dl=1";

    protected ImageView imageView;
    protected ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_image);
        imageView = (ImageView) findViewById(R.id.image_view);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
    }

    /**
     * Скачивает и декодирует изображение.
     */
    @Nullable
    protected static Bitmap downloadAndDecodeImage(@NonNull Context context,
                                                   @NonNull String url,
                                                   @Nullable ProgressCallback progressCallback) {
        try {
            final File destFile = FileUtils.createTempExternalFile(context, "jpg");
            DownloadUtils.downloadFile(url, destFile, progressCallback);
            return BitmapFactory.decodeFile(destFile.getAbsolutePath());
        } catch (IOException e) {
            Log.e("LoadImageDemo", "Failed to download image: " + e, e);
        }
        return null;
    }

    @Nullable
    protected static Bitmap downloadAndDecodeImage(@NonNull Context context,
                                                   @NonNull String url) {
        return downloadAndDecodeImage(context, url, null /*progressCallback*/);
    }

}
