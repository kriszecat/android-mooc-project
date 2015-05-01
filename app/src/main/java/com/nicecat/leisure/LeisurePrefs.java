package com.nicecat.leisure;

import org.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref
public interface LeisurePrefs {

    int categoryPosition();
    int cityPosition();
}