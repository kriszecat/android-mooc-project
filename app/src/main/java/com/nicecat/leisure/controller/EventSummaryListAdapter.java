package com.nicecat.leisure.controller;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.nicecat.leisure.R;
import com.nicecat.leisure.model.LeisureEventSummary;
import com.nicecat.leisure.view.EventItemView;
import com.nicecat.leisure.view.EventItemView_;

/**
 * Created by cchabot on 26/04/2015.
 */
public class EventSummaryListAdapter extends ArrayAdapter<LeisureEventSummary> {

    private Context mContext;

    public EventSummaryListAdapter(Context context) {
        super(context, R.layout.list_item_event);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        EventItemView eventItemView;
        if (convertView == null) {
            eventItemView = EventItemView_.build(mContext);
        } else {
            eventItemView = (EventItemView) convertView;
        }

        eventItemView.bind(getItem(position));

        if (position % 2 == 1) {
            eventItemView.setBackgroundResource(R.color.grey);
        } else {
            eventItemView.setBackgroundResource(R.color.leisure_light);
        }

        return eventItemView;
    }
}
