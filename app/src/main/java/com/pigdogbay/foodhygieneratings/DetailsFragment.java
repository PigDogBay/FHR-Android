package com.pigdogbay.foodhygieneratings;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pigdogbay.foodhygieneratings.cards.AddressCard;
import com.pigdogbay.foodhygieneratings.cards.CardsAdapter;
import com.pigdogbay.foodhygieneratings.cards.ICard;
import com.pigdogbay.foodhygieneratings.cards.LocalAuthorityCard;
import com.pigdogbay.foodhygieneratings.cards.MapCard;
import com.pigdogbay.foodhygieneratings.cards.RatingCard;
import com.pigdogbay.foodhygieneratings.cards.ScoresCard;
import com.pigdogbay.foodhygieneratings.model.Establishment;
import com.pigdogbay.foodhygieneratings.model.MainModel;

import java.util.ArrayList;
import java.util.List;

public class DetailsFragment extends Fragment {

    public static final String TAG = "details";
    private CardsAdapter cardsAdapter;


    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Establishment establishment = MainModel.get(getContext()).getSelectedEstablishment();
        List<ICard> cards = new ArrayList<>();
        if (establishment!=null) {
            cards.add(new RatingCard());
            cards.add(new ScoresCard());
            cards.add(new AddressCard(establishment));
            cards.add(new LocalAuthorityCard());
        }
        cardsAdapter = new CardsAdapter(cards);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recyler_view, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(cardsAdapter);

        return rootView;
    }


}
