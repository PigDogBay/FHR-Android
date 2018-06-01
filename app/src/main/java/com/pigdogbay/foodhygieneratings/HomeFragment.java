package com.pigdogbay.foodhygieneratings;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.pigdogbay.foodhygieneratings.model.Injector;
import com.pigdogbay.foodhygieneratings.model.Query;
import com.pigdogbay.foodhygieneratings.model.SearchType;
import com.pigdogbay.lib.utils.ActivityUtils;

public class HomeFragment extends Fragment implements TextWatcher {


    public static final String TAG = "home";

    private TextView placeTextView;
    private TextView nameTextView;
    private View placeClearButton, nameClearButton;

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
        placeTextView.addTextChangedListener(this);
        nameTextView = view.findViewById(R.id.home_business_name);
        nameTextView.addTextChangedListener(this);
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

        nameClearButton = view.findViewById(R.id.home_business_clear);
        nameClearButton.setOnClickListener(view12 -> onNameFieldClearClicked());
        nameClearButton.setVisibility(View.GONE);
        placeClearButton = view.findViewById(R.id.home_place_clear);
        placeClearButton.setOnClickListener(view13 -> onPlaceFieldClearClicked());
        placeClearButton.setVisibility(View.GONE);
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

        if (Injector.getMainModel().findEstablishments(query)) {
            Injector.getMainModel().setSearchType(SearchType.quick);
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

    private void onNameFieldClearClicked(){
        nameTextView.setText("");
        nameTextView.requestFocus();
        ActivityUtils.showKeyboard(getActivity(),nameTextView);
    }
    private void onPlaceFieldClearClicked(){
        placeTextView.setText("");
        placeTextView.requestFocus();
        ActivityUtils.showKeyboard(getActivity(),placeTextView);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        int visibility = placeTextView.getText().length()>0 ? View.VISIBLE : View.GONE;
        placeClearButton.setVisibility(visibility);
        visibility = nameTextView.getText().length()>0 ? View.VISIBLE : View.GONE;
        nameClearButton.setVisibility(visibility);
    }
}
