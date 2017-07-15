package de.librecarsharing;

import java.util.Set;

/**
 * Created by fred on 30.06.17.
 */
public class CarWithoutRides {

    private DBCar dbCar;
    public CarWithoutRides(DBCar dbCar)
    {
        this.dbCar=dbCar;
    }
    public String getType() {return this.dbCar.getType();}
    public long getId()
    {
        return this.dbCar.getId();
    }
    public int getColor(){return this.dbCar.getColor();}
    public String getName()
        {
            return this.dbCar.getName();
        }
    public int getSeats(){return this.dbCar.getSeats();}
    public String getStatus(){return this.dbCar.getStatus();}
    public String getInfo(){return this.dbCar.getInfo();}
    public String getImageFile() {return dbCar.getImageFile();}
}
