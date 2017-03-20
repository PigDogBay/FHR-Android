package com.pigdogbay.foodhygieneratings;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.pigdogbay.foodhygieneratings.model.DummyDataProvider;
import com.pigdogbay.foodhygieneratings.model.FetchState;
import com.pigdogbay.foodhygieneratings.model.IDataProvider;
import com.pigdogbay.lib.utils.ActivityUtils;
import com.pigdogbay.lib.utils.ObservableProperty;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


/**
 * Created by Mark on 20/03/2017.
 *
 */
public class DummyDataProviderTest implements ObservableProperty.PropertyChangedObserver<FetchState> {

    private IDataProvider target;
    private FetchState state = FetchState.ready;

    @Before
    public void setUp() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        String data = ActivityUtils.readResource(context,R.raw.stoke);
        target = new DummyDataProvider(data);
        target.getFetchStateProperty().addObserver(this);
    }

    @After
    public void tearDown() throws Exception {
        target.getFetchStateProperty().removeObserver(this);
    }

    @Test
    public void test1() throws Exception {
        target.findLocalEstablishments();
        while (state!=FetchState.loaded){
            Thread.sleep(100);
        }
        assertThat(target.getResults().size(),is(99));
    }

    @Override
    public void update(ObservableProperty<FetchState> sender, FetchState update) {
        this.state = update;
    }
}
