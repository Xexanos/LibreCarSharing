package de.librecarsharing;

import javax.persistence.*;
import java.util.*;

@Entity
public class DBCommunity extends DBIdentified{


    private String name;
    private Set<DBCar> cars;
    private Set<DBUser> users;
    private DBUser admin;

    public DBCommunity() {
        users= new HashSet<DBUser>();
        cars = new HashSet<DBCar>();
    }
    @ManyToOne
    public DBUser getAdmin() {
        return admin;
    }

    public void setAdmin(DBUser admin) {
        this.admin = admin;

    }

    @ManyToMany(mappedBy = "communities")
    public Set<DBUser> getUsers() {
        return users;
    }

    public void setUsers(Set<DBUser> users) {
        this.users = users;
    }
    public void addUser(DBUser user) {

        this.users.add(user);
        if (!user.getCommunities().contains(this)) {
            user.addCommunity(this);
        }
    }
    @OneToMany(mappedBy ="community")
    public Set<DBCar> getCars() {
        return cars;
    }

    public void setCars(Set<DBCar> cars) {
        this.cars = cars;
    }

    public void addCar(DBCar car) {
        this.cars.add(car);
        if (car.getCommunity()!=this) {
            car.setCommunity(this);
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
