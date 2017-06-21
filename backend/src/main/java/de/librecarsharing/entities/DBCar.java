package de.librecarsharing.entities;

import javax.persistence.*;
import java.util.*;

import static javax.persistence.CascadeType.ALL;

@Entity
public class DBCar {


    private String name;
    @Id
    private long IDCar;


    public DBCar() {
        this.rides = new ArrayList<>();
    }

    public long getIDCar() {
        return IDCar;
    }

    public void setIDCar(long IDCar) {
        this.IDCar = IDCar;
    }


    @OneToMany(targetEntity=DBRide.class, mappedBy="car")
    private List<DBRide> rides;
    public List<DBRide> getRides(){
        return rides;
    }
    public void setRides(List<DBRide> rides) {
        this.rides = rides;
    }
    public void addRide(DBRide ride) {
        this.rides.add(ride);
        if (ride.getCar()!=this) {
            ride.setCar(this);
        }
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
