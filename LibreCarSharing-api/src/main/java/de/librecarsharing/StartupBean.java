package de.librecarsharing;





import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;

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

    }

    private void createData() {
        final DBCommunity community1= new DBCommunity();
        final DBCommunity community2= new DBCommunity();
        community1.setName("community1");
        community2.setName("community2");
        final DBUser tim = new DBUser();
        final DBUser mark = new DBUser();
        final DBUser lisa = new DBUser();
        tim.setDisplayName("Tim");
        mark.setDisplayName("Mark");
        lisa.setDisplayName("Lisa");
        tim.setEmail("tim@tim.de");
        lisa.setEmail("lisa@lisa.de");
        mark.setEmail("mark@mark.de");
        tim.setImageFile("linktofile");
        lisa.setImageFile("linktofile");
        mark.setImageFile("linktofile");
        final DBCar car1 = new DBCar();
        car1.setName("car1");
        car1.setSeats(5);
        car1.setLocation("A");
        car1.setInfo("car1");
        car1.setImageFile("https://upload.wikimedia.org/wikipedia/commons/6/6f/Golf_2_v2.jpg");
        car1.setColor(3);
        tim.addCar(car1);
        final DBCar car2 = new DBCar();
        car2.setName("car2");
        car2.setLocation("B");
        car2.setSeats(6);
        car2.setInfo("car2");
        car2.setImageFile("https://upload.wikimedia.org/wikipedia/commons/6/6f/Golf_2_v2.jpg");
        car2.setColor(3);
        final DBType smallCar=new DBType();
        final DBType transporter= new DBType();
        smallCar.setName("Kleinwagen");
        transporter.setName("Transporter");
        car2.setType(smallCar);
        car1.setType(transporter);
        lisa.addCar(car2);
        final DBRide ride1= new DBRide();
        ride1.setStart(Timestamp.valueOf("2017-01-01 12:00:00"));
        ride1.setEnd(Timestamp.valueOf("2017-01-01 13:00:00"));
        ride1.setCreator(mark);
        ride1.setName("ride1");
        final DBRide ride2= new DBRide();
        ride2.setStart(Timestamp.valueOf("2017-01-01 15:00:00"));
        ride2.setEnd(Timestamp.valueOf("2017-01-01 16:00:00"));
        ride2.setName("ride2");
        ride2.setCreator(tim);
        final DBRide ride3= new DBRide();
        ride3.setStart(Timestamp.valueOf("2017-01-01 12:00:00"));
        ride3.setEnd(Timestamp.valueOf("2017-01-01 13:00:00"));
        ride3.setName("ride3");
        ride3.setCreator(lisa);
        final DBRide ride4= new DBRide();
        ride4.setStart(Timestamp.valueOf("2017-01-01 13:00:00"));
        ride4.setEnd(Timestamp.valueOf("2017-01-01 14:00:00"));
        ride4.setName("ride4");
        ride4.setCreator(lisa);
        car1.addRide(ride1);
        car1.addRide(ride2);
        car2.addRide(ride3);
        car2.addRide(ride4);
        car1.setStatus(true);
        car2.setStatus(true);
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
        community1.setAdmin(tim);
        community2.setAdmin(lisa);



        this.entityManager.persist(community2);
        this.entityManager.persist(community1);
        this.entityManager.persist(smallCar);
        this.entityManager.persist(transporter);
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



}
