package de.librecarsharing;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;


@Entity
public class DBRide extends DBIdentified{

    private String name;
    private DBCar car;
    private Timestamp start;
    private Timestamp end;
    private DBUser creator;

    @ManyToOne
    public DBUser getCreator() {
        return creator;
    }

    public void setCreator(DBUser creator) {
        this.creator = creator;
    }

    @Basic
    public Timestamp getStart() {
        return start;
    }

    public void setStart(Timestamp start) {
        this.start = start;
    }
    @Basic
    public Timestamp getEnd() {
        return end;
    }

    public void setEnd(Timestamp end) {
        this.end = end;
    }

    @Basic
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }





    @ManyToOne
    public DBCar getCar() {
        return car;
    }
    public void setCar(DBCar car) {
        this.car = car;
    }

    public String toString() {
        return this.getName();
    }
}
