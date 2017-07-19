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
    private String imageFile;
    private String licencePlate;
    private boolean status;
    private String info;
    private String type;




    public DBCar() {
        this.rides = new HashSet<DBRide>();
    }

    @Basic
    public String getLicencePlate() {
        return licencePlate;
    }

    public void setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
    }

    @Basic
    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
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
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic
    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @OneToMany(mappedBy = "car" ,cascade=CascadeType.REMOVE)
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

    public String toString() {return this.getName();}

}
