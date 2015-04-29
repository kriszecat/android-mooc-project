/*
 * Created by cchabot on 27/04/2015.
 */
package com.nicecat.leisure.data;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.DefaultDatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import com.nicecat.leisure.BuildConfig;
import com.nicecat.leisure.data.city.CityColumns;

public class LeisureSQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = LeisureSQLiteOpenHelper.class.getSimpleName();

    public static final String DATABASE_FILE_NAME = "leisure.db";
    private static final int DATABASE_VERSION = 1;
    private static LeisureSQLiteOpenHelper sInstance;
    private final Context mContext;
    private final LeisureQLiteOpenHelperCallbacks mOpenHelperCallbacks;

    // @formatter:off
    public static final String SQL_CREATE_TABLE_CITY = "CREATE TABLE IF NOT EXISTS "
            + CityColumns.TABLE_NAME + " ( "
            + CityColumns.CITY_ID + " TEXT NOT NULL, "
            + CityColumns.TITLE + " TEXT NOT NULL, "
            + CityColumns._ID + " INTEGER PRIMARY KEY "
            + " );";

    public static final String SQL_CREATE_INDEX_CITY_INSEE = "CREATE INDEX IDX_CITY_INSEE "
            + " ON " + CityColumns.TABLE_NAME + " ( " + CityColumns.INSEE + " );";

    // @formatter:on

    public static LeisureSQLiteOpenHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = newInstance(context.getApplicationContext());
        }
        return sInstance;
    }

    private static LeisureSQLiteOpenHelper newInstance(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return newInstancePreHoneycomb(context);
        }
        return newInstancePostHoneycomb(context);
    }


    /*
     * Pre Honeycomb.
     */
    private static LeisureSQLiteOpenHelper newInstancePreHoneycomb(Context context) {
        return new LeisureSQLiteOpenHelper(context);
    }

    private LeisureSQLiteOpenHelper(Context context) {
        super(context, DATABASE_FILE_NAME, null, DATABASE_VERSION);
        mContext = context;
        mOpenHelperCallbacks = new LeisureQLiteOpenHelperCallbacks();
    }


    /*
     * Post Honeycomb.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static LeisureSQLiteOpenHelper newInstancePostHoneycomb(Context context) {
        return new LeisureSQLiteOpenHelper(context, new DefaultDatabaseErrorHandler());
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private LeisureSQLiteOpenHelper(Context context, DatabaseErrorHandler errorHandler) {
        super(context, DATABASE_FILE_NAME, null, DATABASE_VERSION, errorHandler);
        mContext = context;
        mOpenHelperCallbacks = new LeisureQLiteOpenHelperCallbacks();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onCreate");
        mOpenHelperCallbacks.onPreCreate(mContext, db);
        db.execSQL(SQL_CREATE_TABLE_CITY);
        db.execSQL(SQL_CREATE_INDEX_CITY_INSEE);
        mOpenHelperCallbacks.onPostCreate(mContext, db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            setForeignKeyConstraintsEnabled(db);
        }
        mOpenHelperCallbacks.onOpen(mContext, db);
    }

    private void setForeignKeyConstraintsEnabled(SQLiteDatabase db) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            setForeignKeyConstraintsEnabledPreJellyBean(db);
        } else {
            setForeignKeyConstraintsEnabledPostJellyBean(db);
        }
    }

    private void setForeignKeyConstraintsEnabledPreJellyBean(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON;");
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setForeignKeyConstraintsEnabledPostJellyBean(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        mOpenHelperCallbacks.onUpgrade(mContext, db, oldVersion, newVersion);
    }
}
