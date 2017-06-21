package de.librecarsharing;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


import de.librecarsharing.entities.DBCar;
import de.librecarsharing.entities.DBEntity;
import de.librecarsharing.entities.DBRide;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.*;

public class Database {

    public static void main(String[] args) {
        final Database database = new Database();

        database.init();
        database.createData();
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
        ride2.setName("ride2");
        car1.addRide(ride1);
        car1.addRide(ride1);


        this.entityManager.persist(ride1);
        this.entityManager.persist(ride1);
        this.entityManager.persist(car1);


    }

    private void queryData() {

        final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        final CriteriaQuery<DBCar> query = builder.createQuery(DBCar.class);
        final Root<DBCar> from = query.from(DBCar.class);
        Path<Integer> joined = from.join("IDCar").get("name");


        //final Predicate predicate = builder.equal(from.get("rides"), "");
        //final Order order = builder.asc(from.get(""));


        System.out.println("OUTPUT: " + this.entityManager.createQuery(query).getResultList());
    }

    private void shutdown() {
        this.entityManager.getTransaction().commit();
        this.entityManager.close();
        this.entityManagerFactory.close();
    }
}