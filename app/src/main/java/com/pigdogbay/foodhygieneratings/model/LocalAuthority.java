package com.pigdogbay.foodhygieneratings.model;

/**
 * Created by Mark on 18/03/2017.
 *
 */
public class LocalAuthority {

    private final String name, code;
    private String email, web;
    private int id, establishmentCount, schemeType;

    public LocalAuthority(String name, String code) {
        this.name = name;
        this.code = code;
        email="";
        web="";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public int getEstablishmentCount() {
        return establishmentCount;
    }

    public void setEstablishmentCount(int establishmentCount) {
        this.establishmentCount = establishmentCount;
    }

    public int getSchemeType() {
        return schemeType;
    }

    public void setSchemeType(int schemeType) {
        this.schemeType = schemeType;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return name;
    }

    private static LocalAuthority ALL;
    public static LocalAuthority getALL() {
        if (ALL==null){
            ALL = new LocalAuthority("All", "-1");
        }
        return ALL;
    }

}
