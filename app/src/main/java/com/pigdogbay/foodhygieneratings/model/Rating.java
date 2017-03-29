package com.pigdogbay.foodhygieneratings.model;

import com.pigdogbay.foodhygieneratings.R;

import java.util.Date;

public class Rating {
    public static final String SCHEME_FHRS = "FHRS";
    public static final String SCHEME_FHIS = "FHIS";
    public static String[] scottishRatingNames = {
            "All", "Pass", "Improvement Required",  "Awaiting Publication", "Awaiting Inspection", "Exempt"};

    enum Scheme {
        FHRS, FHIS
    }

    private final Scores scores;
    private final Date awardedDate;
    private final boolean newRatingPending;
    private final String ratingsKey;
    private final RatingValue ratingValue;
    private final String name;
    private final Scheme scheme;


    public Rating(Scores scores, Date awardedDate, boolean newRatingPending, String ratingsKey, RatingValue ratingValue, String name, Scheme scheme) {
        this.scores = scores;
        this.awardedDate = awardedDate;
        this.newRatingPending = newRatingPending;
        this.ratingsKey = ratingsKey;
        this.ratingValue = ratingValue;
        this.name = name;
        this.scheme = scheme;
    }

    public Scheme getScheme() {
        return scheme;
    }
    public Scores getScores() {
        return scores;
    }
    public Date getAwardedDate() {
        return awardedDate;
    }
    public boolean isNewRatingPending() {
        return newRatingPending;
    }
    public String getRatingsKey() {
        return ratingsKey;
    }
    public RatingValue getRatingValue() {
        return ratingValue;
    }
    public String getName() {
        return name;
    }


    public boolean hasScores() {
        switch (ratingValue){
            case ratingOf0:
            case ratingOf1:
            case ratingOf2:
            case ratingOf3:
            case ratingOf4:
            case ratingOf5:
                return true;
            default:
                return false;
        }
    }

    public boolean hasRating() {
        switch (ratingValue){
            case exempt:
            case awaitingInspection:
            case awaitingPublication:
            case fhis_exempt:
            case fhis_awaitingInspection:
            case fhis_awaitingPublication:
            case other:
                return false;
            default:
                return true;
        }
    }

    public int getIconId(){
        switch (ratingValue){
            case ratingOf0:
                return R.drawable.ic_icon0;
            case ratingOf1:
                return R.drawable.ic_icon1;
            case ratingOf2:
                return R.drawable.ic_icon2;
            case ratingOf3:
                return R.drawable.ic_icon3;
            case ratingOf4:
                return R.drawable.ic_icon4;
            case ratingOf5:
                return R.drawable.ic_icon5;
            case exempt:
            case fhis_exempt:
                return R.drawable.ic_iconexempt;
            case fhis_pass:
                return R.drawable.ic_iconpass;
            case fhis_passEatSafe:
                return R.drawable.ic_iconpassplus;
            case awaitingInspection:
            case fhis_awaitingInspection:
            case fhis_awaitingPublication:
            case awaitingPublication:
                return R.drawable.ic_iconwaiting;
            case fhis_improvementRequired:
                return R.drawable.ic_iconimprovementrequired;
            default:
                return R.drawable.ic_iconexempt;
        }
    }

    public int getLogoId(){
        switch(ratingValue){

            case ratingOf0:
                return R.drawable.fhrs_0;
            case ratingOf1:
                return R.drawable.fhrs_1;
            case ratingOf2:
                return R.drawable.fhrs_2;
            case ratingOf3:
                return R.drawable.fhrs_3;
            case ratingOf4:
                return R.drawable.fhrs_4;
            case ratingOf5:
                return R.drawable.fhrs_5;
            case exempt:
                return R.drawable.fhrs_exempt;
            case fhis_exempt:
                return R.drawable.fhis_exempt;
            case fhis_pass:
                return R.drawable.fhis_pass;
            case fhis_passEatSafe:
                return R.drawable.fhis_pass_and_eat_safe;
            case awaitingInspection:
                return R.drawable.fhrs_awaitinginspection;
            case fhis_improvementRequired:
                return R.drawable.fhis_improvement_required;
            case awaitingPublication:
                return R.drawable.fhrs_awaitingpublication;
            case fhis_awaitingInspection:
                return R.drawable.fhis_awaiting_inspection;
            case fhis_awaitingPublication:
                return R.drawable.fhis_awaiting_publication;
            default:
                return R.drawable.fhrs_exempt;
        }
    }
}


