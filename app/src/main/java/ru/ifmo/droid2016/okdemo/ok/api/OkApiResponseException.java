package ru.ifmo.droid2016.okdemo.ok.api;

import android.support.annotation.NonNull;

import ru.ifmo.droid2016.okdemo.loader.BadResponseException;

/**
 * Исключение, возникающее, если на запрос API ответило ошибкой.
 */
public class OkApiResponseException extends BadResponseException {

    private static final long serialVersionUID = 1L;

    private final @NonNull
    OkApiErrorInfo info;

    public OkApiResponseException(@NonNull OkApiErrorInfo info) {
        super(info.toString());
        this.info = info;
    }

    public @NonNull
    OkApiErrorInfo getInfo() {
        return info;
    }
}
