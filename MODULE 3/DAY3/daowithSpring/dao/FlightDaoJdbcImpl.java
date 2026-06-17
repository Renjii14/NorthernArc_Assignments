package dao;

import Connection.DBmanager;
import entity.Flight;

import java.sql.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class FlightDaoJdbcImpl implements FlightDao {

    private final DBmanager dBmanager;

    public FlightDaoJdbcImpl(DBmanager dBmanager) {
        this.dBmanager = dBmanager;
    }

    public Flight mapToFlight(ResultSet rs) throws SQLException {

        return new Flight(rs.getInt(1), rs.getDate(2), rs.getDate(3), rs.getString(4), rs.getString(5), rs.getTime(6), rs.getTime(7), rs.getDouble(8), rs.getInt(9));
    }

    @Override
    public void save(Flight flight) {

        try {
            Connection con = dBmanager.getConnection();
            String sql = "insert into flight values(?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, flight.getFlightNo());
            ps.setDate(2, (java.sql.Date) flight.getDofDeparture());
            ps.setDate(3, (java.sql.Date) flight.getDofArrival());
            ps.setString(4, flight.getSrc());
            ps.setString(5, flight.getDest());
            ps.setTime(6, flight.getTofDeparture());
            ps.setTime(7, flight.getTofArrival());
            ps.setDouble(8, flight.getCostPerSeat());
            ps.setInt(9, flight.getNumberOfSeats());
            int row = ps.executeUpdate();
            con.close();
            System.out.println("Rows Added : " + row);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Flight findById(int flightNo) {

        try {
            Connection con = dBmanager.getConnection();
            String sql = "select * from flight where flight_no=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, flightNo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return mapToFlight(rs);
            }
            con.close();
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<Flight> findAll() {

        try {
            List<Flight> flights = new LinkedList<>();
            Connection con = dBmanager.getConnection();
            String sql = "select * from flight";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                flights.add(mapToFlight(rs));
            }
            con.close();
            return flights;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateById(int flightNo, Flight flight) {

        try {
            Connection con = dBmanager.getConnection();
            String sql = "update flight set dof_departure=?,dof_arrival=?,src=?,dest=?,tof_departure=?,tof_arrival=?,cost_per_seat=?,number_of_seats=? where flight_no=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setDate(1, (java.sql.Date) flight.getDofDeparture());
            ps.setDate(2, (java.sql.Date) flight.getDofArrival());
            ps.setString(3, flight.getSrc());
            ps.setString(4, flight.getDest());
            ps.setTime(5, flight.getTofDeparture());
            ps.setTime(6, flight.getTofArrival());
            ps.setDouble(7, flight.getCostPerSeat());
            ps.setInt(8, flight.getNumberOfSeats());
            ps.setInt(9, flightNo);
            int row = ps.executeUpdate();
            con.close();
            System.out.println("Rows Updated : " + row);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(int flightNo) {

        try {
            Connection con = dBmanager.getConnection();
            String sql = "delete from flight where flight_no=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, flightNo);
            int row = ps.executeUpdate();
            con.close();
            System.out.println("Rows Deleted : " + row);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAll() {

        try {
            Connection con = dBmanager.getConnection();
            String sql = "delete from flight";
            PreparedStatement ps = con.prepareStatement(sql);
            int row = ps.executeUpdate();
            con.close();
            System.out.println("Rows Deleted : " + row);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<Flight> findBySource(String src) {

        try {
            List<Flight> flights = new LinkedList<>();
            Connection con = dBmanager.getConnection();
            String sql = "select * from flight where src=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, src);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                flights.add(mapToFlight(rs));
            }
            con.close();
            return flights;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<Flight> findByDestination(String dest) {

        try {
            List<Flight> flights = new LinkedList<>();
            Connection con = dBmanager.getConnection();
            String sql = "select * from flight where dest=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, dest);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                flights.add(mapToFlight(rs));
            }
            con.close();
            return flights;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<Flight> findBySourceAndDestination(String src,
                                                         String dest) {

        try {
            List<Flight> flights = new LinkedList<>();
            Connection con = dBmanager.getConnection();
            String sql = "select * from flight where src=? and dest=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, src);
            ps.setString(2, dest);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                flights.add(mapToFlight(rs));
            }
            con.close();
            return flights;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<Flight> findByDepartureDate(java.util.Date date) {

        try {
            List<Flight> flights = new LinkedList<>();
            Connection con = dBmanager.getConnection();
            String sql = "select * from flight where dof_departure=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(date.getTime()));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                flights.add(mapToFlight(rs));
            }
            con.close();
            return flights;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<Flight> sortByCostAsc() {

        try {
            List<Flight> flights = new LinkedList<>();
            Connection con = dBmanager.getConnection();
            String sql = "select * from flight order by cost_per_seat asc";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                flights.add(mapToFlight(rs));
            }
            con.close();
            return flights;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<Flight> sortByCostDesc() {

        try {
            List<Flight> flights = new LinkedList<>();
            Connection con = dBmanager.getConnection();
            String sql = "select * from flight order by cost_per_seat desc";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                flights.add(mapToFlight(rs));
            }
            con.close();
            return flights;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<Flight> sortByAvailableSeatsAsc() {

        try {
            List<Flight> flights = new LinkedList<>();
            Connection con = dBmanager.getConnection();
            String sql = "select * from flight order by number_of_seats asc";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                flights.add(mapToFlight(rs));
            }
            con.close();
            return flights;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<Flight> sortByAvailableSeatsDesc() {

        try {
            List<Flight> flights = new LinkedList<>();
            Connection con = dBmanager.getConnection();
            String sql = "select * from flight order by number_of_seats desc";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                flights.add(mapToFlight(rs));
            }
            con.close();
            return flights;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}