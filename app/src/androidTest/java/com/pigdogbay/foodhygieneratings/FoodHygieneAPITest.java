package com.pigdogbay.foodhygieneratings;

import android.content.Context;
import androidx.test.InstrumentationRegistry;

import com.pigdogbay.foodhygieneratings.model.Establishment;
import com.pigdogbay.foodhygieneratings.model.FoodHygieneAPI;
import com.pigdogbay.foodhygieneratings.model.LocalAuthority;
import com.pigdogbay.foodhygieneratings.model.RatingValue;
import com.pigdogbay.foodhygieneratings.model.Scores;
import com.pigdogbay.lib.utils.ActivityUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Mark on 19/03/2017.
 *
 */
public class FoodHygieneAPITest {

    private JSONObject jsonTestData;
    private JSONObject jsonAuthoritiesData;
    @Before
    public void setup() throws IOException, JSONException {
        Context context = InstrumentationRegistry.getTargetContext();
        String data = ActivityUtils.readResource(context,R.raw.stoke);
        jsonTestData = new JSONObject(data);

        String localAuthoriesData = ActivityUtils.readResource(context, R.raw.authorities);
        jsonAuthoritiesData = new JSONObject(localAuthoriesData);
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
    @Test
    public void parseEstablishments4() throws Exception {
        List<Establishment> results = FoodHygieneAPI.parseEstablishments(jsonTestData);
        Establishment establishment = results.get(4);
        assertThat(establishment.getLocalAuthority().getName() ,is("Stoke-On-Trent"));
        assertThat(establishment.getLocalAuthority().getCode() ,is("880"));
        assertThat(establishment.getLocalAuthority().getEmail() ,is("PublicProtection@stoke.gov.uk"));
        assertThat(establishment.getLocalAuthority().getWeb() ,is("http://www.stoke.gov.uk"));
    }
    @Test
    public void parseEstablishments5() throws Exception {
        List<Establishment> results = FoodHygieneAPI.parseEstablishments(jsonTestData);
        Establishment establishment = results.get(4);
        assertThat(establishment.getDistance() ,is(0.15293596126829617));
        assertThat(establishment.getCoordinate().getLongitude() ,is(-2.20093));
        assertThat(establishment.getCoordinate().getLatitude() ,is(52.982992));
    }
    @Test
    public void parseEstablishments6() throws Exception {
        List<Establishment> results = FoodHygieneAPI.parseEstablishments(jsonTestData);
        Establishment establishment = results.get(4);
        assertThat(establishment.getRating().getName() ,is("5"));
        assertThat(establishment.getRating().getRatingsKey() ,is("fhrs_5_en-gb"));
        assertThat(establishment.getRating().getAwardedDate() ,is(new Date(2016-1900, Calendar.NOVEMBER,30)));
        assertThat(establishment.getRating().isNewRatingPending() ,is(true));
        assertThat(establishment.getRating().getRatingValue() ,is(RatingValue.ratingOf5));

        Scores scores = establishment.getRating().getScores();
        assertThat(scores.getHygiene(),is(5));
        assertThat(scores.getStructural(),is(10));
        assertThat(scores.getManagement(),is(15));
    }


    @Test
    public void parseLocalAuthorities1() throws Exception {
        List<LocalAuthority> authorities = FoodHygieneAPI.parseAuthorities(jsonAuthoritiesData);
        assertThat(authorities.size(), is(384));
    }
    /*
        "LocalAuthorityId":334,
        "LocalAuthorityIdCode":"551",
        "Name":"Anglesey",
        "EstablishmentCount":690,
        "SchemeType":1,
     */
    @Test
    public void parseLocalAuthorities2() throws Exception {
        List<LocalAuthority> authorities = FoodHygieneAPI.parseAuthorities(jsonAuthoritiesData);
        LocalAuthority la = authorities.get(5);
        assertThat(la.getName(), is("Anglesey"));
        assertThat(la.getCode(),is("551"));
        assertThat(la.getId(), is(334));
        assertThat(la.getEstablishmentCount(),is(708));
        assertThat(la.getSchemeType(),is(1));
    }

}
