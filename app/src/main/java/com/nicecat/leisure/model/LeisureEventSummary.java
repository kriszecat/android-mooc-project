package com.nicecat.leisure.model;

public class LeisureEventSummary {
    public String eventId;
    public String title;
    public String city;
    public String startDate;
    public String endDate;

    public LeisureEventSummary(String eventId, String title, String city, String startDate, String endDate) {
        this.eventId = eventId;
        this.title = title;
        this.city = city;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
