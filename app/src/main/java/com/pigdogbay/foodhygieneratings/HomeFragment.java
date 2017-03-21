package com.pigdogbay.foodhygieneratings;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pigdogbay.foodhygieneratings.model.IDataProvider;
import com.pigdogbay.foodhygieneratings.model.MainModel;
import com.pigdogbay.foodhygieneratings.model.Query;
import com.pigdogbay.foodhygieneratings.model.SearchType;
import com.pigdogbay.lib.utils.ActivityUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public static final String TAG = "home";

    private IDataProvider getDataProvider(){
        return MainModel.get(getContext()).getDataProvider();
    }

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_home, container, false);
        wireUpControls(rootView);
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_home, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_search:
                quickSearch();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void wireUpControls(View view){
        view.findViewById(R.id.home_search_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quickSearch();
            }
        });

        view.findViewById(R.id.home_advanced_search_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                advancedSearch();
            }
        });
        view.findViewById(R.id.home_places_near_me_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placesNearMe();
            }
        });
    }

    private void quickSearch() {
        TextView placeTextView = (TextView) getView().findViewById(R.id.home_place);
        TextView nameTextView = (TextView) getView().findViewById(R.id.home_business_name);

        String place = placeTextView.getText().toString();
        String name = nameTextView.getText().toString();
        Query query = new Query();
        query.setPlaceName(place);
        query.setBusinessName(name);
        if (query.isEmpty()){
            ActivityUtils.showInfoDialog(getContext(),R.string.home_empty_query_title, R.string.home_empty_query_message, R.string.ok);
            return;
        }

        if (getDataProvider().findEstablishments(query)){
            MainModel.get(getContext()).setSearchType(SearchType.quick);
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.showResults();
        }
    }
    private void placesNearMe(){
        if (getDataProvider().findLocalEstablishments()) {
            MainModel.get(getContext()).setSearchType(SearchType.local);
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.showResults();
        }
    }

    private void advancedSearch() {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.showAdvancedSearch();
    }
}
