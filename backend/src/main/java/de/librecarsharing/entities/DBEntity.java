package de.librecarsharing.entities;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Created by Admin on 16.06.2017.
 */


@Entity
public class DBEntity extends DBIdentified {

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
}
