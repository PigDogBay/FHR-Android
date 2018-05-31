package com.pigdogbay.foodhygieneratings.model;

import android.content.Context;

import com.pigdogbay.foodhygieneratings.R;
import com.pigdogbay.lib.utils.ActivityUtils;
import com.pigdogbay.lib.utils.ObservableProperty;
import com.pigdogbay.lib.utils.PreferencesHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Created by Mark on 20/03/2017.
 * See P169 of Big Ranch Guide (Singletons and Centralized data storage)
 *
 */
public class MainModel {

    interface IDataProvider {
        List<Establishment> findEstablishments(Query query) throws IOException, JSONException, ParseException;
    }

    private static MainModel mainModel;
    private IDataProvider dataProvider;
    private Context appContext;
    private List<LocalAuthority> localAuthorities;
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


    public static MainModel get(Context c){
        if (mainModel==null){
            //According to big nerd ranch, application context is global to the application and so this should be used for singletons
            //This is no good for testing, so I make the constructor public!
            //https://books.google.co.uk/books?id=OFXJXbCXjTgC&pg=PA169&lpg=PA169&dq=big+nerd+ranch+model+singleton&source=bl&ots=Os93xEil5A&sig=At6cN7kY-IlTdeR7F5sNxEB129c&hl=en&sa=X&ved=0ahUKEwiwpJ_VlIXPAhVoDsAKHbrvAKYQ6AEIQDAF#v=onepage&q=big%20nerd%20ranch%20model%20singleton&f=false
            mainModel = new MainModel(c.getApplicationContext());
        }
        return mainModel;
    }

    /**
     * Public so that apps can pass in a test context
     * See Uncle Bob
     * https://8thlight.com/blog/uncle-bob/2015/06/30/the-little-singleton.html
     * @param appContext - activity context
     */
    public MainModel(Context appContext) {
        this.appContext = appContext;
        dummyData();
        //dataProvider = new WebDataProvider();
        searchType = SearchType.local;
        appStateObservableProperty = new ObservableProperty<>(AppState.ready);
        results = new ArrayList<>();
        filteredResults = new ArrayList<>();
    }

    private void dummyData(){
        try {
            String data = ActivityUtils.readResource(appContext, R.raw.stoke);
            dataProvider = new DummyDataProvider(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<LocalAuthority> getLocalAuthorities(){
        if (localAuthorities==null) {
            try {
                String data = ActivityUtils.readResource(appContext, R.raw.authorities);
                JSONObject jsonObject = new JSONObject(data);
                localAuthorities = FoodHygieneAPI.parseAuthorities(jsonObject);
            } catch (Exception e) {
                e.printStackTrace();
                localAuthorities =  new ArrayList<>();
            }
        }
        return localAuthorities;
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
        builder.append(appContext.getString(R.string.web_app_url));
        builder.append("\n\nAnd iOS:\n");
        builder.append(appContext.getString(R.string.web_app_url_ios));
        builder.append("\n\n");
        return builder.toString();
    }

}
