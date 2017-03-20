package com.pigdogbay.foodhygieneratings.model;

/**
 * Created by Mark on 18/03/2017.
 */
public class Scores {

    private final int hygiene, structural, management;

    public static final String hygienicTitle = "Hygienic food handling";
    public static final String hygienicDescription = "Hygienic handling of food including preparation, cooking, re-heating, cooling and storage";

    public static final String  structuralTitle = "Cleanliness and condition of facilities and building";
    public static final String  structuralDescription = "Cleanliness and condition of facilities and building (including having appropriate layout, ventilation, hand washing facilities and pest control) to enable good food hygiene";

    public static final String  managementTitle = "Management of food safety";
    public static final String  managementDescription = "System or checks in place to ensure that food sold or served is safe to eat, evidence that staff know about food safety, and the food safety officer has confidence that standards will be maintained in the future.";

    private static Scores nullScores = new Scores(0,0,0);

    public static Scores getNullScores() {
        return nullScores;
    }

    public int getHygiene() {
        return hygiene;
    }

    public int getStructural() {
        return structural;
    }

    public int getManagement() {
        return management;
    }

    public Scores(int hygiene, int structural, int management) {
        this.hygiene = hygiene;
        this.structural = structural;
        this.management = management;
    }

    public String getHygieneDescription() {
        return getDescription(hygiene);
    }
    public String getStructuralDescription() {
        return getDescription(structural);
    }
    public String getManagementDescription() {
        return getDescription(management);
    }

    //Taken from fhrsguidance.pdf
    private String getDescription(int score) {
        switch (score) {
            case 0:
                return "very good";
            case 5:
                return "good";
            case 10:
                return "generally satisfactory";
            case 15:
                return "improvement necessary";
            case 20:
                return "major improvement necessary";
            case 25:
                return "urgent improvement necessary";
            case 30:
                return "urgent improvement necessary";
            default:
                return "";

        }
    }

}
