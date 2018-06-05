package com.pigdogbay.foodhygieneratings.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark on 18/03/2017.
 */

public class Business {
    private final String name, type;
    private final int typeId, fhrsId;

    public Business(String name, String type, int typeId, int fhrsId) {
        this.name = name;
        this.type = type;
        this.typeId = typeId;
        this.fhrsId = fhrsId;
    }

    private static final int all = -1;
    private static final int takeawaySandwichShop = 7844;
    private static final int restaurantsCafeCanteen = 1;
    private static final int pubsBarsNightclubs = 7843;
    private static final int mobileCaters = 7846;
    private static final int otherCaters = 7841;
    private static final int hotel = 7842;
    private static final int supermarkets = 7840;
    private static final int retailersOther = 4613;
    private static final int hospitals = 5;
    private static final int school = 7845;
    private static final int importersExporters = 14;
    private static final int farmersGrowers = 7838;
    private static final int distributorsTransporters = 7;
    private static final int manufacturers = 7839;

    private static final String[] businessNames = {
            "All",
            "Takeaway/sandwich shop",
            "Restaurant/Cafe/Canteen",
            "Pub/bar/nightclub",
            "Mobile caterer",
            "Other catering premises",
            "Hotel/bed & breakfast/guest house",
            "Retailers - supermarkets/hypermarkets",
            "Retailers - other",
            "Hospitals/Childcare/Caring Premises",
            "School/college/university",
            "Importers/Exporters",
            "Farmers/growers",
            "Distributors/Transporters",
            "Manufacturers/packers"
        };

    public static final int[] businessTypesIds = {
            Business.all,
            Business.takeawaySandwichShop,
            Business.restaurantsCafeCanteen,
            Business.pubsBarsNightclubs,
            Business.mobileCaters,
            Business.otherCaters,
            Business.hotel,
            Business.supermarkets,
            Business.retailersOther,
            Business.hospitals,
            Business.school,
            Business.importersExporters,
            Business.farmersGrowers,
            Business.distributorsTransporters,
            Business.manufacturers
    };

    public static String getBusinessType(int typeId){
        switch (typeId) {
            case Business.all:
                return businessNames[0];
            case Business.takeawaySandwichShop:
                return businessNames[1];
            case Business.restaurantsCafeCanteen:
                return businessNames[2];
            case Business.pubsBarsNightclubs:
                return businessNames[3];
            case Business.mobileCaters:
                return businessNames[4];
            case Business.otherCaters:
                return businessNames[5];
            case Business.hotel:
                return businessNames[6];
            case Business.supermarkets:
                return businessNames[7];
            case Business.retailersOther:
                return businessNames[8];
            case Business.hospitals:
                return businessNames[9];
            case Business.school:
                return businessNames[10];
            case Business.importersExporters:
                return businessNames[11];
            case Business.farmersGrowers:
                return businessNames[12];
            case Business.distributorsTransporters:
                return businessNames[13];
            case Business.manufacturers:
                return businessNames[14];
            default:
                return "Unknown";
        }
    }
    public static int getBusinessSortOrder(int typeId){
        switch (typeId) {
            case Business.all:
                return 0;
            case Business.takeawaySandwichShop:
                return 1;
            case Business.restaurantsCafeCanteen:
                return 2;
            case Business.pubsBarsNightclubs:
                return 3;
            case Business.mobileCaters:
                return 4;
            case Business.otherCaters:
                return 5;
            case Business.hotel:
                return 6;
            case Business.supermarkets:
                return 7;
            case Business.retailersOther:
                return 8;
            case Business.hospitals:
                return 9;
            case Business.school:
                return 10;
            case Business.importersExporters:
                return 11;
            case Business.farmersGrowers:
                return 12;
            case Business.distributorsTransporters:
                return 13;
            case Business.manufacturers:
                return 14;
            default:
                return 15;
        }
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getTypeId() {
        return typeId;
    }

    public int getFhrsId() {
        return fhrsId;
    }
}
