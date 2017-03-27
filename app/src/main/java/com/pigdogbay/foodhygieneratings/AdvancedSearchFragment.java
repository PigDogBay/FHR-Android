package com.pigdogbay.foodhygieneratings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.pigdogbay.foodhygieneratings.model.Business;
import com.pigdogbay.foodhygieneratings.model.LocalAuthority;
import com.pigdogbay.foodhygieneratings.model.MainModel;
import com.pigdogbay.foodhygieneratings.model.Query;
import com.pigdogbay.foodhygieneratings.model.SearchType;
import com.pigdogbay.lib.utils.ActivityUtils;

public class AdvancedSearchFragment extends Fragment {

    public static final String TAG = "advanced search";
    private static final String ALL = "All";

    private TextView placeTextView,nameTextView;
    private Spinner businessTypeSpinner, areaSpinner, ratingSpinner, ratingOperatorSpinner, statusSpinner;

    private MainModel getMainModel(){
        return MainModel.get(getContext());
    }

    public AdvancedSearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_advanced_search, container, false);
        wireUpControls(view);
        return view;
    }

    private void wireUpControls(View view){
        placeTextView = (TextView) view.findViewById(R.id.advanced_place_text);
        placeTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (EditorInfo.IME_ACTION_SEARCH==i){
                    search();
                    return true;
                }
                return false;
            }
        });
        nameTextView = (TextView) view.findViewById(R.id.advanced_business_text);
        businessTypeSpinner = (Spinner) view.findViewById(R.id.advanced_business_spinner);
        areaSpinner = (Spinner) view.findViewById(R.id.advanced_area_spinner);
        ratingSpinner = (Spinner) view.findViewById(R.id.advanced_rating_spinner);
        ratingOperatorSpinner = (Spinner) view.findViewById(R.id.advanced_rating_operator_spinner);
        statusSpinner = (Spinner) view.findViewById(R.id.advanced_status_spinner);

        ArrayAdapter<LocalAuthority> localAuthorityArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
        localAuthorityArrayAdapter.add(LocalAuthority.getALL());
        localAuthorityArrayAdapter.addAll(getMainModel().getLocalAuthorities());
        localAuthorityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        areaSpinner.setAdapter(localAuthorityArrayAdapter);

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


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_advanced_search,menu);
    }

    private Query createQuery(){
        String place = placeTextView.getText().toString();
        String name = nameTextView.getText().toString();
        String rating = (String) ratingSpinner.getSelectedItem();
        String comparison = (String) ratingOperatorSpinner.getSelectedItem();
        String status = (String) statusSpinner.getSelectedItem();
        LocalAuthority localAuthority = (LocalAuthority) areaSpinner.getSelectedItem();
        int business = Business.businessTypesIds[businessTypeSpinner.getSelectedItemPosition()];


        Query query = new Query();
        query.setPlaceName(place);
        query.setBusinessName(name);


        if (!ALL.equals(localAuthority.getName())){
            query.setLocalAuthorityId(String.valueOf(localAuthority.getId()));
        }
        if (!ALL.equals(rating)){
            query.setRatingKey(rating);
            query.setRatingOperatorKey(comparison);
        }
        if (business!=Business.businessTypesIds[0]){
            query.setBusinessTypeId(String.valueOf(business));
        }
        if (!ALL.equals(status)){
            status = status.replace(" ","");
            query.setRatingKey(status);
        }
        return query;

    }

    private void search() {
        ActivityUtils.hideKeyboard(getActivity(),placeTextView.getWindowToken());
        Query query = createQuery();
        if (getMainModel().findEstablishments(query)){
            MainModel.get(getContext()).setSearchType(SearchType.advanced);
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.showResults();
        }

    }

}
