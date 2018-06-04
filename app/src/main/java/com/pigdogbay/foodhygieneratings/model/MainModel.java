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
    final private List<Establishment> filteredResults;
    private boolean isBusy = false;

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
        this.containingTextFilter = containingTextFilter;
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

    public boolean findEstablishments(final Query query) {
        if (isBusy){
            return false;
        }
        isBusy = true;
        results.clear();
        filteredResults.clear();
        containingTextFilter = "";
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
        if (appStateObservableProperty.getValue().equals(AppState.loaded) && results!=null){
            final String lowercaseText = containingText.toLowerCase();
            for (Establishment est : results){
                if (est.getBusiness().getName().toLowerCase().contains(lowercaseText)){
                    filteredResults.add(est);
                }
            }
        }
    }

    public List<Establishment> getResults()
    {
        filteredResults.clear();
        if ("".equals(containingTextFilter)){
            return results;
        } else {
            filterResults(containingTextFilter);
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
                builder.append(" * Hygiene Points: ").append(String.valueOf(scores.getHygiene())).append(" - ").append(scores.getHygieneDescription()).append("\n");
                builder.append(" * Management Points: ").append(String.valueOf(scores.getManagement())).append(" - ").append(scores.getManagementDescription()).append("\n");
                builder.append(" * Structural Points: ").append(String.valueOf(scores.getStructural())).append(" - ").append(scores.getStructuralDescription()).append("\n");
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
        builder.append("\nGet the food hygiene ratings app on Android:\n");
        builder.append("https://play.google.com/store/apps/details?id=com.pigdogbay.foodhygieneratings");
        builder.append("\n\nAnd iOS:\n");
        builder.append("https://itunes.apple.com/app/id1213783338");
        builder.append("\n\n");
        return builder.toString();
    }
}
