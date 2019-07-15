package ru.ifmo.droid2016.worldcam.worldcamdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import ru.ifmo.droid2016.worldcam.worldcamdemo.loader.LoadResult;
import ru.ifmo.droid2016.worldcam.worldcamdemo.loader.NearbyWebcamsLoader;
import ru.ifmo.droid2016.worldcam.worldcamdemo.loader.ResultType;
import ru.ifmo.droid2016.worldcam.worldcamdemo.model.Webcam;
import ru.ifmo.droid2016.worldcam.worldcamdemo.utils.RecylcerDividersDecorator;

/**
 * Показывает список камер в выбранных координатах. Координаты передаются в extra параметрах
 * активности.
 */
public class NearbyWebcamsActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<LoadResult<List<Webcam>>> {

    public static final String EXTRA_LATITUDE = "lat";
    public static final String EXTRA_LONGITUDE = "lng";

    private RecyclerView recyclerView;
    private ProgressBar progressView;
    private TextView errorTextView;

    @Nullable
    private WebcamsRecyclerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nearby_webcams_activity);
        progressView = (ProgressBar) findViewById(R.id.progress);
        errorTextView = (TextView) findViewById(R.id.error_text);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(
                new RecylcerDividersDecorator(getResources().getColor(R.color.gray_a)));

        progressView.setVisibility(View.VISIBLE);
        errorTextView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

        // Pass all extra params directly to loader
        final Bundle loaderArgs = getIntent().getExtras();
        getSupportLoaderManager().initLoader(0, loaderArgs, this);
    }

    @Override
    public Loader<LoadResult<List<Webcam>>> onCreateLoader(int id, Bundle args) {
        final double latitude;
        final double longitude;

        if (args != null && args.containsKey(EXTRA_LATITUDE) && args.containsKey(EXTRA_LONGITUDE)) {
            latitude = args.getDouble(EXTRA_LATITUDE);
            longitude = args.getDouble(EXTRA_LONGITUDE);
        } else {
            // default location near SPb center
            latitude = 59.930;
            longitude = 30.372;
        }
        return new NearbyWebcamsLoader(this, latitude, longitude);
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<List<Webcam>>> loader,
                               LoadResult<List<Webcam>> result) {
        if (result.resultType == ResultType.OK) {
            if (result.data != null && !result.data.isEmpty()) {
                displayNonEmptyData(result.data);
            } else {
                displayEmptyData();
            }
        } else {
            displayError(result.resultType);
        }
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<List<Webcam>>> loader) {
        displayEmptyData();
    }

    private void displayEmptyData() {
        progressView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        errorTextView.setVisibility(View.VISIBLE);
        errorTextView.setText(R.string.cameras_not_found);
    }

    private void displayNonEmptyData(List<Webcam> webcams) {
        if (adapter == null) {
            adapter = new WebcamsRecyclerAdapter(this);
            recyclerView.setAdapter(adapter);
        }
        adapter.setWebcams(webcams);
        progressView.setVisibility(View.GONE);
        errorTextView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void displayError(ResultType resultType) {
        progressView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        errorTextView.setVisibility(View.VISIBLE);
        final int messageResId;
        if (resultType == ResultType.NO_INTERNET) {
            messageResId = R.string.no_inernet;
        } else {
            messageResId = R.string.error;
        }
        errorTextView.setText(messageResId);
    }

    private static final String TAG = "Webcams";

}
