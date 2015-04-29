package com.nicecat.leisure.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cchabot on 28/04/2015.
 */
public class LeisureEventCategory implements Parcelable{
    public String categoryId;
    public String name;

    public LeisureEventCategory(String categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int describeContents() {
        return categoryId.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(categoryId);
        dest.writeString(name);
    }
}
