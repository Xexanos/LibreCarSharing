package de.librecarsharing.entities;



import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(DBCar.class)
public abstract class DBCar_ {


        public static volatile SetAttribute<DBCar,DBRide> rides;
        public static volatile SingularAttribute<DBCar, String> name;
        public static volatile SingularAttribute<DBCar, Long> id;
}
