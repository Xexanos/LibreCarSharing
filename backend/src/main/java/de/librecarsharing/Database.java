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
        final DBCommunity community1= new DBCommunity();
        final DBCommunity community2= new DBCommunity();
        community1.setName("community1");
        community2.setName("community2");
        final DBUser tim = new DBUser();
        final DBUser mark = new DBUser();
        final DBUser lisa = new DBUser();
        tim.setName("Tim");
        mark.setName("Mark");
        lisa.setName("Lisa");
        final DBCar car1 = new DBCar();
        car1.setName("car1");
        tim.addCar(car1);
        final DBCar car2 = new DBCar();
        car2.setName("car2");
        lisa.addCar(car2);
        final DBRide ride1= new DBRide();
        ride1.setName("ride1");
        final DBRide ride2= new DBRide();
        ride2.setName("ride2");
        final DBRide ride3= new DBRide();
        ride3.setName("ride3");
        final DBRide ride4= new DBRide();
        ride4.setName("ride4");
        car1.addRide(ride1);
        car1.addRide(ride2);
        car2.addRide(ride3);
        car2.addRide(ride4);
        mark.addCommunity(community1);
        community2.addUser(lisa);


        community1.addCar(car1);
        community2.addCar(car2);
        car1.setLocation("A");
        car2.setLocation("B");



        this.entityManager.persist(community2);
        this.entityManager.persist(community1);
        this.entityManager.persist(tim);
        this.entityManager.persist(mark);
        this.entityManager.persist(lisa);
        this.entityManager.persist(ride3);
        this.entityManager.persist(ride4);
        this.entityManager.persist(car2);
        this.entityManager.persist(ride1);
        this.entityManager.persist(ride2);
        this.entityManager.persist(car1);


    }

    private void queryData() {
        final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();

        // SELECT r.name FROM DBCar AS c, DBRide AS r where c.id= r.car_id AND c.name = 'car1' ORDERBY r.name;
        final CriteriaQuery<DBRide> rideQuery = builder.createQuery(DBRide.class);
        final Root<DBRide> fromRide = rideQuery.from(DBRide.class);
        final Join<DBRide,DBCar> ridejoin = fromRide.join(DBRide_.car);
        Predicate predicate = builder.equal(ridejoin.get(DBCar_.name),"car1");
        Order order = builder.asc(fromRide.get(DBRide_.name));
        rideQuery.select(fromRide).where(predicate).orderBy(order);
        final List<DBRide> result = this.entityManager.createQuery(rideQuery).getResultList();
        System.out.println("result "+ result);



        final CriteriaQuery<DBUser> query = builder.createQuery(DBUser.class);
        final Root<DBUser> fromUser = query.from(DBUser.class);
        final Join<DBUser,DBCommunity> communityJoin = fromUser.join(DBUser_.communities);
        predicate = builder.equal(communityJoin.get(DBCommunity_.name),"community1");
        order = builder.asc(fromUser.get(DBUser_.name));
        query.select(fromUser).where(predicate).orderBy(order);
        final List<DBUser> users = this.entityManager.createQuery(query).getResultList();
        System.out.println("result "+ users);


        DBCommunity com =entityManager.find(DBCommunity.class, new Long(1));//get first entry from hashset
        System.out.println("community1's users :"+ com.getUsers());
        DBUser use = (DBUser) com.getUsers().toArray()[0];
        System.out.println("cars from community1's user lisa "+use.getCars());

        final DBCar car3 = new DBCar();
        car3.setName("car3");
        entityManager.persist(car3);
        entityManager.find(DBCommunity.class,(long)1).addCar(car3);
        System.out.println(use);
        use.addCar(car3);
        use = (DBUser) com.getUsers().toArray()[0];
        System.out.println(use);
        System.out.println("cars from community1's user lisa "+use.getCars());


    }

    private void shutdown() {
        this.entityManager.getTransaction().commit();
        this.entityManager.close();
        this.entityManagerFactory.close();
    }
}