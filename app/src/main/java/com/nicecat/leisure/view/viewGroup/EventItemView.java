package com.nicecat.leisure.view;

import org.androidannotations.annotations.EViewGroup;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nicecat.leisure.R;
import com.nicecat.leisure.model.LeisureEventSummary;

import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

/**
 * Created by cchabot on 26/04/2015.
 */
@EViewGroup(R.layout.list_item_event)
public class EventItemView extends LinearLayout {

    @ViewById
    TextView eventTitle;

    @ViewById
    TextView eventCity;

    @ViewById
    TextView eventDates;

    @StringRes(R.string.result_event_dates)
    String eventDatesFormat;


    public EventItemView(Context context) {
        super(context);
    }

    public void bind(LeisureEventSummary event) {
        eventTitle.setText(event.title);
        eventCity.setText(event.city);
        eventDates.setText(String.format(eventDatesFormat, event.startDate, event.endDate));
    }
}