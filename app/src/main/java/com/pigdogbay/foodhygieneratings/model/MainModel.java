package com.pigdogbay.foodhygieneratings.model;

import com.pigdogbay.lib.utils.ObservableProperty;
import org.json.JSONException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Mark on 20/03/2017.
 */
public class MainModel {

    public interface IDataProvider {
        void setTimeout(int timeout);
        List<Establishment> findEstablishments(Query query) throws IOException, JSONException, ParseException;
    }

    private IDataProvider dataProvider;
    private SearchType searchType;
    private Establishment selectedEstablishment;
    private ObservableProperty<AppState> appStateObservableProperty;
    private List<Establishment> results;
    private String containingTextFilter = "";
    private RatingValue ratingFilter = null;
    final private List<Establishment> filteredResults;
    private boolean isBusy = false;
    private boolean isTextFilterByNameAndAddress = false;

    public ObservableProperty<AppState> getAppStateProperty() {
        return appStateObservableProperty;
    }

    public IDataProvider getDataProvider() {
        return dataProvider;
    }
    public void setDataProvider(IDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }
    public boolean isBusy() {
        return isBusy;
    }
    public SearchType getSearchType() {
        return searchType;
    }
    public void setSearchType(SearchType searchType) {
        this.searchType = searchType;
    }
    public Establishment getSelectedEstablishment() {
        return selectedEstablishment;
    }
    public void setSelectedEstablishment(Establishment selectedEstablishment) {
        this.selectedEstablishment = selectedEstablishment;
    }
    public String getContainingTextFilter() {
        return containingTextFilter;
    }
    public void setContainingTextFilter(String containingTextFilter) {
        this.ratingFilter = null;
        this.containingTextFilter = containingTextFilter;
    }
    public RatingValue getRatingFilter() {
        return ratingFilter;
    }
    public void setRatingFilter(RatingValue ratingFilter) {
        this.containingTextFilter = "";
        this.ratingFilter = ratingFilter;
    }
    public void setTextFilterByNameAndAddress(boolean textFilterByNameAndAddress) {
        isTextFilterByNameAndAddress = textFilterByNameAndAddress;
    }

    public MainModel() {
        searchType = SearchType.local;
        appStateObservableProperty = new ObservableProperty<>(AppState.ready);
        results = new ArrayList<>();
        filteredResults = new ArrayList<>();
    }

    private void sortResults() {
        switch (searchType){
            case local:
                sortByDistance();
                break;
            case quick:
                break;
            case advanced:
                break;
            case map:
                break;
        }
    }

    private void sortByDistance(){
        if (results!=null && results.size()>1){
            Collections.sort(results, (establishment, t1) -> Double.compare(establishment.getDistance(),t1.getDistance()));
        }
    }

    public void clearResults(){
        results.clear();
        filteredResults.clear();
        ratingFilter = null;
        containingTextFilter = "";
    }

    public boolean findEstablishments(final Query query) {
        if (isBusy){
            return false;
        }
        isBusy = true;
        clearResults();
        appStateObservableProperty.setValue(AppState.loading);
        new Thread(() -> {
            try {
                results = dataProvider.findEstablishments(query);
                sortResults();
                appStateObservableProperty.setValue(AppState.loaded);
            } catch (IOException ioe){
                appStateObservableProperty.setValue(AppState.connectionError);
            } catch (Exception e) {
                appStateObservableProperty.setValue(AppState.error);
            }
            isBusy = false;
        }).start();
        return true;
    }

    private void filterResults(final String containingText){
        final String lowercaseText = containingText.toLowerCase();
        for (Establishment est : results){
            if (est.getBusiness().getName().toLowerCase().contains(lowercaseText)){
                filteredResults.add(est);
            } else if (isTextFilterByNameAndAddress && est.getAddress().flatten().toLowerCase().contains(lowercaseText)){
                filteredResults.add(est);
            }
        }
    }

    private void filterResults(RatingValue ratingValue){
        if (RatingValue.other == ratingValue){
            for (Establishment est : results) {
                if (!est.getRating().getRatingValue().isRated()) {
                    filteredResults.add(est);
                }
            }
        } else {
            for (Establishment est : results) {
                if (est.getRating().getRatingValue() == ratingValue) {
                    filteredResults.add(est);
                }
            }
        }
    }

    /*
        Will filter by containing text, or filter by rating value or just return all results.
        Note the filters are not chained
     */
    public List<Establishment> getResults()
    {
        filteredResults.clear();
        if (appStateObservableProperty.getValue().equals(AppState.loaded) && results!=null) {
            if (containingTextFilter!=null && containingTextFilter.length()>0) {
                filterResults(containingTextFilter);
            } else if (ratingFilter!=null){
                filterResults(ratingFilter);
            } else {
                //no filters
                return results;
            }
        }
        return filteredResults;
    }

    public String getShareText(Establishment establishment){
        StringBuilder builder = new StringBuilder("Food Hygiene Rating\n\n");
        builder.append(establishment.getBusiness().getName()).append("\n");
        builder.append(establishment.getAddress().flatten()).append("\n");
        builder.append("\n Rating: ").append(establishment.getRating().getName()).append("\n");
        if (establishment.getRating().hasRating()){
            String dateString = DateFormat.getDateInstance().format(establishment.getRating().getAwardedDate());
            builder.append("Awarded: ").append(dateString).append("\n");
            if (establishment.getRating().hasScores()) {
                builder.append("\nScores\n");
                final Scores scores = establishment.getRating().getScores();
                builder.append(" * Hygiene: ").append(scores.getHygieneDescription()).append("\n");
                builder.append(" * Management: ").append(scores.getManagementDescription()).append("\n");
                builder.append(" * Structural: ").append(scores.getStructuralDescription()).append("\n");
            }
        }
        LocalAuthority la = establishment.getLocalAuthority();
        if (la!=null) {
            builder.append("\nLocal Authority Details\n");
            builder.append(la.getName()).append("\n");
            builder.append("Email: ").append(la.getEmail()).append("\n");
            builder.append("Website: ").append(la.getWeb()).append("\n");
        }

        builder.append("\nView this rating on the FSA Website\n").append(FoodHygieneAPI.createBusinessUrl(establishment)).append("\n");
        builder.append("\nGet the food hygiene ratings app on iOS:\n");
        builder.append("https://itunes.apple.com/app/id1213783338");
        builder.append("\n\nAndroid:\n");
        builder.append("https://play.google.com/store/apps/details?id=com.pigdogbay.foodhygieneratings");
        builder.append("\n\n");
        return builder.toString();
    }
}
