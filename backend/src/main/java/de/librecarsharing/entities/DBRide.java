package de.librecarsharing.entities;

import javax.persistence.*;


@Entity
public class DBRide {

    private String name;



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

    @ManyToOne //@JoinColumn(name="IDCar")
    private DBCar car;

    public DBCar getCar() {
        return car;
    }

    public void setCar(DBCar car) {
        this.car = car;
    }


    @Id
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
