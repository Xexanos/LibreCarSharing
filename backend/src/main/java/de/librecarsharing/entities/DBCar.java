package de.librecarsharing.entities;

import javax.persistence.*;
import java.util.*;

import static javax.persistence.CascadeType.ALL;

@Entity
public class DBCar extends DBIdentified{


    private String name;
    private Set<DBRide> rides;
    private DBCommunity community;
    private DBUser owner;
    private String location;



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



    @OneToMany
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
    @OneToOne
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
