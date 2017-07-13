package de.librecarsharing;

import javax.persistence.Basic;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by fred on 30.06.17.
 */
public class RideNoRef {

    public RideNoRef(DBRide ride){
        this.dbRide=ride;
    }
    private DBRide dbRide;
    private String name;
    private Date start;
    private Date end;
    public Timestamp getStart() {return this.dbRide.getStart();}
    public Timestamp getEnd() {return this.dbRide.getEnd();}
    public String getName() {return this.dbRide.getName();}
}
