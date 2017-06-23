package de.librecarsharing.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
public class DBUser extends DBIdentified{

    private String name;
    private Set<DBCommunity> communities;


    public void DBUser() {communities= new HashSet<DBCommunity>();}

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

    public String toString() {
        return this.getName();
    }


}
