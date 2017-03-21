package com.pigdogbay.foodhygieneratings;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pigdogbay.foodhygieneratings.model.Establishment;
import com.pigdogbay.foodhygieneratings.model.FetchState;
import com.pigdogbay.foodhygieneratings.model.IDataProvider;
import com.pigdogbay.foodhygieneratings.model.MainModel;
import com.pigdogbay.lib.usercontrols.OnListItemClickedListener;
import com.pigdogbay.lib.utils.ObservableProperty;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ResultsFragment extends Fragment implements OnListItemClickedListener, ObservableProperty.PropertyChangedObserver<FetchState> {

    public static final String TAG = "results";

    private ResultsAdapter resultsAdapter;

    private IDataProvider getDataProvider(){
        return MainModel.get(getContext()).getDataProvider();
    }

    public ResultsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
    public void onResume() {
        super.onResume();
        getDataProvider().getFetchStateProperty().addObserver(this);
        update(getDataProvider().getFetchStateProperty().getValue());
    }

    @Override
    public void onPause() {
        super.onPause();
        getDataProvider().getFetchStateProperty().removeObserver(this);
    }

    @Override
    public void onListItemClicked(Object item, int position) {

    }

    @Override
    public void update(ObservableProperty<FetchState> sender, final FetchState update) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                update(update);
            }
        });
    }

    private void update(FetchState state){
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
        List<Establishment> results = getDataProvider().getResults();
        if (results.size()>0){
            resultsAdapter.clear();
            resultsAdapter.addItems(results);
            resultsAdapter.notifyDataSetChanged();
        }

    }
}
