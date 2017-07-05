package de.librecarsharing;

import javax.persistence.*;
import java.sql.Date;


@Entity
public class DBRide extends DBIdentified{

    private String name;
    private DBCar car;
    private Date start;
    private Date end;


    @Basic
    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }
    @Basic
    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
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
