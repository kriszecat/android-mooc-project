/*
 * Created by cchabot on 27/04/2015.
 */
package com.nicecat.leisure.data.city;

import java.util.Date;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.nicecat.leisure.data.base.AbstractCursor;

/**
 * Cursor wrapper for the {@code city} table.
 */
public class CityCursor extends AbstractCursor implements CityModel {
    public CityCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * Technical id of the city as provided by the API REST
     * Cannot be {@code null}.
     */
    @NonNull
    public String getCityId() {
        String res = getStringOrNull(CityColumns.CITY_ID);
        if (res == null)
            throw new NullPointerException("The value of 'city_id' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Name of the city .
     * Cannot be {@code null}.
     */
    @NonNull
    public String getTitle() {
        String res = getStringOrNull(CityColumns.TITLE);
        if (res == null)
            throw new NullPointerException("The value of 'title' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    @Override
    public long getId() {
        return getLongOrNull(CityColumns._ID);
    }

    /**
     * INSEE number of the city .
     */
    public int getInsee() {
        Integer res = getIntegerOrNull(CityColumns.INSEE);
        if (res == null)
            throw new NullPointerException("The value of 'insee' in the database was null, which is not allowed according to the model definition");
        return res;
    }
}
