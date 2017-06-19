package de.librecarsharing;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


import de.librecarsharing.entities.DBEntity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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

        final DBEntity e1 = new DBEntity();
        e1.setName("entity 1");
        this.entityManager.persist(e1);

        final DBEntity e2 = new DBEntity();
        e2.setName("entity 2");
        this.entityManager.persist(e2);

    }

    private void queryData() {

        final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        final CriteriaQuery<DBEntity> query = builder.createQuery(DBEntity.class);
        final Root<DBEntity> from = query.from(DBEntity.class);


        final Predicate predicate = builder.equal(from.get("name"), "entity 1");
        final Order order = builder.asc(from.get("name"));

        query.select(from).where(predicate).orderBy(order);

        System.out.println("OUTPUT: " + this.entityManager.createQuery(query).getResultList());
    }

    private void shutdown() {
        this.entityManager.getTransaction().commit();
        this.entityManager.close();
        this.entityManagerFactory.close();
    }
}