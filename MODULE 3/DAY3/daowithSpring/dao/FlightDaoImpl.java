package dao;

import entity.Flight;

import java.util.*;
import java.util.stream.Collectors;

public class FlightDaoImpl implements FlightDao{

    Map<Integer,Flight> map=new LinkedHashMap<>();
    @Override
    public void save(Flight flight) {
         map.put(flight.getFlightNo(),flight);
    }

    @Override
    public Flight findById(int flightNo) {
        return map.get(flightNo);

    }

    @Override
    public void updateById(int flightNo, Flight flight) {
          map.put(flightNo,flight);
    }

    @Override
    public void deleteById(int flightNo) {
        map.remove(flightNo);
    }

    @Override
    public void deleteAll() {
         map.clear();
    }

    @Override
    public Collection<Flight> findAll() {
        return map.values();
    }

    @Override
    public Collection<Flight> findBySource(String src) {
        List<Flight> list=new ArrayList<>();
        return map.values()
                .stream()
                .filter(flight -> flight.getSrc().equalsIgnoreCase(src))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Flight> findByDestination(String dest) {
        List<Flight> list=new ArrayList<>();
        return map.values()
                .stream()
                .filter(flight -> flight.getDest().equalsIgnoreCase(dest))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Flight> findBySourceAndDestination(String src, String dest) {
        List<Flight> list=new ArrayList<>();
        return map.values()
                .stream()
                .filter(flight -> flight.getSrc().equalsIgnoreCase(src)
                && flight.getDest().equalsIgnoreCase(dest) )
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Flight> findByDepartureDate(Date dofDeparture) {
        List<Flight> list=new ArrayList<>();
        return map.values()
                .stream()
                .filter(flight -> flight.getDofDeparture().equals(dofDeparture))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Flight> sortByCostAsc() {
        List<Flight> list=new ArrayList<>();
        return map.values()
                .stream()
                .sorted(Comparator.comparing(Flight::getCostPerSeat))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Flight> sortByCostDesc() {
        List<Flight> list=new ArrayList<>();
        return map.values()
                .stream()
                .sorted(Comparator.comparing(Flight::getCostPerSeat).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Flight> sortByAvailableSeatsAsc() {
        List<Flight> list=new ArrayList<>();
        return map.values()
                .stream()
                .sorted(Comparator.comparing(Flight::getNumberOfSeats))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Flight> sortByAvailableSeatsDesc() {
        List<Flight> list=new ArrayList<>();
        return map.values()
                .stream()
                .sorted(Comparator.comparing(Flight::getNumberOfSeats).reversed())
                .collect(Collectors.toList());
    }
}
