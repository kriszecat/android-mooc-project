package com.nicecat.leisure;

import android.app.Application;

import com.nicecat.leisure.data.city.CityCursor;
import com.nicecat.leisure.data.city.CitySelection;
import com.nicecat.leisure.service.LeisureService_;

public class LeisureApplication extends Application {

    public void onCreate() {
        super.onCreate();

        // Load city database if not already loaded
        CitySelection select = new CitySelection();
        String[] projection = {"count(*) AS count"};
        CityCursor cursor = select.query(getContentResolver(), projection);
        if (cursor == null) {
            // Load cities from REST open data API
            LeisureService_.intent(getApplicationContext()).loadCities().start();
        } else {
            boolean b = cursor.moveToFirst();
            int count = cursor.getInt(0);
            if (count == 0) {
                // Load cities from REST open data API
                LeisureService_.intent(getApplicationContext()).loadCities().start();
            }
        }
    }
}