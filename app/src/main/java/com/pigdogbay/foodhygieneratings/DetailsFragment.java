package com.pigdogbay.foodhygieneratings;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pigdogbay.foodhygieneratings.cards.AddressCard;
import com.pigdogbay.foodhygieneratings.cards.BusinessCard;
import com.pigdogbay.foodhygieneratings.cards.CardsAdapter;
import com.pigdogbay.foodhygieneratings.cards.ICard;
import com.pigdogbay.foodhygieneratings.cards.LocalAuthorityCard;
import com.pigdogbay.foodhygieneratings.cards.OnButtonClickListener;
import com.pigdogbay.foodhygieneratings.cards.RatingCard;
import com.pigdogbay.foodhygieneratings.cards.ScoresCard;
import com.pigdogbay.foodhygieneratings.model.Establishment;
import com.pigdogbay.foodhygieneratings.model.FoodHygieneAPI;
import com.pigdogbay.foodhygieneratings.model.MainModel;
import com.pigdogbay.lib.utils.ActivityUtils;

import java.util.ArrayList;
import java.util.List;

public class DetailsFragment extends Fragment implements OnButtonClickListener {

    public static final String TAG = "details";
    private CardsAdapter cardsAdapter;
    Establishment establishment;

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        establishment = MainModel.get(getContext()).getSelectedEstablishment();
        List<ICard> cards = new ArrayList<>();
        if (establishment!=null) {
            cards.add(new BusinessCard(establishment, this));
            cards.add(new RatingCard(establishment, this));
            if (establishment.getRating().hasScores()) {
                cards.add(new ScoresCard(establishment, this));
            }
            cards.add(new AddressCard(establishment, this));
            cards.add(new LocalAuthorityCard(establishment, this));
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


    @Override
    public void onButtonPressed(int id, String args) {
        MainActivity mainActivity = (MainActivity) getActivity();
        switch (id){
            case R.id.card_business_website_button:
                String url = FoodHygieneAPI.createBusinessUrl(establishment);
                ActivityUtils.ShowWebPage(getActivity(),url);
                break;
            case R.id.card_address_map_button:
                break;
            case R.id.card_la_email_button:
                String email = establishment.getLocalAuthority().getEmail();
                ActivityUtils.SendEmail(getActivity(),new String[]{email},"","");
                break;
            case R.id.card_la_web_button:
                ActivityUtils.ShowWebPage(getActivity(),establishment.getLocalAuthority().getWeb());
                break;
            case R.id.card_scores_info_button:
                break;
        }
    }
}
