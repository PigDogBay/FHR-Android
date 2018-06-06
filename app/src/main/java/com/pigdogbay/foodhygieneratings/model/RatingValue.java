package com.pigdogbay.foodhygieneratings.model;

/**
 * Created by Mark on 18/03/2017.
 */

public enum RatingValue {
    ratingOf0, ratingOf1, ratingOf2, ratingOf3, ratingOf4, ratingOf5,
    exempt,
    awaitingInspection,
    awaitingPublication,
    fhis_exempt,
    fhis_pass,
    fhis_passEatSafe,
    fhis_improvementRequired,
    fhis_awaitingInspection,
    fhis_awaitingPublication,
    other;

    public boolean isRated(){
        switch (this){
            case ratingOf0:
            case ratingOf1:
            case ratingOf2:
            case ratingOf3:
            case ratingOf4:
            case ratingOf5:
            case fhis_pass:
            case fhis_passEatSafe:
            case fhis_improvementRequired:
                return true;
            default:
                return false;
        }
    }
}
