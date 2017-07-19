package de.librecarsharing;

import javax.persistence.*;


@Entity
public class DBType extends DBIdentified {

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
