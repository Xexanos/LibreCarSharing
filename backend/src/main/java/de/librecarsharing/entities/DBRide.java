package de.librecarsharing.entities;

import javax.persistence.*;


@Entity
public class DBRide extends DBIdentified{

    private String name;
    private DBCar car;


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

    @ManyToOne
    public DBCar getCar() {
        return car;
    }
    public void setCar(DBCar car) {
        this.car = car;
    }

}
