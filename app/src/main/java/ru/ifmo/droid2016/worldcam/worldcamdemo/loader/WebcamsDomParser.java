package ru.ifmo.droid2016.worldcam.worldcamdemo.loader;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.ifmo.droid2016.worldcam.worldcamdemo.model.Webcam;
import ru.ifmo.droid2016.worldcam.worldcamdemo.utils.IOUtils;

/**
 * Методы для парсинга ответов от Webcams API при помощи JSONObject (DOM parser)
 */
public final class WebcamsDomParser {

    @NonNull
    public static List<Webcam> parseWebcams(InputStream in) throws
            IOException,
            JSONException,
            BadResponseException {

        final String content = IOUtils.readToString(in, "UTF-8");
        final JSONObject json = new JSONObject(content);
        return parseWebcams(json);
    }

    @NonNull
    private static List<Webcam> parseWebcams(JSONObject json) throws
            IOException,
            JSONException,
            BadResponseException {

        final String status = json.getString("status");
        if (!"OK".equals(status)) {
            throw new BadResponseException("Unexpected response status from API: " + status);
        }

        final JSONObject resultJson = json.getJSONObject("result");
        final JSONArray webcamsJson = resultJson.getJSONArray("webcams");
        final ArrayList<Webcam> webcams = new ArrayList<>();

        for (int i = 0; i < webcamsJson.length(); i++) {
            final JSONObject webcamJson = webcamsJson.optJSONObject(i);

            if (webcamJson != null) {
                final String id = webcamJson.optString("id", null);
                final String title = webcamJson.optString("title", null);
                final JSONObject imageJson = webcamJson.optJSONObject("image");
                String imageUrl = null;

                if (imageJson != null) {
                    imageUrl = getImageUrlFromJsonByType(imageJson, "daylight");
                    if (TextUtils.isEmpty(imageUrl)) {
                        imageUrl = getImageUrlFromJsonByType(imageJson, "current");
                    }
                }

                if (!TextUtils.isEmpty(id) && !TextUtils.isEmpty(title)
                        && !TextUtils.isEmpty(imageUrl)) {
                    final Webcam webcam = new Webcam(id, title, imageUrl);
                    webcams.add(webcam);
                }
            }
        }
        return webcams;
    }

    @Nullable
    private static String getImageUrlFromJsonByType(JSONObject imageJson, String imageType)
            throws IOException, JSONException{
        JSONObject images = imageJson.optJSONObject(imageType);
        if (images != null) {
            return images.optString(PREFERRED_IMAGE_SIZE, null);
        }
        return null;
    }

    private static final String PREFERRED_IMAGE_SIZE = "preview";

    private WebcamsDomParser() {}
}
