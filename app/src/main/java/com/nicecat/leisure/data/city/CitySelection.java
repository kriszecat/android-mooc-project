/*
 * Created by cchabot on 27/04/2015.
 */
package com.nicecat.leisure.data.city;

import java.util.Date;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.nicecat.leisure.data.base.AbstractSelection;

/**
 * Selection for the {@code city} table.
 */
public class CitySelection extends AbstractSelection<CitySelection> {
    @Override
    protected Uri baseUri() {
        return CityColumns.CONTENT_URI;
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param contentResolver The content resolver to query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @param sortOrder How to order the rows, formatted as an SQL ORDER BY clause (excluding the ORDER BY itself). Passing null will use the default sort
     *            order, which may be unordered.
     * @return A {@code CityCursor} object, which is positioned before the first entry, or null.
     */
    public CityCursor query(ContentResolver contentResolver, String[] projection, String sortOrder) {
        Cursor cursor = contentResolver.query(uri(), projection, sel(), args(), sortOrder);
        if (cursor == null) return null;
        return new CityCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, projection, null)}.
     */
    public CityCursor query(ContentResolver contentResolver, String[] projection) {
        return query(contentResolver, projection, null);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, projection, null, null)}.
     */
    public CityCursor query(ContentResolver contentResolver) {
        return query(contentResolver, null, null);
    }


    public CitySelection id(long... value) {
        addEquals("city." + CityColumns._ID, toObjectArray(value));
        return this;
    }

    public CitySelection cityId(String... value) {
        addEquals(CityColumns.CITY_ID, value);
        return this;
    }

    public CitySelection cityIdNot(String... value) {
        addNotEquals(CityColumns.CITY_ID, value);
        return this;
    }

    public CitySelection cityIdLike(String... value) {
        addLike(CityColumns.CITY_ID, value);
        return this;
    }

    public CitySelection cityIdContains(String... value) {
        addContains(CityColumns.CITY_ID, value);
        return this;
    }

    public CitySelection cityIdStartsWith(String... value) {
        addStartsWith(CityColumns.CITY_ID, value);
        return this;
    }

    public CitySelection cityIdEndsWith(String... value) {
        addEndsWith(CityColumns.CITY_ID, value);
        return this;
    }

    public CitySelection title(String... value) {
        addEquals(CityColumns.TITLE, value);
        return this;
    }

    public CitySelection titleNot(String... value) {
        addNotEquals(CityColumns.TITLE, value);
        return this;
    }

    public CitySelection titleLike(String... value) {
        addLike(CityColumns.TITLE, value);
        return this;
    }

    public CitySelection titleContains(String... value) {
        addContains(CityColumns.TITLE, value);
        return this;
    }

    public CitySelection titleStartsWith(String... value) {
        addStartsWith(CityColumns.TITLE, value);
        return this;
    }

    public CitySelection titleEndsWith(String... value) {
        addEndsWith(CityColumns.TITLE, value);
        return this;
    }

    public CitySelection insee(int... value) {
        addEquals(CityColumns.INSEE, toObjectArray(value));
        return this;
    }

    public CitySelection inseeNot(int... value) {
        addNotEquals(CityColumns.INSEE, toObjectArray(value));
        return this;
    }

    public CitySelection inseeGt(int value) {
        addGreaterThan(CityColumns.INSEE, value);
        return this;
    }

    public CitySelection inseeGtEq(int value) {
        addGreaterThanOrEquals(CityColumns.INSEE, value);
        return this;
    }

    public CitySelection inseeLt(int value) {
        addLessThan(CityColumns.INSEE, value);
        return this;
    }

    public CitySelection inseeLtEq(int value) {
        addLessThanOrEquals(CityColumns.INSEE, value);
        return this;
    }
}
