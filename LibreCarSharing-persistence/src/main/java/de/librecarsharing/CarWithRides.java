package de.librecarsharing;

import java.util.Set;

/**
 * Created by fred on 30.06.17.
 */
public class CarWithRides {
    private DBCar dbCar;
    public CarWithRides(DBCar dbCar)
    {

        this.dbCar=dbCar;
        this.dbCar.getRides().size();
    }
    public long getId()
    {
        return this.dbCar.getId();

    }
    public String getName()
    {
        return this.dbCar.getName();
    }
    public Set<DBRide> getRides()
    {
        return this.dbCar.getRides();
    }

}
