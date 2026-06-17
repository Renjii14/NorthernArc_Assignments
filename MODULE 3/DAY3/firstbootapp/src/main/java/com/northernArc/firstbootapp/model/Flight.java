package com.northernArc.firstbootapp.model;


import java.sql.Time;
import java.util.Date;

public class Flight {

    private int flightNo;
    private Date dofDeparture;
    private Date dofArrival;
    private String src;
    private String dest;
    private Time tofDeparture;
    private Time tofArrival;
    private double costPerSeat;
    private int numberOfSeats;

    public Flight() {
    }

    public Flight(int flightNo,
                  Date dofDeparture,
                  Date dofArrival,
                  String src,
                  String dest,
                  Time tofDeparture,
                  Time tofArrival,
                  double costPerSeat,
                  int numberOfSeats) {

        this.flightNo = flightNo;
        this.dofDeparture = dofDeparture;
        this.dofArrival = dofArrival;
        this.src = src;
        this.dest = dest;
        this.tofDeparture = tofDeparture;
        this.tofArrival = tofArrival;
        this.costPerSeat = costPerSeat;
        this.numberOfSeats = numberOfSeats;
    }

    public int getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(int flightNo) {
        this.flightNo = flightNo;
    }

    public Date getDofDeparture() {
        return dofDeparture;
    }

    public void setDofDeparture(Date dofDeparture) {
        this.dofDeparture = dofDeparture;
    }

    public Date getDofArrival() {
        return dofArrival;
    }

    public void setDofArrival(Date dofArrival) {
        this.dofArrival = dofArrival;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public Time getTofDeparture() {
        return tofDeparture;
    }

    public void setTofDeparture(Time tofDeparture) {
        this.tofDeparture = tofDeparture;
    }

    public Time getTofArrival() {
        return tofArrival;
    }

    public void setTofArrival(Time tofArrival) {
        this.tofArrival = tofArrival;
    }

    public double getCostPerSeat() {
        return costPerSeat;
    }

    public void setCostPerSeat(double costPerSeat) {
        this.costPerSeat = costPerSeat;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "flightNo=" + flightNo +
                ", dofDeparture=" + dofDeparture +
                ", dofArrival=" + dofArrival +
                ", src='" + src + '\'' +
                ", dest='" + dest + '\'' +
                ", tofDeparture=" + tofDeparture +
                ", tofArrival=" + tofArrival +
                ", costPerSeat=" + costPerSeat +
                ", numberOfSeats=" + numberOfSeats +
                '}';
    }
}