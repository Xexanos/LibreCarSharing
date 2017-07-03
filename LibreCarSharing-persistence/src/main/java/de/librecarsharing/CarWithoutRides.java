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
        public long getId()
        {
            return this.dbCar.getId();

        }
        public String getName()
        {
            return this.dbCar.getName();
        }



}
