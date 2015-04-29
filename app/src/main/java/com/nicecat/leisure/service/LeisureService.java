package com.nicecat.leisure.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.nicecat.leisure.data.city.CityContentValues;
import com.nicecat.leisure.model.LeisureEventSummary;
import com.nicecat.leisure.model.LeisureEventCategory;

import org.androidannotations.annotations.EIntentService;
import org.androidannotations.annotations.ServiceAction;
import org.androidannotations.annotations.Trace;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.res.StringRes;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

@EIntentService
public class LeisureService extends IntentService {
    private static final String LOG_TAG = LeisureService.class.getSimpleName();
    public static final String BROADCAST_GET_CATEGORIES =
            "com.nicecat.leisure.service.BROADCAST_GET_CATEGORIES";
    public static final String EXT_DATA_CATEGORIES =
            "com.nicecat.leisure.service.EXT_DATA_CATEGORIES";
    public static final String BROADCAST_GET_SUMMARIES =
            "com.nicecat.leisure.service.BROADCAST_GET_SUMMARIES";
    public static final String EXT_DATA_SUMMARIES =
            "com.nicecat.leisure.service.EXT_DATA_SUMMARIES";

    @StringRes
    String BASE_URL;

    public LeisureService() {
        super("LeisureService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Do nothing here
    }

    @ServiceAction
    @Trace
    public void loadCities() {
        final String CITY_BASE_URL = BASE_URL + "city";
        Uri builtUri = Uri.parse(CITY_BASE_URL).buildUpon().build();
        try {
            URL url = new URL(builtUri.toString());
            String response = getHttpResponse(url);
            if (response != null) {
                bulkInsertCities(response);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Wrong URL ", e);
            showToast(e.getLocalizedMessage());
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Payload parsing error", e);
            showToast(e.getLocalizedMessage());
        } catch (IOException e) {
            Log.e(LOG_TAG, "Payload parsing error", e);
            showToast(e.getLocalizedMessage());
        }
    }

    @ServiceAction
    @Trace
    public void getCategories() {
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath("parameter")
                .appendPath("AgendaLoisirs_rootAgendaCatIds")
                .build();
        try {
            URL url = new URL(uri.toString());
            String httpResponse = getHttpResponse(url);
            if (httpResponse != null) {
                String rootCategory = extractRootCategory(httpResponse);

                uri = Uri.parse(BASE_URL).buildUpon()
                        .appendPath("category")
                        .appendPath(rootCategory)
                        .appendPath("children")
                        .build();
                url = new URL(uri.toString());
                httpResponse = getHttpResponse(url);
                List<LeisureEventCategory> categories = extractChildrenCategory(httpResponse);

                // Send categories result
                Intent result = new Intent(BROADCAST_GET_CATEGORIES)
                        .putExtra(EXT_DATA_CATEGORIES, (ArrayList<LeisureEventCategory>)categories);
                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Wrong URL ", e);
            showToast(e.getLocalizedMessage());
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Payload parsing error", e);
            showToast(e.getLocalizedMessage());
        } catch (IOException e) {
            Log.e(LOG_TAG, "Payload parsing error", e);
            showToast(e.getLocalizedMessage());
        }
    }

    @ServiceAction
    @Trace
    public void getEventSummaries(Uri eventSummaryUri) {
        try {
            URL url = new URL(eventSummaryUri.toString());
            String httpResponse = getHttpResponse(url);
            if (httpResponse != null) {
                List<LeisureEventSummary> summaries = extractEventSummaries(httpResponse);

                // Send categories result
                Intent result = new Intent(BROADCAST_GET_SUMMARIES)
                        .putExtra(EXT_DATA_SUMMARIES, (ArrayList<LeisureEventSummary>) summaries);
                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Wrong URL ", e);
            showToast(e.getLocalizedMessage());
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Payload parsing error", e);
            showToast(e.getLocalizedMessage());
        } catch (IOException e) {
            Log.e(LOG_TAG, "Payload parsing error", e);
            showToast(e.getLocalizedMessage());
        }
    }

    @UiThread
    void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    private String getHttpResponse(URL url) throws IOException {
        String response = null;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Create the request to URL, and open the connection
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();

        InputStream inputStream = urlConnection.getInputStream();
        StringBuffer buffer = new StringBuffer();
        if (inputStream != null) {
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n"); // New add line For debugging
            }

            if (buffer.length() != 0) {
                response = buffer.toString();
            }
            reader.close();
        }
        urlConnection.disconnect();

        return response;
    }

    private void bulkInsertCities(String jsonPayload) throws JSONException {
        // These are the names of the JSON properties that need to be extracted
        final String CITY_ID = "id";
        final String CITY_TITLE = "title";
        final String CITY_INSEE = "insee";

        JSONArray citiesArray = new JSONArray(jsonPayload);
        Vector<ContentValues> cvVector = new Vector<ContentValues>(citiesArray.length());

        // Parse the cities from JSON stream
        for (int i = 0; i < citiesArray.length(); i++) {
            JSONObject cityJson = citiesArray.getJSONObject(i);
            /*String id = cityJson.getString(CITY_ID);
            String title = cityJson.getString(CITY_TITLE);
            Integer insee = cityJson.getInt(CITY_INSEE);
            String message = String.format(">>> city[%s, %s, %d]", id, title, insee);
            Log.i(LOG_TAG, message);*/

            CityContentValues values = new CityContentValues();
            values.putCityId(cityJson.getString(CITY_ID));
            values.putTitle(cityJson.getString(CITY_TITLE));
            values.putInsee(cityJson.getInt(CITY_INSEE));
            cvVector.add(values.values());
        }

        // Bulk insert the content values fetched from JSON stream
        if (cvVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cvVector.size()];
            cvVector.toArray(cvArray);
            getContentResolver().bulkInsert(new CityContentValues().uri(), cvArray);
        }
    }

    private String extractRootCategory(String jsonPayload) throws JSONException {
        String category = null;
        final String CATEGORY_VALUE = "value";

        JSONArray rootArray = new JSONArray(jsonPayload);
        if (rootArray.length() == 1) {
            JSONObject categoryJson = rootArray.getJSONObject(0);
            category = categoryJson.getString(CATEGORY_VALUE);
        }
        return category;
    }

    private List<LeisureEventCategory> extractChildrenCategory(String jsonPayload) throws JSONException {
        List<LeisureEventCategory> categories = null;

        final String CHILDREN_CATEGORY = "children";
        final String CATEGORY_ID = "id";
        final String CATEGORY_NAME = "name";

        JSONArray rootArray = new JSONArray(jsonPayload);
        if (rootArray.length() == 1) {
            JSONObject response = rootArray.getJSONObject(0);

            JSONArray categoryArray = response.getJSONArray(CHILDREN_CATEGORY);
            categories = new ArrayList<LeisureEventCategory>(categoryArray.length());

            for (int i = 0; i < categoryArray.length(); i++) {
                JSONObject categoryJson = categoryArray.getJSONObject(i);
                categories.add(new LeisureEventCategory(
                        categoryJson.getString(CATEGORY_ID),
                        categoryJson.getString(CATEGORY_NAME)));
            }
        }

        return categories;
    }

    private List<LeisureEventSummary> extractEventSummaries(String jsonPayload) throws JSONException {
        List<LeisureEventSummary> summaries = null;

        final String DATA = "data";
        final String EVENT_ID = "eventId";
        final String TITLE = "title";
        final String CITY = "city";
        final String START_DATE = "startDate";
        final String END_DATE = "endDate";

        JSONObject rootJson = new JSONObject(jsonPayload);
        JSONArray dataArray = rootJson.getJSONArray(DATA);
        summaries = new ArrayList<LeisureEventSummary>(dataArray.length());

        for (int i = 0; i < dataArray.length(); i++) {
            JSONObject summaryJson = dataArray.getJSONObject(i);
            JSONObject cityJson = summaryJson.getJSONObject(CITY);
            summaries.add(new LeisureEventSummary(
                    summaryJson.getString(EVENT_ID),
                    summaryJson.getString(TITLE),
                    cityJson.getString(TITLE),
                    summaryJson.getString(START_DATE),
                    summaryJson.getString(END_DATE)));
        }

        return summaries;
    }

}