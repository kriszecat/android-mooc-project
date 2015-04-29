package com.nicecat.leisure.controller;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import com.nicecat.leisure.view.viewGroup.CityDroppedItemView;
import com.nicecat.leisure.view.viewGroup.CityDroppedItemView_;
import com.nicecat.leisure.view.viewGroup.CityItemView;
import com.nicecat.leisure.view.viewGroup.CityItemView_;

/**
 * Created by cchabot on 27/04/2015.
 */
public class CitySpinnerAdapter extends CursorAdapter {
    private Context mContext;

    public CitySpinnerAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return CityItemView_.build(context);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        if (view instanceof CityItemView) {
            ((CityItemView) view).bind(cursor);
        }
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        CityDroppedItemView itemView;
        if (convertView == null) {
            itemView = CityDroppedItemView_.build(mContext);
        } else {
            itemView = (CityDroppedItemView) convertView;
        }
        itemView.bind((Cursor) getItem(position));
        return itemView;
    }
}
