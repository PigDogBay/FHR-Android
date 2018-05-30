package com.pigdogbay.foodhygieneratings;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.pigdogbay.foodhygieneratings.model.MainModel;
import com.pigdogbay.foodhygieneratings.model.Query;
import com.pigdogbay.foodhygieneratings.model.SearchType;
import com.pigdogbay.lib.utils.ActivityUtils;

public class HomeFragment extends Fragment{


    public static final String TAG = "home";

    private MainModel getMainModel() {
        return MainModel.get(getContext());
    }

    private TextView placeTextView;
    private TextView nameTextView;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        wireUpControls(rootView);
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Food Hygiene Ratings");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_home, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                quickSearch();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void wireUpControls(View view) {
        placeTextView = view.findViewById(R.id.home_place);
        nameTextView = view.findViewById(R.id.home_business_name);
        placeTextView.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (EditorInfo.IME_ACTION_SEARCH==i){
                quickSearch();
                return true;
            }
            return false;
        });

        view.findViewById(R.id.home_search_btn).setOnClickListener(view1 -> quickSearch());

        //Vector drawables are still a pain in the ass
        //http://stackoverflow.com/questions/35761636/is-it-possible-to-use-vectordrawable-in-buttons-and-textviews-using-androiddraw
        Button advancedButton = view.findViewById(R.id.home_advanced_search_btn);
        Drawable searchIcon = ContextCompat.getDrawable(getContext(),R.drawable.ic_search_green_48);
        advancedButton.setCompoundDrawablesRelativeWithIntrinsicBounds(searchIcon,null,null,null);
        advancedButton.setOnClickListener(view15 -> advancedSearch());

        Button nearMeButton = view.findViewById(R.id.home_places_near_me_btn);
        Drawable nearMeIcon = ContextCompat.getDrawable(getContext(),R.drawable.ic_near_me);
        nearMeButton.setCompoundDrawablesRelativeWithIntrinsicBounds(nearMeIcon,null,null,null);
        nearMeButton.setOnClickListener(view14 -> placesNearMe());

        view.findViewById(R.id.home_business_clear).setOnClickListener(view12 -> nameTextView.setText(""));
        view.findViewById(R.id.home_place_clear).setOnClickListener(view13 -> placeTextView.setText(""));
    }

    private void quickSearch() {
        String place = placeTextView.getText().toString();
        String name = nameTextView.getText().toString();
        Query query = new Query();
        query.setPlaceName(place);
        query.setBusinessName(name);
        if (query.isEmpty()) {
            ActivityUtils.showInfoDialog(getContext(), R.string.home_empty_query_title, R.string.home_empty_query_message, R.string.ok);
            return;
        }

        if (getMainModel().findEstablishments(query)) {
            MainModel.get(getContext()).setSearchType(SearchType.quick);
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.showResults();
        }
    }

    private void placesNearMe() {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.findLocalEstablishments();
    }

    private void advancedSearch() {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.showAdvancedSearch();
    }

}
