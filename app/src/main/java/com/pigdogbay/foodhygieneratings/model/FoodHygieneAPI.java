package com.pigdogbay.foodhygieneratings.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Mark on 19/03/2017.
 */
public class FoodHygieneAPI {

    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    public static List<LocalAuthority> parseAuthorities(JSONObject jsonObject) throws JSONException {
        ArrayList<LocalAuthority> authorities = new ArrayList<>();
        JSONArray authoritiesJsonArray = jsonObject.getJSONArray("authorities");
        for (int i = 0; i < authoritiesJsonArray.length(); i++) {
            JSONObject authorityObject = authoritiesJsonArray.getJSONObject(i);
            authorities.add(parseLocalAuthorityFromList(authorityObject));
        }
        return authorities;
    }

    private static LocalAuthority parseLocalAuthorityFromList(JSONObject authorityObject) throws JSONException {
        String name = authorityObject.getString("Name");
        int count = authorityObject.getInt("EstablishmentCount");
        int schemeType = authorityObject.getInt("SchemeType");
        int id = authorityObject.getInt(("LocalAuthorityId"));
        String code = authorityObject.getString("LocalAuthorityIdCode");
        LocalAuthority la = new LocalAuthority(name, code);
        la.setEstablishmentCount(count);
        la.setSchemeType(schemeType);
        la.setId(id);
        return la;
    }


    public static List<Establishment> parseEstablishments(JSONObject jsonObject) throws JSONException, ParseException {
        ArrayList<Establishment> results = new ArrayList<>();
        JSONArray estamblishmentsJsonArray = jsonObject.getJSONArray("establishments");
        for (int i = 0; i < estamblishmentsJsonArray.length(); i++) {
            JSONObject estObject = estamblishmentsJsonArray.getJSONObject(i);
            results.add(parseEstablishment(estObject));
        }
        return results;
    }

    private static Establishment parseEstablishment(JSONObject establishmentObject) throws JSONException, ParseException {
        Business business = parseBusiness(establishmentObject);
        Rating rating = parseRating(establishmentObject);
        LocalAuthority localAuthority = parseLocalAuthority(establishmentObject);
        Address address = parseAddress(establishmentObject);
        Coordinate coordinate = parseCoordinate(establishmentObject);
        double distance = parseDistance(establishmentObject);
        return new Establishment(business, rating, address, localAuthority, distance, coordinate);
    }

    private static Business parseBusiness(JSONObject estObject) throws JSONException {
        String name = estObject.getString("BusinessName");
        String businessType = estObject.getString("BusinessType");
        int businessTypeId = estObject.getInt("BusinessTypeID");
        int fhrsId = estObject.getInt("FHRSID");
        return new Business(name, businessType, businessTypeId, fhrsId);
    }

    private static Rating parseRating(JSONObject estObject) throws JSONException, ParseException {
        String name = estObject.getString("RatingValue");
        String schemeTypeString = estObject.getString("SchemeType");
        Rating.Scheme scheme = Rating.SCHEME_FHIS.equals(schemeTypeString) ? Rating.Scheme.FHIS : Rating.Scheme.FHRS;
        RatingValue ratingValue = parseRatingValue(name, scheme);
        String ratingsKey = estObject.getString("RatingKey");
        boolean isPending = estObject.getBoolean("NewRatingPending");
        String dateString = estObject.getString("RatingDate");
        Date date = dateFormat.parse(dateString);
        Scores scores = parseScores(estObject);
        return new Rating(scores, date, isPending, ratingsKey, ratingValue, name, scheme);
    }

    private static Scores parseScores(JSONObject estObject) {
        try {
            JSONObject scores = estObject.getJSONObject("scores");
            int hygiene = scores.getInt("Hygiene");
            int structural = scores.getInt("Structural");
            int management = scores.getInt("ConfidenceInManagement");
            return new Scores(hygiene, structural, management);
        } catch (JSONException e) {
            return Scores.getNullScores();
        }
    }

    private static Address parseAddress(JSONObject estObject) throws JSONException {
        String line1 = estObject.getString("AddressLine1");
        String line2 = estObject.getString("AddressLine2");
        String line3 = estObject.getString("AddressLine3");
        String line4 = estObject.getString("AddressLine4");
        String postcode = estObject.getString("PostCode");
        return new Address(line1, line2, line3, line4, postcode);
    }

    private static LocalAuthority parseLocalAuthority(JSONObject estObject) throws JSONException {
        String name = estObject.getString("LocalAuthorityName");
        String code = estObject.getString("LocalAuthorityCode");
        String web = estObject.getString("LocalAuthorityWebSite");
        String email = estObject.getString("LocalAuthorityEmailAddress");
        LocalAuthority la = new LocalAuthority(name, code);
        la.setWeb(web);
        la.setEmail(email);
        return la;
    }

    private static Coordinate parseCoordinate(JSONObject estObject) throws JSONException {
        JSONObject geocode = estObject.getJSONObject("geocode");
        String longitudeStr = geocode.getString("longitude");
        String latitudeStr = geocode.getString("latitude");
        double longitude = 0;
        double latitude = 0;
        try {
            longitude = Double.parseDouble(longitudeStr);
            latitude = Double.parseDouble(latitudeStr);
        } catch (Exception e){}
        return new Coordinate(longitude, latitude);
    }

    private static double parseDistance(JSONObject estObject) {
        try {
            return estObject.getDouble("Distance");
        } catch (JSONException e) {
            return 0.0;
        }
    }

    private static RatingValue parseRatingValue(String name, Rating.Scheme scheme) {
        switch (scheme) {
            case FHRS:
                switch (name) {
                    case "0":
                        return RatingValue.ratingOf0;
                    case "1":
                        return RatingValue.ratingOf1;
                    case "2":
                        return RatingValue.ratingOf2;
                    case "3":
                        return RatingValue.ratingOf3;
                    case "4":
                        return RatingValue.ratingOf4;
                    case "5":
                        return RatingValue.ratingOf5;
                    case "Exempt":
                        return RatingValue.exempt;
                    case "AwaitingInspection":
                        return RatingValue.awaitingInspection;
                    case "Awaiting Inspection":
                        return RatingValue.awaitingInspection;
                    case "AwaitingPublication":
                        return RatingValue.awaitingPublication;
                    case "Awaiting Publication":
                        return RatingValue.awaitingPublication;
                }
            case FHIS:
                switch (name) {
                    case "Exempt":
                        return RatingValue.fhis_exempt;
                    case "AwaitingInspection":
                        return RatingValue.fhis_awaitingInspection;
                    case "Awaiting Inspection":
                        return RatingValue.fhis_awaitingInspection;
                    case "Pass":
                        return RatingValue.fhis_pass;
                    case "Pass and Eat Safe":
                        return RatingValue.fhis_passEatSafe;
                    case "Improvement Required":
                        return RatingValue.fhis_improvementRequired;
                    case "ImprovementRequired":
                        return RatingValue.fhis_improvementRequired;
                    case "AwaitingPublication":
                        return RatingValue.fhis_awaitingPublication;
                    case "Awaiting Publication":
                        return RatingValue.fhis_awaitingPublication;
                    default:
                        return RatingValue.fhis_exempt;
                }
        }
        return RatingValue.other;
    }


}
