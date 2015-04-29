/*
 * Created by cchabot on 27/04/2015.
 */
package com.nicecat.leisure.data.city;

import com.nicecat.leisure.data.base.BaseModel;

import java.util.Date;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * A city where to find leisure events.
 */
public interface CityModel extends BaseModel {

    /**
     * Technical id of the city as provided by the API REST
     * Cannot be {@code null}.
     */
    @NonNull
    String getCityId();

    /**
     * Name of the city .
     * Cannot be {@code null}.
     */
    @NonNull
    String getTitle();
}
