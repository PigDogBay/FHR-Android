package com.pigdogbay.foodhygieneratings;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.pigdogbay.foodhygieneratings.model.Establishment;
import com.pigdogbay.foodhygieneratings.model.FoodHygieneAPI;
import com.pigdogbay.lib.utils.ActivityUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Created by Mark on 19/03/2017.
 */

public class FoodHygieneAPITest {

    JSONObject jsonTestData;
    @Before
    public void setup() throws IOException, JSONException {
        Context context = InstrumentationRegistry.getTargetContext();
        String data = ActivityUtils.readResource(context,R.raw.stoke);
        jsonTestData = new JSONObject(data);
    }

    @Test
    public void parseEstablishments1() throws Exception {
        List<Establishment> results = FoodHygieneAPI.parseEstablishments(jsonTestData);
        assertThat(results.size(),is(99));
    }
    @Test
    public void parseEstablishments2() throws Exception {
        List<Establishment> results = FoodHygieneAPI.parseEstablishments(jsonTestData);
        Establishment establishment = results.get(4);
        assertThat(establishment.getBusiness().getName(),is("Bake'n Butty"));
    }
    @Test
    public void parseEstablishments3() throws Exception {
        List<Establishment> results = FoodHygieneAPI.parseEstablishments(jsonTestData);
        Establishment establishment = results.get(4);
        assertThat(establishment.getAddress().getLine1(),is(""));
        assertThat(establishment.getAddress().getLine2(),is("54 Stone Road"));
        assertThat(establishment.getAddress().getLine3(),is("Stoke-on-Trent"));
        assertThat(establishment.getAddress().getLine4(),is(""));
        assertThat(establishment.getAddress().getPostcode(),is("ST4 6SP"));
    }
}
