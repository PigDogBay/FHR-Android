package com.pigdogbay.foodhygieneratings.model;

import com.pigdogbay.foodhygieneratings.R;

import java.util.Date;

public class Rating {
    public static String[] scottishRatingNames = {
            "All", "Pass", "Improvement Required",  "Awaiting Publication", "Awaiting Inspection", "Exempt"};

    private final Scores scores;
    private final Date awardedDate;
    private final boolean newRatingPending;
    private final String ratingsKey;
    private final RatingValue ratingValue;
    private final String name;

    public Rating(Scores scores, Date awardedDate, boolean newRatingPending, String ratingsKey, RatingValue ratingValue, String name) {
        this.scores = scores;
        this.awardedDate = awardedDate;
        this.newRatingPending = newRatingPending;
        this.ratingsKey = ratingsKey;
        this.ratingValue = ratingValue;
        this.name = name;
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
                return R.drawable.ic_iconexempt;
            case pass:
                return R.drawable.ic_iconpass;
            case passEatSafe:
                return R.drawable.ic_iconpassplus;
            case awaitingInspection:
                return R.drawable.ic_iconwaiting;
            case improvementRequired:
                return R.drawable.ic_iconimprovementrequired;
            case awaitingPublication:
                return R.drawable.ic_iconwaiting;
            case other:
                return R.drawable.ic_iconexempt;
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
            case pass:
                return R.drawable.fhis_pass;
            case passEatSafe:
                return R.drawable.fhis_pass_and_eat_safe;
            case awaitingInspection:
                return R.drawable.fhrs_awaitinginspection;
            case improvementRequired:
                return R.drawable.fhis_improvement_required;
            case awaitingPublication:
                return R.drawable.fhrs_awaitingpublication;
            default:
                return R.drawable.fhrs_exempt;
        }
    }

    public String getRatingDescription() {
        switch (ratingValue){
            case ratingOf0:
                return "0 - Urgent Improvement Necessary";
            case ratingOf1:
                return "1 - Major Improvement Necessary";
            case ratingOf2:
                return "2 - Improvement Necessary";
            case ratingOf3:
                return "3 - Generally Satisfactory";
            case ratingOf4:
                return "4 - Good";
            case ratingOf5:
                return "5 - Very Good";
            case exempt:
                return "Exempt";
            case pass:
                return "Pass";
            case passEatSafe:
                return "Pass and Eat Safe";
            case awaitingInspection:
                return "Awaiting Inspection";
            case improvementRequired:
                return "Improvement Required";
            case awaitingPublication:
                return "Awaiting Publication";
            default:
                return "";
        }
    }
}


