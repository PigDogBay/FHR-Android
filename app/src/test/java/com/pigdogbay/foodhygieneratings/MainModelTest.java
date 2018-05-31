package com.pigdogbay.foodhygieneratings;
import com.pigdogbay.foodhygieneratings.model.AppState;
import com.pigdogbay.foodhygieneratings.model.DummyDataProvider;
import com.pigdogbay.foodhygieneratings.model.Establishment;
import com.pigdogbay.foodhygieneratings.model.MainModel;
import com.pigdogbay.foodhygieneratings.model.Query;
import com.pigdogbay.lib.utils.ObservableProperty;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.*;

public class MainModelTest implements ObservableProperty.PropertyChangedObserver<AppState> {

    private static final String STOKE_RESOURCE = "stoke.json";

    MainModel.IDataProvider getDataProvider(String resourePath) throws IOException {
        String path = getClass().getClassLoader().getResource(resourePath).getPath();
        String json = new String(Files.readAllBytes(Paths.get(path)));
        return new DummyDataProvider(json);
    }

    MainModel createMainModel() throws IOException {
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
