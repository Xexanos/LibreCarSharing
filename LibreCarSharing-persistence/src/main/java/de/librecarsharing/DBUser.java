package de.librecarsharing;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
public class DBUser extends DBIdentified{

    private String dispname;
    private String username;
    private String password;
    private Set<DBCar> cars;        //cars owned by user
    private Set<DBCommunity> communities;


    public DBUser() {
        communities= new HashSet<DBCommunity>();
        cars= new HashSet<DBCar>();
    }
    @Basic
    @Column(unique=true)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    public String getDispname() {
        return dispname;
    }
    public void setDispname(String dispname) {
        this.dispname = dispname;
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
        return this.getDispname();
    }


}
