package de.librecarsharing;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
public class DBUser extends DBIdentified{

    private String displayName;
    private String username;
    private String password;
    private Set<DBCar> cars;        //cars owned by user
    private Set<DBCommunity> communities;
    private Set<DBCommunity> administartes;
    private String email;
    private String imageFile;
    private Set<DBRide> rides;


    public DBUser() {
        communities= new HashSet<DBCommunity>();
        administartes= new HashSet<DBCommunity>();
        cars= new HashSet<DBCar>();
        rides= new HashSet<DBRide>();
    }
    @Basic
    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    @Basic
    @Column(unique=true)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @OneToMany(mappedBy = "admin")
    public Set<DBCommunity> getAdministartes() {
        return administartes;
    }

    public void setAdministartes(Set<DBCommunity> administartes) {
        this.administartes = administartes;
    }

    public void addAdministartes(DBCommunity community) {
        this.administartes.add(community);
        if (community.getAdmin()!=this) {
            community.setAdmin(this);
        }
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
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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
    @OneToMany(mappedBy = "creator")
    public Set<DBRide> getRides() {
        return rides;
    }

    public void setRides(Set<DBRide> rides) {
        this.rides = rides;
    }

    public void addRide(DBRide ride)
    {
        this.rides.add(ride);
        if(ride.getCreator()!=this)
        ride.setCreator(this);
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
        return this.getDisplayName();
    }


}
