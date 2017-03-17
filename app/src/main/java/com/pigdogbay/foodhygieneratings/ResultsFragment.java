package com.pigdogbay.foodhygieneratings;


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
import com.pigdogbay.lib.usercontrols.OnListItemClickedListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ResultsFragment extends Fragment implements OnListItemClickedListener {

    public static final String TAG = "results";

    private ResultsAdapter resultsAdapter;


    public ResultsFragment() {
        // Required empty public constructor
    }


    List<Establishment> createData() {
        ArrayList<Establishment> establishments = new ArrayList<>();
        establishments.add(new Establishment("Riverside Fish Bar", "4 Riverside Road, Trent Vale, Stoke on Trent"));
        establishments.add(new Establishment("Sangams", "14 Glebe Street, Stoke on Trent"));
        return establishments;
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
        resultsAdapter = new ResultsAdapter(createData(),this);
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);
        recyclerView.setAdapter(resultsAdapter);
    }

    @Override
    public void onListItemClicked(Object item, int position) {

    }
}
