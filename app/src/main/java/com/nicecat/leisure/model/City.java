package com.nicecat.leisure.model;

/**
 * Created by cchabot on 26/04/2015.
 */
public class City {

    int id;
    public String cityId;
    public String title;
    public Integer insee;

    public City() {
        // Needed by ormlite
    }

    public City(String cityId, String title, Integer insee) {
        this.cityId = cityId;
        this.title = title;
        this.insee = insee;
    }
}
