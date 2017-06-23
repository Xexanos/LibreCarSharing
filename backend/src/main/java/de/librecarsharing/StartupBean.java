package de.librecarsharing;

/**
 * Created by Admin on 19.06.2017.
 */


import de.librecarsharing.entities.DBEntity;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
//import javax.persistence.criteria.*;
import java.util.Date;

@Singleton
@Startup
public class StartupBean {

    @PersistenceContext
    private EntityManager entityManager;
    private EntityManagerFactory entityManagerFactory;
    private final static String PERSISTENCE_UNIT = "pu";


    @PostConstruct
    public void startup() {


        init();
        //createData();
        shutdown();
    }
    private void init() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        this.entityManager = this.entityManagerFactory.createEntityManager();
        this.entityManager.getTransaction().begin();
    }
    private void createData() {

        final DBEntity e1 = new DBEntity();
        e1.setName("startup");
        this.entityManager.persist(e1);


    }


    @PreDestroy
    public void shutdown() {

        this.entityManager.getTransaction().commit();
        this.entityManager.close();
        this.entityManagerFactory.close();
    }
}
