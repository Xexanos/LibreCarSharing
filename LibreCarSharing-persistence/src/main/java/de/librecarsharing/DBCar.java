package de.librecarsharing;

import javax.persistence.*;
import java.util.*;

@Entity
public class DBCar extends DBIdentified{


    private String name;
    private Set<DBRide> rides;
    private DBCommunity community;
    private DBUser owner;
    private String location;
    private int seats;
    private int color;
    private String status;
    private String info;




    public DBCar() {
        this.rides = new HashSet<DBRide>();
    }
    @Basic
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    @Basic
    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Basic
    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }
    @Basic
    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Basic
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @OneToMany(mappedBy = "car")
    public Set<DBRide> getRides(){
        return rides;
    }
    public void setRides(Set<DBRide> rides) {
        this.rides = rides;
    }
    public void addRide(DBRide ride) {
        this.rides.add(ride);
        if (ride.getCar()!=this) {
            ride.setCar(this);
        }
    }
    @ManyToOne
    public DBUser getOwner() {
        return owner;
    }

    public void setOwner(DBUser owner) {
        this.owner = owner;
    }

    @ManyToOne
    public DBCommunity getCommunity() {
        return community;
    }

    public void setCommunity(DBCommunity community) {
        this.community = community;
    }

    @Basic
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String toString() {
        return this.getName();
    }




}
