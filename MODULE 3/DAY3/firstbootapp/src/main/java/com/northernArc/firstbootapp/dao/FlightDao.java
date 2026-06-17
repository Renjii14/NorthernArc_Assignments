package com.northernArc.firstbootapp.dao;

import com.northernArc.firstbootapp.model.Flight;

import java.util.Collection;
import java.util.Date;

public interface FlightDao {

    void save(Flight flight);

    Flight findById(int flightNo);

    void updateById(int flightNo, Flight flight);

    void deleteById(int flightNo);

    void deleteAll();

    Collection<Flight> findAll();

    Collection<Flight> findBySource(String src);

    Collection<Flight> findByDestination(String dest);

    Collection<Flight> findBySourceAndDestination(String src, String dest);

    Collection<Flight> findByDepartureDate(Date dofDeparture);

    Collection<Flight> sortByCostAsc();

    Collection<Flight> sortByCostDesc();

    Collection<Flight> sortByAvailableSeatsAsc();

    Collection<Flight> sortByAvailableSeatsDesc();
}