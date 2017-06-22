package de.librecarsharing;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


import de.librecarsharing.entities.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.*;
import java.util.List;

public class Database {

    public static void main(String[] args) {
        final Database database = new Database();

        database.init();
        //database.createData();
        database.queryData();
        database.shutdown();
    }


    private EntityManager entityManager;
    private EntityManagerFactory entityManagerFactory;
    private final static String PERSISTENCE_UNIT = "pu";


    private void init() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        this.entityManager = this.entityManagerFactory.createEntityManager();
        this.entityManager.getTransaction().begin();
    }

    private void createData() {

        final DBCar car1 = new DBCar();
        car1.setName("car1");
        final DBRide ride1= new DBRide();
        ride1.setName("ride1");
        final DBRide ride2= new DBRide();
        ride1.setId(0);
        ride2.setId(1);
        ride2.setName("ride2");
        car1.addRide(ride1);
        car1.addRide(ride2);


        this.entityManager.persist(ride1);
        this.entityManager.persist(ride2);
        this.entityManager.persist(car1);


    }

    private void queryData() {

        final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        final CriteriaQuery<DBCar> query = builder.createQuery(DBCar.class);
        final Root<DBCar> from = query.from(DBCar.class);
        final Join<DBCar,DBRide> ridejoin = from.join(DBCar_.rides);
        final Predicate predicate = builder.equal(ridejoin.get(DBRide_.name),"ride1");
        final Order order = builder.asc(from.get(DBCar_.name));
        query.select(from);//.where(predicate);

        final List<DBCar> result = this.entityManager.createQuery(query).getResultList();
        System.out.println("result "+ result.get(0).getRides().toArray()[1]);
        //System.out.println("OUTPUT: " + this.entityManager.createQuery(query).getResultList().get(0).getRides().get(1));
    }

    private void shutdown() {
        this.entityManager.getTransaction().commit();
        this.entityManager.close();
        this.entityManagerFactory.close();
    }
}