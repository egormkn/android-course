package ru.ifmo.droid2016.okdemo.ok.loader;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;
import android.util.JsonReader;
import android.util.Log;

import com.facebook.stetho.urlconnection.StethoURLConnectionManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import ru.ifmo.droid2016.okdemo.loader.BadResponseException;
import ru.ifmo.droid2016.okdemo.loader.LoadResult;
import ru.ifmo.droid2016.okdemo.loader.ResultType;
import ru.ifmo.droid2016.okdemo.ok.api.OkApiNoSessionException;
import ru.ifmo.droid2016.okdemo.ok.api.OkApiRequest;
import ru.ifmo.droid2016.okdemo.ok.api.Session;
import ru.ifmo.droid2016.okdemo.ok.api.SessionStore;
import ru.ifmo.droid2016.okdemo.utils.IOUtils;

/**
 * Created by dmitry.trunin on 17.10.2016.
 */

public class OkApiRequestLoader<TData> extends AsyncTaskLoader<LoadResult<TData, ? extends Exception>> {

    @NonNull
    private final Context context;

    @NonNull
    private final OkApiRequest request;

    @NonNull
    private final OkApiResultParser<TData> parser;

    public OkApiRequestLoader(@NonNull Context context,
                              @NonNull OkApiRequest request,
                              @NonNull OkApiResultParser<TData> parser) {
        super(context);
        this.context = context;
        this.request = request;
        this.parser = parser;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public LoadResult<TData, ? extends Exception> loadInBackground() {
        Log.d(TAG, "Start performing Ok API request: " + request);

        // Для отладки. См. http://facebook.github.io/stetho/
        StethoURLConnectionManager stethoManager = new StethoURLConnectionManager("OkApi");

        // Получаем ранее сохраненную сессию, нужную для выполнения авторизованного запроса
        final Session session = SessionStore.getInstance().getSession(context);
        final String url;

        try {
            url = session.getGetUrl(request);
        } catch (OkApiNoSessionException e) {
            Log.e(TAG, "Failed to obtain API request URL: " + e, e);
            return LoadResult.error(e);
        }

        HttpURLConnection conn = null;
        InputStream in = null;

        try {

            conn = (HttpURLConnection) new URL(url).openConnection();
            stethoManager.preConnect(conn, null);

            int responseCode = conn.getResponseCode();
            Log.d(TAG, "Received HTTP response code: " + responseCode);
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("Unexpected HTTP response: " + responseCode
                        + ", " + conn.getResponseMessage());
            }

            stethoManager.postConnect();

            in = conn.getInputStream();
            in = stethoManager.interpretResponseStream(in);

            final JsonReader reader = new JsonReader(new InputStreamReader(in));
            final TData data = parser.parse(reader);
            return LoadResult.ok(data);

        } catch (IOException e) {
            Log.e(TAG, "Failed to receive response: " + e, e);
            stethoManager.httpExchangeFailed(e);
            if (IOUtils.isConnectionAvailable(getContext(), false)) {
                return LoadResult.error(e);
            } else {
                return LoadResult.noInternet();
            }

        } catch (BadResponseException e) {
            Log.e(TAG, "Failed to execute response: " + e, e);
            return LoadResult.error(e);

        } finally {
            // Закрываем все потоки и соедиениние
            IOUtils.readAndCloseSilently(in);
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private static final String TAG = "OkLoader";
}
