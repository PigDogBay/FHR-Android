package com.pigdogbay.foodhygieneratings.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark on 18/03/2017.
 */

public class Business {
    private final String name, type;
    private final int typeId, fhrsId;
    private final int sortOrder;

    public Business(String name, String type, int typeId, int fhrsId) {
        this.name = name;
        this.type = type;
        this.typeId = typeId;
        this.fhrsId = fhrsId;
        this.sortOrder = 0;
    }

    private Business(String type, int typeId, int sortOrder) {
        this.type = type;
        this.typeId = typeId;
        this.sortOrder = sortOrder;
        this.name = "";
        this.fhrsId = 0;
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

    private static List<Business> businessTypes;

    public static final String[] businessNames = {
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

    public static List<Business> getBusinessCategories() {
        if (businessTypes==null){
            businessTypes = new ArrayList<>();
            for (int i=0; i<businessNames.length;i++){
                businessTypes.add(new Business(businessNames[i], Business.businessTypesIds[i], i));
            }
        }
        return businessTypes;
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
