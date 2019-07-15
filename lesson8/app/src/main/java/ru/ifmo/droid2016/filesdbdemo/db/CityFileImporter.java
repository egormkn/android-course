package ru.ifmo.droid2016.filesdbdemo.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.zip.GZIPInputStream;

import ru.ifmo.droid2016.filesdbdemo.json.CityJsonParser;
import ru.ifmo.droid2016.filesdbdemo.json.CityParserCallback;
import ru.ifmo.droid2016.filesdbdemo.util.CancelHandle;
import ru.ifmo.droid2016.filesdbdemo.util.ObservableInputStream;
import ru.ifmo.droid2016.filesdbdemo.util.ProgressCallback;

public abstract class CityFileImporter implements CityParserCallback {

    private SQLiteDatabase db;
    private SQLiteStatement insertStatement;
    private int importedCount;

    CityFileImporter(SQLiteDatabase db) {
        this.db = db;
    }

    public final synchronized void importCities(@NonNull File srcFile,
                                                @Nullable ProgressCallback progressCallback,
                                                @Nullable CancelHandle cancelHandle)
            throws IOException {

        InputStream in = null;

//        db.beginTransaction();
//        insertStatement = prepareInsertStatement(db);

        try {
            long fileSize = srcFile.length();
            in = new FileInputStream(srcFile);
            in = new BufferedInputStream(in);
            in = new ObservableInputStream(in, fileSize, progressCallback);
            in = new GZIPInputStream(in);
            importCities(in, cancelHandle);
//            db.setTransactionSuccessful();

        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Failed to close file: " + e, e);
                }
            }
//            insertStatement.close();
//            db.endTransaction();
        }
    }

    protected abstract CityJsonParser createParser();

    private void importCities(InputStream in, @Nullable CancelHandle cancelHandle) {
        CityJsonParser parser = createParser();
        try {
            parser.parseCities(in, this, cancelHandle);

        } catch (Exception e) {
            Log.e(LOG_TAG, "Failed to parse cities: " + e, e);
        }
    }

    @Override
    public void onCityParsed(long id, String name, String country, double lat, double lon) {
        insertCity(db, id, name, country, lat, lon);
        importedCount++;
        if (importedCount % 1000 == 0) {
            Log.d(LOG_TAG, "Processed " + importedCount + " cities");
        }
    }

    private boolean insertCity(SQLiteDatabase db,
                               long id,
                               @NonNull String name,
                               @NonNull String country,
                               double latitude,
                               double longitude) {
        final ContentValues values = new ContentValues();
        values.put(CityContract.CityColumns.CITY_ID, id);
        values.put(CityContract.CityColumns.NAME, name);
        values.put(CityContract.CityColumns.COUNTRY, country);
        values.put(CityContract.CityColumns.LATITUDE, latitude);
        values.put(CityContract.CityColumns.LONGITUDE, longitude);

        long rowId = db.insert(CityContract.Cities.TABLE, null /*nullColumnHack not needed*/, values);
        if (rowId < 0) {
            Log.w(LOG_TAG, "Failed to insert city: id=" + id + " name=" + name);
            return false;
        }
        return true;
    }

    private boolean insertCity(SQLiteStatement insertStatement,
                               long id,
                               @NonNull String name,
                               @NonNull String country,
                               double latitude,
                               double longitude) {
        insertStatement.clearBindings();
        insertStatement.bindLong(1, id);
        insertStatement.bindString(2, name);
        insertStatement.bindString(3, country);
        insertStatement.bindDouble(4, latitude);
        insertStatement.bindDouble(5, longitude);

        final long rowId = insertStatement.executeInsert();
        if (rowId < 0) {
            Log.w(LOG_TAG, "Failed to insert city: id=" + id + " name=" + name);
            return false;
        }
        return true;
    }

    private SQLiteStatement prepareInsertStatement(SQLiteDatabase db) {
        return db.compileStatement("INSERT INTO "
                + CityContract.Cities.TABLE + " ("
                + CityContract.CityColumns.CITY_ID + ","
                + CityContract.CityColumns.NAME + ","
                + CityContract.CityColumns.COUNTRY + ","
                + CityContract.CityColumns.LATITUDE + ","
                + CityContract.CityColumns.LONGITUDE
                + ") VALUES (?,?,?,?,?)");
    }

    private static final String LOG_TAG = "CityReader";

}
