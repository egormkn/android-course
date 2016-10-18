package ru.ifmo.droid2016.okdemo.ok;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import ru.ifmo.droid2016.okdemo.R;
import ru.ifmo.droid2016.okdemo.common.CurrentUser;
import ru.ifmo.droid2016.okdemo.common.WebViewUtils;
import ru.ifmo.droid2016.okdemo.loader.LoadResult;
import ru.ifmo.droid2016.okdemo.loader.ResultType;
import ru.ifmo.droid2016.okdemo.ok.api.OkApi;
import ru.ifmo.droid2016.okdemo.ok.api.OkApiRequest;
import ru.ifmo.droid2016.okdemo.ok.api.OkConstants;
import ru.ifmo.droid2016.okdemo.ok.api.Session;
import ru.ifmo.droid2016.okdemo.ok.api.SessionStore;
import ru.ifmo.droid2016.okdemo.ok.api.user.OkCurrentUserParser;
import ru.ifmo.droid2016.okdemo.ok.loader.OkApiRequestLoader;

/**
 * После логина выполняет запрос информации о текущем пользователе и показывает
 * его фотографию и имя.
 */
public class OkDemoActivity extends OkBaseActivity
        implements LoaderManager.LoaderCallbacks<LoadResult<CurrentUser, ? extends Exception>> {

    private TextView nameView;
    private SimpleDraweeView imageView;
    private ProgressBar progressView;
    private Button logoutButton;

    @Override
    protected void initContentView() {
        setContentView(R.layout.activity_ok_demo);
        nameView = (TextView) findViewById(R.id.user_name);
        imageView = (SimpleDraweeView) findViewById(R.id.user_photo);
        progressView = (ProgressBar) findViewById(R.id.progress);
        logoutButton = (Button) findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(logoutClickListener);

        resetView();
    }

    void resetView() {
        logoutButton.setEnabled(false);
        progressView.setVisibility(View.VISIBLE);
        nameView.setText(null);
        imageView.setImageBitmap(null);
    }

    @Override
    protected void onLoggedIn(Session session, boolean allowStateLoss) {
        startCurrentUserRequest();
    }

    void onCurrentUser(CurrentUser currentUser) {
        Log.d(TAG, "onCurrentUser: " + currentUser);
        nameView.setText(currentUser.name);
        if (!TextUtils.isEmpty(currentUser.picUrl)) {
            imageView.setImageURI(currentUser.picUrl);
        }
        progressView.setVisibility(View.GONE);
        logoutButton.setEnabled(true);
    }

    void onCurrentUserError(Exception error) {
        Log.w(TAG, "onCurrentUserError: " + error);
        String errorMessage = error == null ? getString(R.string.error) : error.getMessage();
        nameView.setText(errorMessage);
        progressView.setVisibility(View.GONE);
        logoutButton.setEnabled(true);
    }

    void onNoInternet() {
        Log.w(TAG, "onNoInternet");
        nameView.setText(R.string.no_internet);
        progressView.setVisibility(View.GONE);
    }


    void startCurrentUserRequest() {
        final LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(0, null, this);
    }

    @Override
    public Loader<LoadResult<CurrentUser, ? extends Exception>> onCreateLoader(int id, Bundle args) {
        final OkApiRequest getCurrentUserRequest =
                OkApi.User.CurrentUser.createRequest(OkConstants.APP_PUBLIC_KEY);
        return new OkApiRequestLoader<>(this, getCurrentUserRequest, new OkCurrentUserParser());
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<CurrentUser, ? extends Exception>> loader,
                               LoadResult<CurrentUser, ? extends Exception> result) {
        if (result.resultType == ResultType.OK) {
            onCurrentUser(result.data);
        } else if (result.resultType == ResultType.ERROR) {
            onCurrentUserError(result.error);
        } else if (result.resultType == ResultType.NO_INTERNET) {
            onNoInternet();
        }
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<CurrentUser, ? extends Exception>> loader) {
        resetView();
    }

    private View.OnClickListener logoutClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Удаляем сесиию из нашего хранилища
            SessionStore.getInstance().updateKeys(OkDemoActivity.this, null, null);
            // Очищаем вьюшки
            resetView();

            // Чистим куки в WebView, чтобы OAuth не подумал, что мы уже залогинились
            WebViewUtils.clearCookies(OkDemoActivity.this);

            // Отменяем загрузку для текущего пользователя
            final LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.destroyLoader(0);

            // Выкидываем пользователя на логин
            startLogin();

        }
    };

    private static final String TAG = "OkDemoActivity";
}
