package de.librecarsharing.entities;



import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(DBRide.class)
public abstract class DBRide_ {


    public static volatile SingularAttribute<DBRide,DBCar> car;
    public static volatile SingularAttribute<DBRide, String> name;
    public static volatile SingularAttribute<DBRide, Long> id;
    public static volatile SingularAttribute<DBRide, Long> IDCar;
}
