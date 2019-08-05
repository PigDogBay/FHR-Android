package com.pigdogbay.foodhygieneratings;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
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
import com.pigdogbay.foodhygieneratings.model.Injector;
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
    private MainModel mainModel;

    public AdvancedSearchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        mainModel = Injector.INSTANCE.getMainModel();
        View view = inflater.inflate(R.layout.fragment_advanced_search, container, false);
        wireUpControls(view);
        return view;
    }

    private void wireUpControls(View view){
        placeTextView = view.findViewById(R.id.advanced_place_text);
        placeTextView.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (EditorInfo.IME_ACTION_SEARCH==i){
                search();
                return true;
            }
            return false;
        });
        view.findViewById(R.id.advanced_name_clear).setOnClickListener(view1 -> nameTextView.setText(""));

        nameTextView = view.findViewById(R.id.advanced_business_text);
        view.findViewById(R.id.advanced_place_clear).setOnClickListener(view12 -> placeTextView.setText(""));

        businessTypeSpinner = view.findViewById(R.id.advanced_business_spinner);
        areaSpinner = view.findViewById(R.id.advanced_area_spinner);
        ratingSpinner = view.findViewById(R.id.advanced_rating_spinner);
        ratingOperatorSpinner = view.findViewById(R.id.advanced_rating_operator_spinner);
        statusSpinner = view.findViewById(R.id.advanced_status_spinner);

        ArrayAdapter<LocalAuthority> localAuthorityArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
        localAuthorityArrayAdapter.add(LocalAuthority.getALL());
        localAuthorityArrayAdapter.addAll(Injector.INSTANCE.getLocalAuthorities(getContext()));
        localAuthorityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        areaSpinner.setAdapter(localAuthorityArrayAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Advanced Search");
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
        if (mainModel.findEstablishments(query)){
            mainModel.setSearchType(SearchType.advanced);
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.showResults();
        }
    }
}
