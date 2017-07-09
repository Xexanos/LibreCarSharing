package de.librecarsharing;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.subject.Subject;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Path("api")
@Transactional
public class RestApi {


    @PersistenceContext
    private EntityManager entityManager;

    @Path("carsfromuser/{userId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public List<CarWithoutRides> getAllCarsFromUser(@PathParam("userId") final long userId) {

        final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        final CriteriaQuery<DBCar> query = builder.createQuery(DBCar.class);
        final Root<DBCar> from = query.from(DBCar.class);
        final Join<DBCar,DBUser> join = from.join(DBCar_.owner);
        Predicate predicate = builder.equal(join.get(DBCommunity_.id),userId);
        Order order = builder.asc(from.get(DBCar_.name));
        query.select(from).where(predicate).orderBy(order);
        final List<DBCar> cars = this.entityManager.createQuery(query).getResultList();

        System.out.println("result "+ cars);
        return cars.stream().map(CarWithoutRides::new).collect(Collectors.toList());

    }
    @Path("carsfromcommunity/{communityid}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public List<CarWithoutRides> getAllCarsFromCommunity(@PathParam("communityid") final long comId) {

        final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        final CriteriaQuery<DBCar> query = builder.createQuery(DBCar.class);
        final Root<DBCar> from = query.from(DBCar.class);
        final Join<DBCar,DBCommunity> join = from.join(DBCar_.community);
        Predicate predicate = builder.equal(join.get(DBCommunity_.id),comId);
        Order order = builder.asc(from.get(DBCar_.name));
        query.select(from).where(predicate).orderBy(order);
        final List<DBCar> cars = this.entityManager.createQuery(query).getResultList();

        System.out.println("result "+ cars);
        return cars.stream().map(CarWithoutRides::new).collect(Collectors.toList());

    }
    @Path("users/{communityid}/")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsersFromCommunity(@PathParam("communityid") final long comId) {

        final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        final CriteriaQuery<DBUser> query = builder.createQuery(DBUser.class);
        final Root<DBUser> from = query.from(DBUser.class);
        final Join<DBUser,DBCommunity> join = from.join(DBUser_.communities);
        Predicate predicate = builder.equal(join.get(DBCommunity_.id),comId);
        Order order = builder.asc(from.get(DBUser_.dispname));
        query.select(from).where(predicate).orderBy(order);
        final List<DBUser> users = this.entityManager.createQuery(query).getResultList();
        System.out.println("result "+ users);
        final Subject subject = SecurityUtils.getSubject();


        List<String> usernames=users.stream().map(DBUser::getUsername).collect(Collectors.toList());

        if (usernames.contains(subject.getPrincipal()))
            return Response.ok(users.stream().map(UserNoRef::new).collect(Collectors.toList())).build();
        else
            return Response.status(Response.Status.UNAUTHORIZED).build();

    }
    @Path("communitysformuser/{userid}/")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCommunitysFromUser(@PathParam("userid") final long userId) {
        final Subject subject = SecurityUtils.getSubject();
        DBUser user =this.entityManager.find(DBUser.class, userId);
        if((user!=null&&subject.getPrincipal().equals(user.getUsername()))||subject.hasRole("admin")){
            final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
            final CriteriaQuery<DBCommunity> query = builder.createQuery(DBCommunity.class);
            final Root<DBCommunity> from = query.from(DBCommunity.class);
            final Join<DBCommunity, DBUser> join = from.join(DBCommunity_.users);
            Predicate predicate = builder.equal(join.get(DBCommunity_.id), userId);
            Order order = builder.asc(from.get(DBCommunity_.name));
            query.select(from).where(predicate).orderBy(order);
            final List<DBCommunity> communitys = this.entityManager.createQuery(query).getResultList();
            System.out.println("result " + communitys);
            return Response.ok(communitys.stream().map(CommunityNoRef::new).collect(Collectors.toList())).build();
        }
        else
            return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    @Path("allcommunities/")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public List<CommunityNoRef> getAllCommunitys() {
        final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        final CriteriaQuery<DBCommunity> query = builder.createQuery(DBCommunity.class);
        final Root<DBCommunity> from = query.from(DBCommunity.class);
        Order order = builder.asc(from.get(DBCommunity_.name));
        query.select(from).orderBy(order);
        final List<DBCommunity> communitys = this.entityManager.createQuery(query).getResultList();
        System.out.println("result "+ communitys);
        return communitys.stream().map(CommunityNoRef::new).collect(Collectors.toList());
    }

    @Path("rides/{carid}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRidesFromCar(@PathParam("carid") final long carId) {

        final Subject subject = SecurityUtils.getSubject();
        DBCar car;
        if((car = this.entityManager.find(DBCar.class, carId)) != null)
        {
        DBCommunity community;
        if((community= car.getCommunity())!=null)
        {

            if(community.getUsers().stream().map(DBUser::getUsername)
                .collect(Collectors.toList()).contains(subject.getPrincipal())||subject.hasRole("admin"))
                {
                final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
                final CriteriaQuery<DBRide> query = builder.createQuery(DBRide.class);
                final Root<DBRide> from = query.from(DBRide.class);
                final Join<DBRide,DBCar> join = from.join(DBRide_.car);
                Predicate predicate = builder.equal(join.get(DBCar_.id),carId);
                Order order = builder.asc(from.get(DBRide_.id));
                query.select(from).where(predicate).orderBy(order);
                final List<DBRide> rides = this.entityManager.createQuery(query).getResultList();
                System.out.println("result "+ rides);
                return Response.ok(rides.stream().map(RideNoRef::new).collect(Collectors.toList())).build();
                }
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }

        }
        return Response.status(Response.Status.BAD_REQUEST).build();


    }

    @Path("owner/{carid}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public UserNoRef getOwnerOfCar(@PathParam("carid") final long carId) {


        final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        final CriteriaQuery<DBUser> query = builder.createQuery(DBUser.class);
        final Root<DBUser> from = query.from(DBUser.class);
        final Join<DBUser,DBCar> join = from.join(DBUser_.cars);
        Predicate predicate = builder.equal(join.get(DBCar_.id),carId);
        query.select(from).where(predicate);
        final DBUser owner = this.entityManager.createQuery(query).getResultList().get(0);
        System.out.println("result "+ owner);
        return new UserNoRef(owner);

    }
    @Path("carwrides/{id}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response carsWithRides(@PathParam("id") final long id) {
        CarWithRides car=new CarWithRides(this.entityManager.find(DBCar.class, id));
        return Response.ok(car).build();
    }

    @Path("ride/{id}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRideById(@PathParam("id") final long id) {
        final Subject subject = SecurityUtils.getSubject();
        if(subject.hasRole("admin"))
            return Response.ok(this.entityManager.find(DBRide.class, id)).build();
        else
            return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @Path("car/{id}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCarById(@PathParam("id") final long id) {
        final Subject subject = SecurityUtils.getSubject();
        if(subject.hasRole("admin"))
            return Response.ok(this.entityManager.find(DBRide.class, id)).build();
        else
            return Response.status(Response.Status.UNAUTHORIZED).build();

    }

    @Path("community/{id}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCommunityById(@PathParam("id") final long id) {
        final Subject subject = SecurityUtils.getSubject();
        if(subject.hasRole("admin"))
            return Response.ok(this.entityManager.find(DBCommunity.class, id)).build();
        else
            return Response.status(Response.Status.UNAUTHORIZED).build();

    }



    @Path("register/{username}/{password}/")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(@PathParam("username") final String username,@PathParam("username") final String password) {
        final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        final CriteriaQuery<DBUser> query = builder.createQuery(DBUser.class);
        final Root<DBUser> from = query.from(DBUser.class);
        Predicate predicate = builder.equal(from.get(DBUser_.username),username);
        query.select(from).where(predicate);
        if(this.entityManager.createQuery(query).getResultList().size()==0) {
            final DBUser user = new DBUser();
            user.setDispname(username);
            user.setUsername(username);
            user.setPassword(password);
            this.entityManager.persist(user);
            return Response.ok(user).build();
        }
        else
        {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @Path("adduser/{userId}/to/{comId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    public Response create(@PathParam("userId") final long userId, @PathParam("comId") final long comId) {
        DBCommunity community;
        DBUser user;
        if((community=this.entityManager.find(DBCommunity.class,comId ))!=null);
        {
            if((user= this.entityManager.find(DBUser.class,userId))!=null)
            {
                community.addUser(user);
            }
        }


        return Response.ok().build();
    }


}