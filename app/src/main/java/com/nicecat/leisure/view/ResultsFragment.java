package com.nicecat.leisure.view;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.nicecat.leisure.R;
import com.nicecat.leisure.controller.EventSummaryListAdapter;
import com.nicecat.leisure.model.LeisureEventSummary;
import com.nicecat.leisure.service.LeisureService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import java.util.List;


/**
 * Created by cchabot on 25/04/2015.
 */
@EFragment(R.layout.fragment_results)
@OptionsMenu(R.menu.menu_main)
public class ResultsFragment extends Fragment {

    //private ListAdapter mResultsAdapter;
    private OnFragmentInteractionListener mListener;

    private ArrayAdapter<LeisureEventSummary> mResultsAdapter;
    private LeisureEventSummary mSelectedEventSummary;

    @FragmentArg
    String eventSummaryUri;

    @ViewById
    ListView eventSummaryList;

    @OptionsMenuItem
    MenuItem menuShareEvent;

    @StringRes(R.string.event_summary_format)
    String eventSummaryFormat;


    @AfterViews
    void setupContentAdapter() {
        mResultsAdapter = new EventSummaryListAdapter(getActivity());
        //mResultsAdapter.setDropDownViewResource(R.layout.list_item_spinner_dropped);
        eventSummaryList.setAdapter(mResultsAdapter);
    }

    @ItemClick
    void eventSummaryListItemClicked(LeisureEventSummary event) {
        mSelectedEventSummary = event;

        // TODO manage detail view
        Uri uri = null;

        /*if (mListener != null) {
            mListener.onEventSummaryClicked(uri);
        }*/
    }

    // LeisureService.getSummaries() background task terminated
    @Receiver(actions = LeisureService.BROADCAST_GET_SUMMARIES, local = true)
    public void onReceiveSummaries(
            @Receiver.Extra(LeisureService.EXT_DATA_SUMMARIES) List<LeisureEventSummary> summaries) {
        mResultsAdapter.clear();
        mResultsAdapter.addAll(summaries);
    }

    @OptionsItem
    boolean menuShareEventSelected() {
        if (mSelectedEventSummary != null) {
            String summary = String.format(eventSummaryFormat,
                    mSelectedEventSummary.city,
                    mSelectedEventSummary.startDate,
                    mSelectedEventSummary.endDate,
                    mSelectedEventSummary.title);

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, summary);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }
        return true;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (mSelectedEventSummary == null) {
            menuShareEvent.setEnabled(false);
        } else {
            menuShareEvent.setEnabled(true);

        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onEventSummaryClicked(Uri uri);
    }

}
