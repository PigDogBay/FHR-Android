package com.pigdogbay.foodhygieneratings;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
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
        return rootView;
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
