package com.nicecat.leisure.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.FrameLayout;

import com.nicecat.leisure.R;
import com.nicecat.leisure.service.LeisureService_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by cchabot on 25/04/2015.
 */
@EActivity(R.layout.activity_main)
public class MainActivity extends ActionBarActivity implements
        MainFragment.OnFragmentInteractionListener,
        ResultsFragment.OnFragmentInteractionListener {

    private final static String LOG_TAG = MainActivity.class.getSimpleName();
    private final static String DFTAG = "DFTAG";

    private boolean bSavedInstance;
    private boolean mTwoPane;

    @ViewById
    FrameLayout resultsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bSavedInstance = savedInstanceState != null;
        Log.i(LOG_TAG, ">>> Saved instance state : " + bSavedInstance);
    }

    @AfterViews
    void setupResultsFragment() {
        if (resultsContainer != null) {
            // The result container belongs to the large-screen layouts (res/layout-sw600dp).
            // If this view is present, then the activity should be in two-pane mode.
            mTwoPane = true;

            if (!bSavedInstance) {
                // Show the result view in this activity by adding or replacing the result fragment
                // using a fragment transaction.
                ResultsFragment fragment = ResultsFragment_.builder().build();
                getFragmentManager().beginTransaction()
                        .replace(R.id.resultsContainer, fragment, DFTAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }

        Log.i(LOG_TAG, ">>> Two panes : " + mTwoPane);
    }

    @Override
    public void onButtonSearchClicked(Uri eventSummaryUri) {
        if (mTwoPane) {
            // In two-pane mode, show the result view in this activity by adding or replacing
            // the detail fragment using a fragment transaction.
            ResultsFragment fragment = ResultsFragment_.builder()
                    .eventSummaryUri(eventSummaryUri.toString())
                    .build();

            getFragmentManager().beginTransaction()
                    .replace(R.id.resultsContainer, fragment, DFTAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, ResultsActivity_.class).setData(eventSummaryUri);
            startActivity(intent);
        }

        // Load event summaries from REST open data API
        LeisureService_.intent(this).getEventSummaries(eventSummaryUri).start();
    }

    @Override
    public void onEventSummaryClicked(Uri detailUri) {
        // TODO manage URI
        // Intent intent = new Intent(this, DetailActivity_.class).setData(detailUri);
        // startActivity(intent);
    }
}
