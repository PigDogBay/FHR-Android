package com.pigdogbay.foodhygieneratings;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.pigdogbay.foodhygieneratings.cards.AddressCard;
import com.pigdogbay.foodhygieneratings.cards.CardsAdapter;
import com.pigdogbay.foodhygieneratings.cards.ICard;
import com.pigdogbay.foodhygieneratings.cards.LocalAuthorityCard;
import com.pigdogbay.foodhygieneratings.cards.OnButtonClickListener;
import com.pigdogbay.foodhygieneratings.cards.RatingCard;
import com.pigdogbay.foodhygieneratings.cards.ScoresCard;
import com.pigdogbay.foodhygieneratings.model.Establishment;
import com.pigdogbay.foodhygieneratings.model.FoodHygieneAPI;
import com.pigdogbay.foodhygieneratings.model.Injector;
import com.pigdogbay.lib.utils.ActivityUtils;

import java.util.ArrayList;
import java.util.List;

public class DetailsFragment extends Fragment implements OnButtonClickListener {

    public static final String TAG = "details";
    private CardsAdapter cardsAdapter;
    Establishment establishment;

    public DetailsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        establishment = Injector.mainModel.getSelectedEstablishment();
        List<ICard> cards = new ArrayList<>();
        if (establishment!=null) {
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.recyler_view, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(cardsAdapter);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(establishment.getBusiness().getName());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_details, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_details_share:
                share();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onButtonPressed(int id, String[] args) {
        MainActivity mainActivity = (MainActivity) getActivity();
        switch (id){
            case R.id.card_business_website_button:
                String url = FoodHygieneAPI.createBusinessUrl(establishment);
                ActivityUtils.ShowWebPage(getActivity(),url);
                break;
            case R.id.card_address_map_button:
                mainActivity.showEstablishmentMap();
                break;
            case R.id.card_la_email_button:
                String email = establishment.getLocalAuthority().getEmail();
                ActivityUtils.SendEmail(getActivity(),new String[]{email},"Food Hygiene Rating",Injector.mainModel.getShareText(establishment));
                break;
            case R.id.card_la_web_button:
                ActivityUtils.ShowWebPage(getActivity(),establishment.getLocalAuthority().getWeb());
                break;
            case R.id.card_scores_info_button:
                mainActivity.showHtmlText(R.string.scores_description);
                break;
        }
    }

    private void share() {
        try {
            ActivityUtils.shareText(getActivity(), "Rating: "+establishment.getBusiness().getName(),
                    Injector.mainModel.getShareText(establishment),
                    R.string.share_chooser_title);
        }
        catch (Exception e) {
            Toast.makeText(getContext(), "Unable to share", Toast.LENGTH_SHORT).show();
        }
    }

}
