package com.pigdogbay.foodhygieneratings.model;

import com.pigdogbay.lib.utils.ObservableProperty;

import java.util.List;

/**
 * Created by Mark on 20/03/2017.
 *
 */
public interface IDataProvider {
    ObservableProperty<FetchState> getFetchStateProperty();
    List<Establishment> getResults();
    Coordinate getCoordinate();
    boolean findLocalEstablishments();
    boolean findEstablishments(Query query);
}
