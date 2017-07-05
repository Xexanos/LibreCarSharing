package de.librecarsharing;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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
    public String getInfo(){return this.dbCar.getInfo();}
    public Set<RideNoRef> getRides()
    {
        return this.dbCar.getRides().stream().map(RideNoRef::new).collect(Collectors.toSet());

    }
    public int getSeats(){return this.dbCar.getSeats();}
    public int getColor(){return this.dbCar.getColor();}
    public String getStatus(){return this.dbCar.getStatus();}
}
