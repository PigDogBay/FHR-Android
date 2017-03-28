package com.pigdogbay.foodhygieneratings;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
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

/**
 * A simple {@link Fragment} subclass.
 */
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
        placeTextView = (TextView) view.findViewById(R.id.home_place);
        nameTextView = (TextView) view.findViewById(R.id.home_business_name);
        placeTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (EditorInfo.IME_ACTION_SEARCH==i){
                    quickSearch();
                    return true;
                }
                return false;
            }
        });

        view.findViewById(R.id.home_search_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quickSearch();
            }
        });

        //Vector drawables are still a pain in the ass
        //http://stackoverflow.com/questions/35761636/is-it-possible-to-use-vectordrawable-in-buttons-and-textviews-using-androiddraw
        Button advancedButton = (Button) view.findViewById(R.id.home_advanced_search_btn);
        Drawable searchIcon = ContextCompat.getDrawable(getContext(),R.drawable.ic_search_green_48);
        advancedButton.setCompoundDrawablesRelativeWithIntrinsicBounds(searchIcon,null,null,null);
        advancedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                advancedSearch();
            }
        });

        Button nearMeButton = (Button) view.findViewById(R.id.home_places_near_me_btn);
        Drawable nearMeIcon = ContextCompat.getDrawable(getContext(),R.drawable.ic_near_me);
        nearMeButton.setCompoundDrawablesRelativeWithIntrinsicBounds(nearMeIcon,null,null,null);
        nearMeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placesNearMe();
            }
        });

    }

    private void quickSearch() {
        ActivityUtils.hideKeyboard(getActivity(),placeTextView.getWindowToken());
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
        ActivityUtils.hideKeyboard(getActivity(),placeTextView.getWindowToken());
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.findLocalEstablishments();
    }

    private void advancedSearch() {
        ActivityUtils.hideKeyboard(getActivity(),placeTextView.getWindowToken());
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.showAdvancedSearch();
    }

}
