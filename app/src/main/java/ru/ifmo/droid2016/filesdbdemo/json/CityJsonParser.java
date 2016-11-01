package ru.ifmo.droid2016.filesdbdemo.json;

import android.support.annotation.Nullable;

import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;

import ru.ifmo.droid2016.filesdbdemo.util.CancelHandle;

/**
 * Created by dmitry.trunin on 16.11.2015.
 */
public interface CityJsonParser {

    void parseCities(InputStream in,
                     @Nullable CityParserCallback callback,
                     @Nullable CancelHandle cancelHandle) throws Exception;
}
