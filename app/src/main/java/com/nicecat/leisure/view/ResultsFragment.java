package com.nicecat.leisure.view;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.nicecat.leisure.R;
import com.nicecat.leisure.controller.EventSummaryListAdapter;
import com.nicecat.leisure.model.LeisureEventSummary;
import com.nicecat.leisure.service.LeisureService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.ViewById;

import java.util.List;


/**
 * Created by cchabot on 25/04/2015.
 */
@EFragment(R.layout.fragment_results)
public class ResultsFragment extends Fragment {

    //private ListAdapter mResultsAdapter;
    private OnFragmentInteractionListener mListener;

    private ArrayAdapter<LeisureEventSummary> mResultsAdapter;

    @FragmentArg
    String eventSummaryUri;

    @ViewById
    ListView eventSummaryList;

    @AfterViews
    void setupContentAdapter() {
        mResultsAdapter = new EventSummaryListAdapter(getActivity());
        mResultsAdapter.setDropDownViewResource(R.layout.list_item_spinner_dropped);
        eventSummaryList.setAdapter(mResultsAdapter);
    }

    @ItemClick
    void eventSummaryListItemClicked(LeisureEventSummary event) {
        // TODO build URI
        Uri uri = null;

        if (mListener != null) {
            mListener.onEventSummaryClicked(uri);
        }
        Toast.makeText(getActivity(), event.title + " " + event.city, Toast.LENGTH_SHORT).show();
    }

    // LeisureService.getSummaries() background task terminated
    @Receiver(actions = LeisureService.BROADCAST_GET_SUMMARIES, local = true)
    public void onReceiveSummaries(
            @Receiver.Extra(LeisureService.EXT_DATA_SUMMARIES) List<LeisureEventSummary> summaries) {
        mResultsAdapter.clear();
        mResultsAdapter.addAll(summaries);
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
