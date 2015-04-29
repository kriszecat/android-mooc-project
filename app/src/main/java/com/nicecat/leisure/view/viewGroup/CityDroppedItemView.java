package com.nicecat.leisure.view.viewGroup;

import android.content.Context;
import android.database.Cursor;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nicecat.leisure.R;
import com.nicecat.leisure.view.MainFragment;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by cchabot on 26/04/2015.
 */
@EViewGroup(R.layout.list_item_spinner_dropped)
public class CityDroppedItemView extends LinearLayout {

    @ViewById
    TextView label;

    public CityDroppedItemView(Context context) {
        super(context);
    }

    public void bind(Cursor cursor) {
        String title = cursor.getString(MainFragment.COL_CITY_TITLE);
        label.setText(title);
    }
}