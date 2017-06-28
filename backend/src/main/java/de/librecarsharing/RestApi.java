package de.librecarsharing;

import de.librecarsharing.entities.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;





@Path("/rest")
@Transactional
public class RestApi {


    @PersistenceContext
    private EntityManager entityManager;

    @Path("/{communityid}/cars")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public List<DBCar> getAllCarsFromCommunity(@PathParam("communityid") final String communityid) {
        long comId;
        try {
            comId = Long.parseLong(communityid);
        }
        catch(Exception e)
        {
            return null;
        }

        final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        final CriteriaQuery<DBCar> query = builder.createQuery(DBCar.class);
        final Root<DBCar> from = query.from(DBCar.class);
        final Join<DBCar,DBCommunity> join = from.join(DBCar_.community);
        Predicate predicate = builder.equal(join.get(DBCommunity_.id),comId);
        Order order = builder.asc(from.get(DBCar_.name));
        query.select(from).where(predicate).orderBy(order);
        final List<DBCar> cars = this.entityManager.createQuery(query).getResultList();
        System.out.println("result "+ cars);
        return cars;

    }
    @Path("/{communityid}/users")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public List<DBUser> getAllUsersFromCommunity(@PathParam("communityid") final long comId) {


        final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        final CriteriaQuery<DBUser> query = builder.createQuery(DBUser.class);
        final Root<DBUser> from = query.from(DBUser.class);
        final Join<DBUser,DBCommunity> join = from.join(DBUser_.communities);
        Predicate predicate = builder.equal(join.get(DBCommunity_.id),comId);
        Order order = builder.asc(from.get(DBUser_.name));
        query.select(from).where(predicate).orderBy(order);
        final List<DBUser> users = this.entityManager.createQuery(query).getResultList();
        System.out.println("result "+ users);
        return users;

    }

    @Path("/{carid}/rides")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public List<DBRide> getAllRidesFromCar(@PathParam("carid") final long carId) {


        final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        final CriteriaQuery<DBRide> query = builder.createQuery(DBRide.class);
        final Root<DBRide> from = query.from(DBRide.class);
        final Join<DBRide,DBCar> join = from.join(DBRide_.car);
        Predicate predicate = builder.equal(join.get(DBCar_.id),carId);
        Order order = builder.asc(from.get(DBRide_.id));
        query.select(from).where(predicate).orderBy(order);
        final List<DBRide> rides = this.entityManager.createQuery(query).getResultList();
        System.out.println("result "+ rides);
        return rides ;

    }

    @Path("/{carid}/owner")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public DBUser getOwnerOfCar(@PathParam("carid") final long carId) {


        final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        final CriteriaQuery<DBUser> query = builder.createQuery(DBUser.class);
        final Root<DBUser> from = query.from(DBUser.class);
        final Join<DBUser,DBCar> join = from.join(DBUser_.cars);
        Predicate predicate = builder.equal(join.get(DBCar_.id),carId);
        query.select(from).where(predicate);
        final DBUser owner = this.entityManager.createQuery(query).getResultList().get(0);
        System.out.println("result "+ owner);
        return owner ;

    }


    @Path("/ride/{id}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public DBRide getRideById(@PathParam("id") final long id) {
        return this.entityManager.find(DBRide.class, id);
    }
    @Path("/car/{id}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public DBRide getCarById    (@PathParam("id") final long id) {
        return this.entityManager.find(DBRide.class, id);
    }
    @Path("/community/{id}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public DBCommunity getCommunityById(@PathParam("id") final long id) {
        return this.entityManager.find(DBCommunity.class, id);
    }

















    // beispiel
    /*
    @PersistenceContext
    private EntityManager entityManager;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<DBNews> readAllAsJSON() {
        final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        final CriteriaQuery<DBNews> query = builder.createQuery(DBNews.class);

        final Root<DBNews> from = query.from(DBNews.class);

        query.select(from);

        return this.entityManager.createQuery(query).getResultList();
    }

    @Path("/{id}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public DBNews readAsJSON(@PathParam("id") final long id) {
        return this.entityManager.find(DBNews.class, id);
    }

    @Path("/{id}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_XML)
    public DBNews readAsXML(@PathParam("id") final long id) {
        return this.entityManager.find(DBNews.class, id);
    }

    // An example of how to misuse the API and do something unRESTful
    @Path("/new/{headline}/{content}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(@PathParam("headline") final String headline, @PathParam("content") final String content) {

        final DBNews news = new DBNews();

        news.setHeadline(headline);
        news.setContent(content);
        news.setPublishedOn(new Date());

        this.entityManager.persist(news);

        return Response.ok(news).build();
    }

    // More idiomatic way of creating items
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(final DBNews param) {

        final DBNews news = new DBNews();

        news.setHeadline(param.getHeadline());
        news.setContent(param.getContent());
        news.setPublishedOn(new Date());

        this.entityManager.persist(news);

        return Response.ok(news).build();
    }
    */
}