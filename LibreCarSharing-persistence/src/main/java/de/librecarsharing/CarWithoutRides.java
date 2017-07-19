package de.librecarsharing;

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
    public boolean isStatus(){return this.dbCar.isStatus();}
    public String getInfo(){return this.dbCar.getInfo();}
    public String getImageFile() {return dbCar.getImageFile();}
    public String getLicencePlate(){return this.dbCar.getLicencePlate();}
}
