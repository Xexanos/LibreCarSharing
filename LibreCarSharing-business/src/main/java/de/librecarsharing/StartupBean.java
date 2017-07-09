package de.librecarsharing;

/**
 * Created by Admin on 19.06.2017.
 */




import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.List;

//import javax.persistence.criteria.*;


@Singleton
@Startup
public class StartupBean {

    @PersistenceContext
    private EntityManager entityManager;
    private EntityManagerFactory entityManagerFactory;
    private final static String PERSISTENCE_UNIT = "pu";


    @PostConstruct
    public void startup() {


        //init();
        //createData(); //uncomment to generate sample data
        //  queryData();
        shutdown();
    }

    private void createData() {
        final DBCommunity community1= new DBCommunity();
        final DBCommunity community2= new DBCommunity();
        community1.setName("community1");
        community2.setName("community2");
        final DBUser tim = new DBUser();
        final DBUser mark = new DBUser();
        final DBUser lisa = new DBUser();
        tim.setDispname("Tim");
        mark.setDispname("Mark");
        lisa.setDispname("Lisa");
        final DBCar car1 = new DBCar();
        car1.setName("car1");
        car1.setSeats(5);
        tim.addCar(car1);
        final DBCar car2 = new DBCar();
        car2.setName("car2");
        car2.setSeats(6);
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
        community1.addUser(tim);
        mark.setUsername("mark");
        mark.setPassword("mark");
        tim.setUsername("tim");
        tim.setPassword("tim");
        lisa.setUsername("lisa");
        lisa.setPassword("lisa");
        community1.addCar(car1);
        community2.addCar(car2);
        car1.setLocation("A");
        car2.setLocation("B");
        //community1.setAdmin(tim);
        //community2.setAdmin(lisa);



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
        order = builder.asc(fromUser.get(DBUser_.dispname));
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
    @PreDestroy
    public void shutdown() {

       // this.entityManager.getTransaction().commit();
        //this.entityManager.close();
        //this.entityManagerFactory.close();
    }
}
