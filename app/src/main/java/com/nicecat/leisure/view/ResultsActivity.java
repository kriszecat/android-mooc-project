package com.nicecat.leisure.view;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.nicecat.leisure.R;

import org.androidannotations.annotations.EActivity;

/**
 * Created by cchabot on 25/04/2015.
 */
@EActivity(R.layout.activity_results)
public class ResultsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity using a fragment transaction.
            ResultsFragment fragment = new ResultsFragment_();

            getFragmentManager().beginTransaction()
                    .add(R.id.resultsContainer, fragment)
                    .commit();
        }
    }
}