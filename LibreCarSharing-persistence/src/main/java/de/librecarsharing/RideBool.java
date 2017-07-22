package de.librecarsharing;

import java.sql.Timestamp;


public class RideBool {

    public RideBool(DBRide ride,boolean editable){
        this.dbRide=ride;
        this.editable=editable;
    }
    private DBRide dbRide;
    private boolean editable;
    public Timestamp getStart() {return this.dbRide.getStart();}
    public Timestamp getEnd() {return this.dbRide.getEnd();}
    public String getName() {return this.dbRide.getName();}
    public long getId(){return this.dbRide.getId();}
    public boolean isEditable(){return editable;}

}
