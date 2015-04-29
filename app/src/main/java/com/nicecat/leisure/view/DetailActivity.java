package com.nicecat.leisure.view;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.nicecat.leisure.R;

import org.androidannotations.annotations.EActivity;

/**
 * Created by cchabot on 25/04/2015.
 */
@EActivity(R.layout.activity_detail)
public class DetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putParcelable(DetailFragment.DETAIL_URI, getIntent().getData());

            DetailFragment fragment = new DetailFragment_();
            fragment.setArguments(arguments);

            getFragmentManager().beginTransaction()
                    .add(R.id.item_detail_container, fragment)
                    .commit();
        }
    }
}