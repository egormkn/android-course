package ru.ifmo.droid2016.okdemo.ok.api;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Методы для работы с Ok API https://apiok.ru/dev/methods/rest/
 */
public final class OkApi {

    static final String BASE_HOST_NAME = "api.ok.ru";
    static final String DEFAULT_SCHEMA = "http";

    public static final class User {

        // Поле с именем пользователя
        public static final String NAME = "name";

        // Поле с урлом большой картинки пользователя
        public static final String PIC_FULL = "pic_full";

        private static final String PREFIX = "user.";

        public static final class CurrentUser {

            private static final String DEFAULT_FIELDS = PREFIX + NAME + "," + PREFIX + PIC_FULL;

            public static OkApiRequest createRequest(@NonNull String appPublicKey) {
                OkApiRequest request = new OkApiRequest();
                request.setMethod("users.getCurrentUser");
                request.addParam("application_key", appPublicKey);
                request.addParam("fields", DEFAULT_FIELDS);
                request.setUseSessionKey(true);
                return request;
            }
        }
    }



    public static String getGetUrl(@NonNull Context context, @NonNull OkApiRequest request)
            throws OkApiNoSessionException {
        if (request.useSessionKey) {
            final Session session =  SessionStore.getInstance().getSession(context);
            return session.getGetUrl(request);
        }
        throw new UnsupportedOperationException("No-session requests not supported");
    }

    private static AtomicInteger requestCount = new AtomicInteger(0);

    private OkApi() {}

    private static final String LOG_TAG = "Api";
}
