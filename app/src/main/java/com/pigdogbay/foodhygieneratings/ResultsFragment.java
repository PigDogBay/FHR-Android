package com.pigdogbay.foodhygieneratings;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
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
import com.pigdogbay.lib.usercontrols.OnListItemClickedListener;
import com.pigdogbay.lib.utils.ObservableProperty;

import java.util.List;


@SuppressWarnings("ConstantConditions")
public class ResultsFragment extends Fragment implements OnListItemClickedListener<Establishment>, ObservableProperty.PropertyChangedObserver<AppState> {

    public static final String TAG = "results";

    private EstablishmentAdapter establishmentAdapter;

    private MainModel getMainModel(){
        return Injector.INSTANCE.getMainModel();
    }

    public ResultsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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
                getMainModel().setContainingTextFilter(newText);
                List<Establishment> results = getMainModel().getResults();
                List<Object> groupedResults = HeaderSectionConverter.INSTANCE.convert(results,getMainModel().getSearchType());
                establishmentAdapter.setEstablishments(groupedResults);
                establishmentAdapter.notifyDataSetChanged();
                updateTitle(results.size());
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            default:
                return super.onOptionsItemSelected(item);
        }
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
                List<Establishment> results = getMainModel().getResults();
                establishmentAdapter.setSearchType(getMainModel().getSearchType());
                List<Object> groupedResults = HeaderSectionConverter.INSTANCE.convert(results,getMainModel().getSearchType());
                establishmentAdapter.setEstablishments(groupedResults);
                establishmentAdapter.notifyDataSetChanged();
                updateTitle(results.size());
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
}
