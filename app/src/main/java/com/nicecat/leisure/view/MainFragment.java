package com.nicecat.leisure.view;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.nicecat.leisure.R;
import com.nicecat.leisure.controller.CitySpinnerAdapter;
import com.nicecat.leisure.data.city.CityColumns;
import com.nicecat.leisure.model.LeisureEventCategory;
import com.nicecat.leisure.service.LeisureService;
import com.nicecat.leisure.service.LeisureService_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.ItemSelect;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by cchabot on 25/04/2015.
 */
@EFragment(R.layout.fragment_main)
public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private OnFragmentInteractionListener mListener;

    private static final String[] CITY_COLUMNS = {
            CityColumns.TITLE,
            CityColumns.INSEE
    };
    public static final int COL_CITY_TITLE = 0;
    public static final int COL_CITY_INSEE = 1;

    private CitySpinnerAdapter mCityAdapter;
    private ArrayAdapter<LeisureEventCategory> mThemeAdapter;

    @InstanceState
    int mThemePosition = Spinner.INVALID_POSITION;

    @InstanceState
    int mCityPosition = Spinner.INVALID_POSITION;

    @InstanceState
    int mCurrentDay = -1;

    @InstanceState
    int mCurrentMonth = -1;

    @InstanceState
    int mCurrentYear = -1;

    @StringRes
    String BASE_URL;

    @ViewById
    Spinner spinnerTheme;

    @ViewById
    Spinner spinnerCity;

    @ViewById
    DatePicker datePicker;

    @AfterViews
    void setupCategories() {
        // Load categories from REST open data API
        LeisureService_.intent(getActivity()).getCategories().start();
    }

    @AfterViews
    void setupContentAdapters() {
        // Create and attach spinner adapters
        mCityAdapter = new CitySpinnerAdapter(getActivity(), null, 0);
        spinnerCity.setAdapter(mCityAdapter);

        mThemeAdapter = new ArrayAdapter<LeisureEventCategory>(
                getActivity(), R.layout.list_item_spinner, 0);
        mThemeAdapter.setDropDownViewResource(R.layout.list_item_spinner_dropped);
        spinnerTheme.setAdapter(mThemeAdapter);
    }

    @AfterViews
    void setupViews() {
        // Setup or reset views to their original state
        if (mThemePosition != ListView.INVALID_POSITION) {
            spinnerTheme.setSelection(mThemePosition);
        }

        if (mCurrentDay == -1) {
            Calendar now = Calendar.getInstance();
            datePicker.init(
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH),
                    new DatePicker.OnDateChangedListener() {
                        @Override
                        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            mCurrentDay = dayOfMonth;
                            mCurrentMonth = monthOfYear;
                            mCurrentYear = year;
                        }
                    });
        } else {
            datePicker.updateDate(mCurrentYear, mCurrentMonth, mCurrentDay);
        }
    }

    @ItemSelect
    public void spinnerThemeItemSelected(boolean selected, Object selectedItem) {
        mThemePosition = spinnerTheme.getSelectedItemPosition();
    }

    @ItemSelect
    public void spinnerCityItemSelected(boolean selected, Object selectedItem) {
        mCityPosition = spinnerCity.getSelectedItemPosition();
    }

    @Click
    void searchButtonClicked() {
        // Get current date from date picker
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        long dateTime = datePicker.getCalendarView().getDate();
        String date = dateFormat.format(new Date(dateTime));

        // Get current theme id from theme spinner
        LeisureEventCategory category = (LeisureEventCategory) spinnerTheme.getSelectedItem();
        String categoryId = category.categoryId;

        // Get current insee number from city spinner
        Cursor cursor = (Cursor) spinnerCity.getSelectedItem();
        int insee = -1;
        if (cursor != null) {
            insee = cursor.getInt(COL_CITY_INSEE);
        }

        // Construct URI from the search criteria
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath("event")
                .appendPath("summary")
                .appendQueryParameter("catIds", categoryId)
                .appendQueryParameter("insee", Integer.toString(insee))
                .appendQueryParameter("itemsPerPage", Integer.toString(100))
                .build();

        if (mListener != null) {
            mListener.onButtonSearchClicked(uri);
        }
        Toast.makeText(getActivity(), uri.toString(), Toast.LENGTH_SHORT).show();
    }

    // LeisureService.getCategories() background task terminated
    @Receiver(actions = LeisureService.BROADCAST_GET_CATEGORIES, local = true)
    public void onReceiveCategories(
            @Receiver.Extra(LeisureService.EXT_DATA_CATEGORIES) List<LeisureEventCategory> categories) {
        mThemeAdapter.clear();
        mThemeAdapter.addAll(categories);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(007, null, this);
        super.onActivityCreated(savedInstanceState);
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // This is called when a new Loader needs to be created.
        // This fragment only uses one loader, so we don't care about checking the id.
        return new CursorLoader(getActivity(), CityColumns.CONTENT_URI,
                CITY_COLUMNS, null, null, CityColumns.DEFAULT_ORDER);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCityAdapter.swapCursor(data);
        if (mCityPosition != ListView.INVALID_POSITION) {
            spinnerCity.setSelection(mCityPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCityAdapter.swapCursor(null);
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
        public void onButtonSearchClicked(Uri uri);
    }

}
