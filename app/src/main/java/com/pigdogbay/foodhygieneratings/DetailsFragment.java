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
import com.pigdogbay.foodhygieneratings.cards.PhotoCard;
import com.pigdogbay.foodhygieneratings.cards.PlaceCard;
import com.pigdogbay.foodhygieneratings.cards.RatingCard;
import com.pigdogbay.foodhygieneratings.cards.ScoresCard;
import com.pigdogbay.foodhygieneratings.model.Establishment;
import com.pigdogbay.foodhygieneratings.model.FoodHygieneAPI;
import com.pigdogbay.foodhygieneratings.model.Injector;
import com.pigdogbay.foodhygieneratings.model.FetchStatus;
import com.pigdogbay.foodhygieneratings.model.IPlaceFetcher;
import com.pigdogbay.foodhygieneratings.model.IPlaceImage;
import com.pigdogbay.foodhygieneratings.model.MBPlace;
import com.pigdogbay.lib.patterns.PropertyChangedObserver;
import com.pigdogbay.lib.utils.ActivityUtils;

import java.util.ArrayList;

public class DetailsFragment extends Fragment implements OnButtonClickListener,
        PropertyChangedObserver<FetchStatus> {

    public static final String TAG = "details";
    private static final int PLACE_CARD_INDEX = 1;
    private CardsAdapter cardsAdapter;
    Establishment establishment;
    private IPlaceFetcher placeFetcher;
    private ArrayList<ICard> cards;
    private MBPlace place;
    public DetailsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        establishment = Injector.mainModel.getSelectedEstablishment();
        placeFetcher = Injector.INSTANCE.createFetcher(getActivity());
        cards = new ArrayList<>();
        if (establishment!=null) {
            cards.add(new RatingCard(establishment, this));
            if (establishment.getRating().hasScores()) {
                cards.add(new ScoresCard(establishment, this));
            }
            cards.add(new AddressCard(establishment, this));
            cards.add(new LocalAuthorityCard(establishment, this));
        }
        cardsAdapter = new CardsAdapter(cards);
        placeFetcher.getObservableStatus().addObserver(this);
        placeFetcher.fetch(establishment);
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
        if (getActivity()!=null) {
            getActivity().setTitle(establishment.getBusiness().getName());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        placeFetcher.getObservableStatus().removeObserver(this);
        if (place!=null){
            for (IPlaceImage img : place.getImages()){
                img.getObservableStatus().removeObserver(this);
            }
        }
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
        if (getActivity()==null) return;
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
            if (getActivity()==null) return;
            ActivityUtils.shareText(getActivity(), "Rating: "+establishment.getBusiness().getName(),
                    Injector.mainModel.getShareText(establishment),
                    R.string.share_chooser_title);
        }
        catch (Exception e) {
            Toast.makeText(getContext(), "Unable to share", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateMBPlace(FetchStatus update){
        switch (update){
            case Uninitialized:
                break;
            case Fetching:
                break;
            case Ready:
                onPlaceCreated(placeFetcher.getMbPlace());
                break;
            case Error:
                Toast.makeText(getActivity(),"No Place Details",Toast.LENGTH_SHORT).show();
                break;
        }
    }
    private void updatePlaceImage(FetchStatus update, int index){
        switch (update){
            case Uninitialized:
                break;
            case Fetching:
                break;
            case Ready:
                cardsAdapter.notifyItemChanged(index);
                break;
            case Error:
                cardsAdapter.notifyItemChanged(index);
                break;
        }
    }
    private void onPlaceCreated(MBPlace place) {
        this.place = place;
        for (IPlaceImage img : place.getImages()) {
            img.getObservableStatus().addObserver(this);
        }

        cards.add(PLACE_CARD_INDEX,new PlaceCard(place));
        cardsAdapter.notifyItemInserted(PLACE_CARD_INDEX);

        int photoCount = place.getImages().size();
        if (photoCount>0){
            place.getImages().get(0).setIndex(PLACE_CARD_INDEX);
        }
        if (photoCount>1){
            int startPosition = cards.size();
            //add cards for the extra images
            for (int i=1; i<photoCount;i++){
                IPlaceImage img = place.getImages().get(i);
                PhotoCard photoCard = new PhotoCard(img);
                cards.add(photoCard);
                img.setIndex(cards.size()-1);
            }
            cardsAdapter.notifyItemRangeChanged(startPosition,photoCount);
        }
    }


    @Override
    public void update(@NonNull Object sender, FetchStatus update) {
        if (getActivity()==null) return;
        getActivity().runOnUiThread(()->{
            if (getActivity()==null) return;
            if (sender instanceof IPlaceFetcher) {
                updateMBPlace(update);
            } else {
                updatePlaceImage(update, ((IPlaceImage)sender).getIndex());
            }
        });
    }
}
