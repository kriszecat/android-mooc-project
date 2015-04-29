/*
 * Created by cchabot on 27/04/2015.
 */
package com.nicecat.leisure.data.city;

import android.net.Uri;
import android.provider.BaseColumns;

import com.nicecat.leisure.data.LeisureProvider;
import com.nicecat.leisure.data.city.CityColumns;

/**
 * A city where to find leisure events.
 */
public class CityColumns implements BaseColumns {
    public static final String TABLE_NAME = "city";
    public static final Uri CONTENT_URI = Uri.parse(LeisureProvider.CONTENT_URI_BASE + "/" + TABLE_NAME);

    /**
     * Technical id of the city as provided by the API REST
     */
    public static final String CITY_ID = "city_id";

    /**
     * Name of the city .
     */
    public static final String TITLE = "title";

    /**
     * INSEE number of the city .
     */
    public static final String _ID = "insee";

    public static final String INSEE = "insee";


    public static final String DEFAULT_ORDER = TABLE_NAME + "." +_ID;

    // @formatter:off
    public static final String[] ALL_COLUMNS = new String[] {
            CITY_ID,
            TITLE,
            _ID
    };
    // @formatter:on

    public static boolean hasColumns(String[] projection) {
        if (projection == null) return true;
        for (String c : projection) {
            if (c.equals(CITY_ID) || c.contains("." + CITY_ID)) return true;
            if (c.equals(TITLE) || c.contains("." + TITLE)) return true;
            if (c.equals(INSEE) || c.contains("." + INSEE)) return true;
        }
        return false;
    }

}
