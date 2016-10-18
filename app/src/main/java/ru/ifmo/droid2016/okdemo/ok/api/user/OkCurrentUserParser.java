package ru.ifmo.droid2016.okdemo.ok.api.user;

import android.support.annotation.NonNull;
import android.util.JsonReader;

import java.io.IOException;

import ru.ifmo.droid2016.okdemo.common.CurrentUser;
import ru.ifmo.droid2016.okdemo.ok.loader.OkApiResultParser;
import ru.ifmo.droid2016.okdemo.ok.api.OkApi;

/**
 * Created by dmitry.trunin on 30.11.2015.
 */
public class OkCurrentUserParser extends OkApiResultParser<CurrentUser> {

    private String bigPicUrl;
    private String userName;

    @Override
    protected boolean parseRootField(String key, JsonReader reader) throws IOException {
        switch (key) {
            case OkApi.User.NAME:       userName = reader.nextString(); return true;
            case OkApi.User.PIC_FULL:   bigPicUrl = reader.nextString(); return true;
        }
        return false;
    }

    @NonNull
    @Override
    protected CurrentUser createResult() throws IOException {
        return new CurrentUser(bigPicUrl, userName);
    }
}
