package com.pigdogbay.foodhygieneratings;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.SearchView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.pigdogbay.foodhygieneratings.model.Establishment;
import com.pigdogbay.foodhygieneratings.model.AppState;
import com.pigdogbay.foodhygieneratings.model.HeaderSectionConverter;
import com.pigdogbay.foodhygieneratings.model.Injector;
import com.pigdogbay.foodhygieneratings.model.MainModel;
import com.pigdogbay.foodhygieneratings.model.RatingValue;
import com.pigdogbay.lib.usercontrols.OnListItemClickedListener;
import com.pigdogbay.lib.utils.ObservableProperty;

import java.util.List;


@SuppressWarnings("ConstantConditions")
public class ResultsFragment extends Fragment implements OnListItemClickedListener<Establishment>, ObservableProperty.PropertyChangedObserver<AppState> {

    public static final String TAG = "results";

    private MenuItem menu_checked, menu_all;
    private EstablishmentAdapter establishmentAdapter;

    private MainModel getMainModel(){
        return Injector.INSTANCE.getMainModel();
    }

    public ResultsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.recyler_view, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        establishmentAdapter = new EstablishmentAdapter(this);
        RecyclerView recyclerView = getView().findViewById(R.id.recycler_view);
        recyclerView.setAdapter(establishmentAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_results, menu);
        menu_all = menu.findItem(R.id.menu_results_filter_all);
        modelToMenu(menu);

        MenuItem searchItem = menu.findItem(R.id.menu_results_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        searchView.setQuery(getMainModel().getContainingTextFilter(),false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Reset rating filter
                if (!menu_all.isChecked()){
                    menu_all.setChecked(true);
                    menu_checked.setChecked(false);
                    menu_checked = menu_all;
                }
                getMainModel().setContainingTextFilter(newText);
                updateResults();
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.isCheckable() && !item.isChecked())
        {
            item.setChecked(true);
            menu_checked.setChecked(false);
            menu_checked = item;
        }
        switch (item.getItemId())
        {
            case R.id.menu_results_filter_all:
                getMainModel().setRatingFilter(null);
                break;
            case R.id.menu_results_filter_0_stars:
                getMainModel().setRatingFilter(RatingValue.ratingOf0);
                break;
            case R.id.menu_results_filter_1_stars:
                getMainModel().setRatingFilter(RatingValue.ratingOf1);
                break;
            case R.id.menu_results_filter_2_stars:
                getMainModel().setRatingFilter(RatingValue.ratingOf2);
                break;
            case R.id.menu_results_filter_3_stars:
                getMainModel().setRatingFilter(RatingValue.ratingOf3);
                break;
            case R.id.menu_results_filter_4_stars:
                getMainModel().setRatingFilter(RatingValue.ratingOf4);
                break;
            case R.id.menu_results_filter_5_stars:
                getMainModel().setRatingFilter(RatingValue.ratingOf5);
                break;
            case R.id.menu_results_filter_improvement_required:
                getMainModel().setRatingFilter(RatingValue.fhis_improvementRequired);
                break;
            case R.id.menu_results_filter_pass:
                getMainModel().setRatingFilter(RatingValue.fhis_pass);
                break;
            case R.id.menu_results_filter_pass_eat_safe:
                getMainModel().setRatingFilter(RatingValue.fhis_passEatSafe);
                break;
            case R.id.menu_results_filter_not_rated:
                getMainModel().setRatingFilter(RatingValue.other);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        updateResults();
        return true;
    }

    private void modelToMenu(Menu menu){
        //Switch can not handle null (used for all)
        if (getMainModel().getRatingFilter()==null){
            menu_checked = menu_all;
            menu_checked.setChecked(true);
            return;
        }
        switch (getMainModel().getRatingFilter()){
            case ratingOf0:
                menu_checked = menu.findItem(R.id.menu_results_filter_0_stars);
                break;
            case ratingOf1:
                menu_checked = menu.findItem(R.id.menu_results_filter_1_stars);
                break;
            case ratingOf2:
                menu_checked = menu.findItem(R.id.menu_results_filter_2_stars);
                break;
            case ratingOf3:
                menu_checked = menu.findItem(R.id.menu_results_filter_3_stars);
                break;
            case ratingOf4:
                menu_checked = menu.findItem(R.id.menu_results_filter_4_stars);
                break;
            case ratingOf5:
                menu_checked = menu.findItem(R.id.menu_results_filter_5_stars);
                break;
            case fhis_pass:
                menu_checked = menu.findItem(R.id.menu_results_filter_pass);
                break;
            case fhis_passEatSafe:
                menu_checked = menu.findItem(R.id.menu_results_filter_pass_eat_safe);
                break;
            case fhis_improvementRequired:
                menu_checked = menu.findItem(R.id.menu_results_filter_improvement_required);
                break;
            case other:
                menu_checked = menu.findItem(R.id.menu_results_filter_not_rated);
                break;
        }
        menu_checked.setChecked(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        getMainModel().getAppStateProperty().addObserver(this);
        update(getMainModel().getAppStateProperty().getValue());
    }

    @Override
    public void onPause() {
        super.onPause();
        getMainModel().getAppStateProperty().removeObserver(this);
    }

    @Override
    public void update(ObservableProperty<AppState> sender, final AppState update) {
        getActivity().runOnUiThread(() -> update(update));
    }

    private void update(AppState state){
        switch (state){
            case ready:
                break;
            case loading:
                getActivity().setTitle("Loading...");
                break;
            case loaded:
                updateResults();
                break;
            case connectionError:
                getActivity().setTitle("No Connection");
                break;
            case error:
                getActivity().setTitle("Search Error");
                break;
        }
    }

    @Override
    public void onListItemClicked(Establishment item, int position) {
        getMainModel().setSelectedEstablishment(item);
        ((MainActivity) getActivity()).showDetails();
    }

    private void updateTitle(int resultsFound){
        getActivity().setTitle(resultsFound+" Results Found");
    }

    private void updateResults(){
        List<Establishment> results = getMainModel().getResults();
        establishmentAdapter.setSearchType(getMainModel().getSearchType());
        List<Object> groupedResults = HeaderSectionConverter.INSTANCE.convert(results,getMainModel().getSearchType());
        establishmentAdapter.setEstablishments(groupedResults);
        establishmentAdapter.notifyDataSetChanged();
        updateTitle(results.size());
    }
}
