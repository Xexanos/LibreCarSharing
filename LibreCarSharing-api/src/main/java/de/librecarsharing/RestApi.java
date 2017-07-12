package de.librecarsharing;

import de.librecarsharing.json.Addride;
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
import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;
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
    public Response getAllCarsFromUser(@PathParam("userId") final long userId) {

        final Subject subject = SecurityUtils.getSubject();
        if(subject.getPrincipal()!=null)
        {
            long subid=-1;
            subid = this.getIdFromUname(subject.getPrincipal().toString());
            if(subid==userId ||subject.hasRole("admin"));
            {
                final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
                final CriteriaQuery<DBCar> query = builder.createQuery(DBCar.class);
                final Root<DBCar> from = query.from(DBCar.class);
                final Join<DBCar, DBUser> join = from.join(DBCar_.owner);
                Predicate predicate = builder.equal(join.get(DBCommunity_.id), userId);
                Order order = builder.asc(from.get(DBCar_.name));
                query.select(from).where(predicate).orderBy(order);
                final List<DBCar> cars = this.entityManager.createQuery(query).getResultList();
                System.out.println("result " + cars);
                return Response.ok(cars.stream().map(CarWithoutRides::new).collect(Collectors.toList())).build();
            }
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    @Path("carsfromcommunity/{communityid}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCarsFromCommunity(@PathParam("communityid") final long comId) {


        final Subject subject = SecurityUtils.getSubject();
        DBCommunity community;
        if((community = this.entityManager.find(DBCommunity.class, comId)) != null)
        {
            if(community.getUsers().stream().map(DBUser::getUsername)
                    .collect(Collectors.toList()).contains(subject.getPrincipal())||subject.hasRole("admin"))
                {
                    List<CarWithoutRides> cars=community.getCars().stream().map(CarWithoutRides::new).collect(Collectors.toList());
                    System.out.println("result "+ cars);
                    return Response.ok(cars).build();
                }
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();



    }
    @Path("users/{communityid}/")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsersFromCommunity(@PathParam("communityid") final long comId) {
        final Subject subject = SecurityUtils.getSubject();

        final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        final CriteriaQuery<DBUser> query = builder.createQuery(DBUser.class);
        final Root<DBUser> from = query.from(DBUser.class);
        final Join<DBUser,DBCommunity> join = from.join(DBUser_.communities);
        Predicate predicate = builder.equal(join.get(DBCommunity_.id),comId);
        Order order = builder.asc(from.get(DBUser_.dispname));
        query.select(from).where(predicate).orderBy(order);
        final List<DBUser> users = this.entityManager.createQuery(query).getResultList();
        System.out.println("result "+ users);

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
    @Path("carwrides/{id}") // shiro needed
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
    @Path("addride")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addRide(final Addride data) {

        final Subject subject = SecurityUtils.getSubject();
        DBCar car;
        if((car = this.entityManager.find(DBCar.class, data.carId)) != null)
        {
            DBCommunity community;
            if((community= car.getCommunity())!=null)
            {

                if(community.getUsers().stream().map(DBUser::getUsername)
                        .collect(Collectors.toList()).contains(subject.getPrincipal())||subject.hasRole("admin"))
                {
                    Timestamp startStamp=new Timestamp(data.start);
                    Timestamp endStamp= new Timestamp(data.end);
                    final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
                    final CriteriaQuery<DBRide> query = builder.createQuery(DBRide.class);
                    final Root<DBRide> from = query.from(DBRide.class);
                    final Join<DBRide,DBCar> join = from.join(DBRide_.car);
                    Predicate predicate1 = builder.equal(join.get(DBCar_.id),data.carId);
                    Predicate predicate2 = builder.greaterThanOrEqualTo(from.get(DBRide_.end),startStamp);
                    Predicate predicate3 = builder.lessThanOrEqualTo(from.get(DBRide_.start),startStamp);
                    Predicate startpred =builder.and(predicate2,predicate3);
                    Predicate predicate4 = builder.greaterThanOrEqualTo(from.get(DBRide_.end),endStamp);
                    Predicate predicate5 = builder.lessThanOrEqualTo(from.get(DBRide_.start),endStamp);
                    Predicate endpred =builder.and(predicate4,predicate5);
                    Predicate intersect = builder.or(endpred,startpred);
                    Predicate whole = builder.and(predicate1,intersect);
                    Order order = builder.asc(from.get(DBRide_.end));
                    query.select(from).where(whole).orderBy(order);
                    List<DBRide> intersects= this.entityManager.createQuery(query).getResultList();
                    if(intersects.size()==0) {
                        final Set<DBRide> rides = car.getRides();//this.entityManager.createQuery(query).getResultList();

                        DBRide newride = new DBRide();
                        newride.setCar(car);
                        newride.setEnd(endStamp);
                        newride.setStart(startStamp);
                        newride.setName(this.entityManager.find(DBUser.class, getIdFromUname(subject.getPrincipal().toString())).getDispname());
                        this.entityManager.persist(newride);
                        car.addRide(newride);
                        System.out.println("added"+newride);
                        return Response.ok(new RideNoRef(newride)).build();
                    }
                    else {
                        return Response.ok(intersects.stream().map(RideNoRef::new).collect(Collectors.toList())).build();
                    }
                }

                return Response.status(Response.Status.UNAUTHORIZED).build();
            }

        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }


    @Path("register/{username}/{password}/")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(@PathParam("username") final String username,@PathParam("password") final String password) {
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

    @Path("createcommunity/{name}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCommunity(@PathParam("name") final String name) {
        final Subject subject = SecurityUtils.getSubject();
        final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        final CriteriaQuery<DBCommunity> query = builder.createQuery(DBCommunity.class);
        final Root<DBCommunity> from = query.from(DBCommunity.class);
        Predicate predicate = builder.equal(from.get(DBCommunity_.name),name);
        query.select(from).where(predicate);
        if(this.entityManager.createQuery(query).getResultList().size()==0) {
            final DBCommunity community = new DBCommunity();
            DBUser creator= entityManager.find(DBUser.class,getIdFromUname(subject.getPrincipal().toString()));
            community.setAdmin(creator);
            community.setName(name);
            this.entityManager.persist(community);
            return Response.ok(community).build();
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
        final Subject subject = SecurityUtils.getSubject();
        if((community=this.entityManager.find(DBCommunity.class,comId ))!=null);
        {
            if(community.getAdmin().getUsername().equals(subject.getPrincipal())) {
                if ((user = this.entityManager.find(DBUser.class, userId)) != null) {
                    community.addUser(user);
                }
            }
            else{
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
        }
        return Response.ok().build();
    }

    private Long getIdFromUname(String username)
    {
        final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        final CriteriaQuery<DBUser> query = builder.createQuery(DBUser.class);
        final Root<DBUser> from = query.from(DBUser.class);
        Predicate predicate = builder.equal(from.get(DBUser_.username),username);
        query.select(from).where(predicate);
        long id;
        List<DBUser> user= this.entityManager.createQuery(query).getResultList();
        if(user.size()==0)
            return null;
        else
        {
            return user.get(0).getId();
        }
    }


}