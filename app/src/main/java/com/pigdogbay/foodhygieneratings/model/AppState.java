package com.pigdogbay.foodhygieneratings.model;

/**
 * Created by Mark on 20/03/2017.
 *
 */
public enum AppState
{
    ready,
    requestingLocationAuthorization,
    locating,
    foundLocation,
    notAuthorizedForLocating,
    errorLocating,
    loading,
    loaded,
    error
}
