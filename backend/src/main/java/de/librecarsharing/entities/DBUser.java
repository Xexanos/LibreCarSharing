package de.librecarsharing.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
public class DBUser extends DBIdentified{

    private String name;
    private Set<DBCar> cars;
    private Set<DBCommunity> communities;


    public DBUser() {
        communities= new HashSet<DBCommunity>();
        cars= new HashSet<DBCar>();
    }

    @Basic
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @ManyToMany
    public Set<DBCommunity> getCommunities() {
        return communities;
    }

    public void setCommunities(Set<DBCommunity> communities) {
        this.communities = communities;
    }

    public void addCommunity(DBCommunity community) {
        this.communities.add(community);
        if (!community.getUsers().contains(this)) {
            community.addUser(this);
        }
    }
    @OneToMany(mappedBy = "owner")
    public Set<DBCar> getCars() {
        return cars;
    }

    public void setCars(Set<DBCar> cars) {

        this.cars = cars;

    }
    public void addCar(DBCar car)
    {
        this.cars.add(car);
        if(car.getOwner()!=this)
        car.setOwner(this);
    }

    public String toString() {
        return this.getName();
    }


}
