package com.pigdogbay.foodhygieneratings;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.pigdogbay.foodhygieneratings.model.Establishment;
import com.pigdogbay.foodhygieneratings.model.AppState;
import com.pigdogbay.foodhygieneratings.model.MainModel;
import com.pigdogbay.lib.usercontrols.OnListItemClickedListener;
import com.pigdogbay.lib.utils.ObservableProperty;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
@SuppressWarnings("ConstantConditions")
public class ResultsFragment extends Fragment implements OnListItemClickedListener<Establishment>, ObservableProperty.PropertyChangedObserver<AppState> {

    public static final String TAG = "results";

    private ResultsAdapter resultsAdapter;

    private MainModel getMainModel(){
        return MainModel.get(getContext());
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
        resultsAdapter = new ResultsAdapter(this);
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);
        recyclerView.setAdapter(resultsAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_results, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_results_map:
                ((MainActivity) getActivity()).showMap();
                return true;
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
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                update(update);
            }
        });
    }

    private void update(AppState state){
        switch (state){
            case ready:
                break;
            case requestingLocationAuthorization:
                break;
            case locating:
                break;
            case foundLocation:
                break;
            case notAuthorizedForLocating:
                break;
            case errorLocating:
                break;
            case loading:
                break;
            case loaded:
                reloadTable();
                break;
            case error:
                break;
        }
    }

    void reloadTable(){
        List<Establishment> results = getMainModel().getResults();
        if (results.size()>0){
            MainModel.get(getContext()).sortResults();
            resultsAdapter.clear();
            resultsAdapter.addItems(results);
            resultsAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onListItemClicked(Establishment item, int position) {
        MainModel.get(getContext()).setSelectedEstablishment(item);
        ((MainActivity) getActivity()).showDetails();
    }
}
