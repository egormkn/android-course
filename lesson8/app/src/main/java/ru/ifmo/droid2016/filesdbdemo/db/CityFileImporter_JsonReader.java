package ru.ifmo.droid2016.filesdbdemo.db;

import android.database.sqlite.SQLiteDatabase;

import ru.ifmo.droid2016.filesdbdemo.json.CityJsonParser;
import ru.ifmo.droid2016.filesdbdemo.json.CityJsonReaderParser;

/**
 * Created by dmitry.trunin on 14.11.2015.
 */
public class CityFileImporter_JsonReader extends CityFileImporter {

    public CityFileImporter_JsonReader(SQLiteDatabase db) {
        super(db);
    }

    @Override
    protected CityJsonParser createParser() {
        return new CityJsonReaderParser();
    }
}
