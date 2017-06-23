package de.librecarsharing.entities;

import javax.persistence.*;
import java.util.*;

import static javax.persistence.CascadeType.ALL;

@Entity
public class DBCommunity extends DBIdentified{


    private String name;
    private Set<DBCar> cars;
    private Set<DBUser> users;
    private DBUser owner;


    public DBCommunity() {
        this.cars = new HashSet<DBCar>();
        this.users= new HashSet<DBUser>();
    }


    @OneToMany
    public Set<DBUser> getUsers() {
        return users;
    }

    public void setUsers(Set<DBUser> users) {
        this.users = users;
    }

    @OneToMany(mappedBy="community")
    public Set<DBCar> getCars() {
        return cars;
    }

    public void setCars(Set<DBCar> cars) {
        this.cars = cars;
    }

    public void addCar(DBCar car) {
        this.cars.add(car);
        //if (cars.getCar()!=this) {
         //   ride.setCar(this);
        //}
    }

    @OneToOne
    public DBUser getOwner() {
        return owner;
    }

    public void setOwner(DBUser owner) {
        this.owner = owner;
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
