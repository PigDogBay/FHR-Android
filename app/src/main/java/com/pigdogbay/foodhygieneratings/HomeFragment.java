package com.pigdogbay.foodhygieneratings;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.pigdogbay.foodhygieneratings.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public static final String TAG = "home";

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
                search();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void wireUpControls(View view){
        view.findViewById(R.id.home_search_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
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

    private void placesNearMe(){
        search();
    }

    private void advancedSearch() {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.showAdvancedSearch();
    }

    private void search() {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.showResults();

    }
}
