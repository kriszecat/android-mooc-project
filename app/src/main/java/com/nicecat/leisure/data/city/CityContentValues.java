/*
 * Created by cchabot on 27/04/2015.
 */
package com.nicecat.leisure.data.city;

import java.util.Date;

import android.content.ContentResolver;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.nicecat.leisure.data.base.AbstractContentValues;

/**
 * Content values wrapper for the {@code city} table.
 */
public class CityContentValues extends AbstractContentValues {
    @Override
    public Uri uri() {
        return CityColumns.CONTENT_URI;
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(ContentResolver contentResolver, @Nullable CitySelection where) {
        return contentResolver.update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    /**
     * Technical id of the city as provided by the API REST
     */
    public CityContentValues putCityId(@NonNull String value) {
        if (value == null) throw new IllegalArgumentException("cityId must not be null");
        mContentValues.put(CityColumns.CITY_ID, value);
        return this;
    }


    /**
     * Name of the city .
     */
    public CityContentValues putTitle(@NonNull String value) {
        if (value == null) throw new IllegalArgumentException("title must not be null");
        mContentValues.put(CityColumns.TITLE, value);
        return this;
    }


    /**
     * INSEE number of the city .
     */
    public CityContentValues putInsee(int value) {
        mContentValues.put(CityColumns.INSEE, value);
        return this;
    }

}
