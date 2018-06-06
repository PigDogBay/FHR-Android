package com.pigdogbay.foodhygieneratings;
import com.pigdogbay.foodhygieneratings.model.AppState;
import com.pigdogbay.foodhygieneratings.model.DummyDataProvider;
import com.pigdogbay.foodhygieneratings.model.Establishment;
import com.pigdogbay.foodhygieneratings.model.MainModel;
import com.pigdogbay.foodhygieneratings.model.Query;
import com.pigdogbay.foodhygieneratings.model.RatingValue;
import com.pigdogbay.lib.utils.ObservableProperty;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.*;

public class MainModelTest implements ObservableProperty.PropertyChangedObserver<AppState> {

    private static final String STOKE_RESOURCE = "stoke.json";

    private MainModel.IDataProvider getDataProvider(String resourePath) throws IOException {
        String path = getClass().getClassLoader().getResource(resourePath).getPath();
        String json = new String(Files.readAllBytes(Paths.get(path)));
        return new DummyDataProvider(json);
    }

    private MainModel createMainModel() throws IOException {
        MainModel mainModel = new MainModel();
        mainModel.setDataProvider(getDataProvider(STOKE_RESOURCE));
        return mainModel;

    }

    @Test
    synchronized public void findEstablishments1() throws IOException, InterruptedException {
        MainModel mainModel = createMainModel();
        mainModel.getAppStateProperty().addObserver(this);
        mainModel.findEstablishments(new Query());
        wait();
        assertEquals(AppState.loaded,mainModel.getAppStateProperty().getValue());
        List<Establishment> results =  mainModel.getResults();
        assertEquals(99, results.size());

        mainModel.getAppStateProperty().removeObserver(this);
    }
    /*
       Set containing text filter "fish"
     */
    @Test
    synchronized public void findEstablishments2() throws IOException, InterruptedException {
        MainModel mainModel = createMainModel();
        mainModel.getAppStateProperty().addObserver(this);
        mainModel.findEstablishments(new Query());
        wait();
        assertEquals(AppState.loaded,mainModel.getAppStateProperty().getValue());
        mainModel.setContainingTextFilter("fish");
        List<Establishment> results =  mainModel.getResults();
        assertEquals(3, results.size());
        assertEquals("Riverside Fish Bar", results.get(0).getBusiness().getName());
        mainModel.getAppStateProperty().removeObserver(this);
    }
    /*
       Set containing text filter "stone" and search in both name and address
     */
    @Test
    synchronized public void findEstablishments3() throws IOException, InterruptedException {
        MainModel mainModel = createMainModel();
        mainModel.getAppStateProperty().addObserver(this);
        mainModel.findEstablishments(new Query());
        wait();
        assertEquals(AppState.loaded,mainModel.getAppStateProperty().getValue());
        mainModel.setTextFilterByNameAndAddress(true);
        mainModel.setContainingTextFilter("stone");
        List<Establishment> results =  mainModel.getResults();
        assertEquals(10, results.size());
        assertEquals("Riverside Fish Bar", results.get(2).getBusiness().getName());
        mainModel.getAppStateProperty().removeObserver(this);
    }
    /*
       Set rating filter to 1
     */
    @Test
    synchronized public void findEstablishments4() throws IOException, InterruptedException {
        MainModel mainModel = createMainModel();
        mainModel.getAppStateProperty().addObserver(this);
        mainModel.findEstablishments(new Query());
        wait();
        assertEquals(AppState.loaded,mainModel.getAppStateProperty().getValue());
        mainModel.setRatingFilter(RatingValue.ratingOf1);
        List<Establishment> results =  mainModel.getResults();
        assertEquals(5, results.size());
        assertEquals("Aroma", results.get(0).getBusiness().getName());
        mainModel.getAppStateProperty().removeObserver(this);
    }
    /*
       Set rating filter to other
     */
    @Test
    synchronized public void findEstablishments5() throws IOException, InterruptedException {
        MainModel mainModel = createMainModel();
        mainModel.getAppStateProperty().addObserver(this);
        mainModel.findEstablishments(new Query());
        wait();
        assertEquals(AppState.loaded,mainModel.getAppStateProperty().getValue());
        mainModel.setRatingFilter(RatingValue.other);
        List<Establishment> results =  mainModel.getResults();
        assertEquals(9, results.size());
        assertEquals("St John's Community Centre", results.get(1).getBusiness().getName());
        mainModel.getAppStateProperty().removeObserver(this);
    }

    @Override
    synchronized public void update(ObservableProperty<AppState> sender, AppState update) {
        switch (update){
            case ready:
                break;
            case loading:
                break;
            case loaded:
                notify();
                break;
            case connectionError:
                notify();
                break;
            case error:
                notify();
                break;
        }
    }
}
