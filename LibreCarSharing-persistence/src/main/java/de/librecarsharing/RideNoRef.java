package de.librecarsharing;

import javax.persistence.Basic;
import java.sql.Date;

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
    public Date getStart() {return this.dbRide.getStart();}
    public Date getEnd() {return this.dbRide.getEnd();}
    public String getName() {return this.dbRide.getName();}
}
