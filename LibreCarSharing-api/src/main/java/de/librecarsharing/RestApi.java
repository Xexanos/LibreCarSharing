package de.librecarsharing;

import de.librecarsharing.json.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;


@Path("")
@Transactional
public class RestApi {


    @PersistenceContext
    private EntityManager entityManager;


    @Path("user/{userid}/car")//{userId}
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCarsFromUser(@PathParam("userid")final long userId) {

        final Subject subject = SecurityUtils.getSubject();

        if(subject!=null&&subject.getPrincipal()!=null)
        {
            Long subid;


            subid = this.getIdFromUname(subject.getPrincipal().toString());

            if((subid!=null&&subid==userId) ||subject.hasRole("admin"));
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
    @Path("community/{comid}/car")//{communityid}
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCarsFromCommunity(@PathParam("comid")final long comId){
        final Subject subject = SecurityUtils.getSubject();
        DBCommunity community;
        if((community = this.entityManager.find(DBCommunity.class, comId)) != null)
        {
            if(community!=null) {
                if (subject!=null&&subject.getPrincipal()!=null&&(community.getUsers().stream().map(DBUser::getUsername)
                        .collect(Collectors.toList()).contains(subject.getPrincipal()))|| subject.hasRole("admin")) {
                    List<CarWithoutRides> cars = community.getCars().stream().map(CarWithoutRides::new).collect(Collectors.toList());
                    System.out.println("result " + cars);
                    return Response.ok(cars).build();
                }
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
    /*
    @Path("community/{comid}/user")//{communityid}/
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsersFromCommunity(@PathParam("comid") final long comId) {

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

    }*/
    @Path("user/{userid}/community")//{userid}
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllcommunitiesFromUser(@PathParam("userid") final long userId) {
        final Subject subject = SecurityUtils.getSubject();
        DBUser user =this.entityManager.find(DBUser.class, userId);
        if(user!=null) {
            if (subject!=null&&(user != null && subject.getPrincipal() != null && (subject.getPrincipal().equals(user.getUsername())) || subject.hasRole("admin"))) {
                final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
                final CriteriaQuery<DBCommunity> query = builder.createQuery(DBCommunity.class);
                final Root<DBCommunity> from = query.from(DBCommunity.class);
                final Join<DBCommunity, DBUser> join = from.join(DBCommunity_.users);
                Predicate predicate = builder.equal(join.get(DBCommunity_.id), userId);
                Order order = builder.asc(from.get(DBCommunity_.name));
                query.select(from).where(predicate).orderBy(order);
                final List<DBCommunity> communities = this.entityManager.createQuery(query).getResultList();
                System.out.println("result " + communities);
                return Response.ok(communities.stream().map(CommunityNoRef::new).collect(Collectors.toList())).build();
            }
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    @Path("allcommunities")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<CommunityNoRef> getAllcommunities() {
        final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        final CriteriaQuery<DBCommunity> query = builder.createQuery(DBCommunity.class);
        final Root<DBCommunity> from = query.from(DBCommunity.class);
        Order order = builder.asc(from.get(DBCommunity_.name));
        query.select(from).orderBy(order);
        final List<DBCommunity> communities = this.entityManager.createQuery(query).getResultList();
        System.out.println("result "+ communities);
        return communities.stream().map(CommunityNoRef::new).collect(Collectors.toList());
    }

    @Path("rides") //carid
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRidesFromCar(final Id data) {
        long carId =data.id;
        final Subject subject = SecurityUtils.getSubject();
        DBCar car;
        if((car = this.entityManager.find(DBCar.class, carId)) != null)
        {
        DBCommunity community;
        if((community= car.getCommunity())!=null)
        {

            if(subject.getPrincipal()!=null&&community.getUsers().stream().map(DBUser::getUsername)
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

    @Path("currentuser")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCurrentUser() {
        final Subject subject = SecurityUtils.getSubject();
        if(subject!=null)
        {
            if(subject.getPrincipal()!=null)
            {
                Long id= getIdFromUname(subject.getPrincipal().toString());
                if(id!=null)
                {
                    Response.ok(new UserNoRef(this.entityManager.find(DBUser.class, id))).build();
                }

            }
        }

        return Response.status(Response.Status.UNAUTHORIZED).build();

    }




    @Path("owner")//carid  //TODO: shiro Status remove
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UserNoRef getOwnerOfCar(final Id data) {

        long carId =data.id;

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
    @Path("carwrides")//eher für admins
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response carsWithRides(final Id data) {
        long id =data.id;
        final Subject subject = SecurityUtils.getSubject();
        if(subject.hasRole("admin"))
            return Response.ok(new CarWithRides(this.entityManager.find(DBCar.class, id))).build();
        else
            return Response.status(Response.Status.UNAUTHORIZED).build();

    }

    @Path("ride")//rideid
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRideById(final Id data) {
        long id =data.id;
        final Subject subject = SecurityUtils.getSubject();
        if(subject.hasRole("admin"))
            return Response.ok(this.entityManager.find(DBRide.class, id)).build();
        else
            return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @Path("car")//rideid
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCarById(final Id data) {
        long rideId =data.id;
        final Subject subject = SecurityUtils.getSubject();
        if(subject.hasRole("admin"))
            return Response.ok(this.entityManager.find(DBRide.class, rideId)).build();
        else
            return Response.status(Response.Status.UNAUTHORIZED).build();

    }

    @Path("community")//comid
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCommunityById(final Id data) {
        long comId =data.id;
        final Subject subject = SecurityUtils.getSubject();
        if(subject.hasRole("admin"))
            return Response.ok(this.entityManager.find(DBCommunity.class, comId)).build();
        else
            return Response.status(Response.Status.UNAUTHORIZED).build();

    }
    @Path("ride")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addRide(final Addride data) {


        final Subject subject = SecurityUtils.getSubject();
        DBCar car;
        if((car = this.entityManager.find(DBCar.class, data.carid)) != null &&data.start<data.end)
        {
            DBCommunity community;
            if((community= car.getCommunity())!=null)
            {

                if(subject.getPrincipal()!=null&&community.getUsers().stream().map(DBUser::getUsername)
                        .collect(Collectors.toList()).contains(subject.getPrincipal())||subject.hasRole("admin"))
                {
                    Timestamp startStamp=new Timestamp(data.start);
                    Timestamp endStamp= new Timestamp(data.end);
                    List<DBRide> intersects= getIntersects(startStamp,endStamp,data.carid);
                    if(intersects.size()==0) {
                        //final Set<DBRide> rides = car.getRides();//this.entityManager.createQuery(query).getResultList();
                        DBUser creator=this.entityManager.find(DBUser.class, getIdFromUname(subject.getPrincipal().toString()));
                        if(creator!=null) {
                            DBRide newride = new DBRide();
                            newride.setCar(car);
                            newride.setEnd(endStamp);
                            newride.setStart(startStamp);
                            newride.setCreator(creator);
                            newride.setName(creator.getDispname());
                            this.entityManager.persist(newride);
                            car.addRide(newride);
                            System.out.println("added" + newride);
                            return Response.ok(new RideNoRef(newride)).build();
                        }
                    }
                    else {
                        return Response.status(Response.Status.BAD_REQUEST).entity(intersects.stream().map(RideNoRef::new).collect(Collectors.toList())).build();
                    }
                }

                return Response.status(Response.Status.UNAUTHORIZED).build();
            }

        }
        System.out.println("principal: "+subject.getPrincipal()+"addride carid: "+data.carid+" start"+data.start +" end "+data.end +"end - start"+(data.end-data.start));
        return Response.status(Response.Status.BAD_REQUEST).build();
    }


    @Path("user")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(final Credentials data) {
        String email =data.email;
        String username=data.username;
        String password=data.password;
        String displayname=data.displayname;
        if(!isValidEmailAddress(email))
            return Response.status(Response.Status.BAD_REQUEST).build();
        final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        final CriteriaQuery<DBUser> query = builder.createQuery(DBUser.class);
        final Root<DBUser> from = query.from(DBUser.class);
        Predicate predicate = builder.equal(from.get(DBUser_.username),username);
        query.select(from).where(predicate);
        if(this.entityManager.createQuery(query).getResultList().size()==0) {
            final DBUser user = new DBUser();
            user.setDispname(displayname);
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);

            this.entityManager.persist(user);
            return Response.ok(user).build();
        }
        else
        {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }


    @Path("user")//update
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(final UpdateProfile data) {
        String email = data.email;
        String username = data.username;
        String newpassword= data.newpassword;
        String password = data.password;
        String displayname = data.displayname;
        String imagefile = data.imagefile;
        String principal;
        final Subject subject = SecurityUtils.getSubject();
        if(subject.getPrincipal()!=null) {
            principal = subject.getPrincipal().toString();
            if (principal != null && principal.equals(username)) {
                final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
                final CriteriaQuery<DBUser> query = builder.createQuery(DBUser.class);
                final Root<DBUser> from = query.from(DBUser.class);
                Predicate predicate = builder.equal(from.get(DBUser_.username), username);
                query.select(from).where(predicate);
                List<DBUser> users = this.entityManager.createQuery(query).getResultList();
                if (users.size() == 1) {
                    DBUser user = users.get(0);
                    if (user.getPassword() != null && password.equals(user.getPassword())) {
                        if (!isValidEmailAddress(email))
                        {
                            return Response.status(Response.Status.BAD_REQUEST).build();
                        }
                        user = this.entityManager.find(DBUser.class, user.getId());
                        if (user != null) {
                            user.setEmail(email);
                            user.setPassword(newpassword);
                            user.setDispname(displayname);
                            user.setImageFile(imagefile);
                        }
                        return Response.ok(new UserNoRef(user)).build();
                    }
                }
            }
        }

        return Response.status(Response.Status.BAD_REQUEST).build();
}

    @Path("ride")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRide(final UpdateRide data) {
        long id = data.rideid;
        Timestamp newstart = new Timestamp(data.newstart);
        Timestamp newend = new Timestamp(data.newend);
        String principal;
        final Subject subject = SecurityUtils.getSubject();
        if (subject.getPrincipal() != null) {
            principal = subject.getPrincipal().toString();
            if (principal != null) {
                final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
                final CriteriaQuery<DBRide> query = builder.createQuery(DBRide.class);
                final Root<DBRide> from = query.from(DBRide.class);
                Predicate predicate = builder.equal(from.get(DBRide_.id), id);
                query.select(from).where(predicate);
                List<DBRide> rides = this.entityManager.createQuery(query).getResultList();
                if (rides.size() == 1) {
                    DBRide ride = rides.get(0);
                    System.out.println("D"+ride.getCreator().getId());
                    ride.getCreator().getId();
                    if (ride.getCreator().getId() == this.getIdFromUname(subject.getPrincipal().toString())) {
                        System.out.println("E");
                        ride = this.entityManager.find(DBRide.class, ride.getId());
                        if (ride != null) {
                            List<DBRide> intersects = this.getIntersects(newstart, newend, ride.getCar().getId());
                            if (intersects.size() == 0 || (intersects.size() == 1 && intersects.get(0).getId() == id)) {
                                ride.setStart(newstart);
                                ride.setEnd(newend);

                                return Response.ok(new RideNoRef(ride)).build();

                            }

                        }
                    }

                }
            }
        }
            System.out.println("I");
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
    @Path("car") //TODO: weitermachen
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRide(final UpdateCar data) {
        long id = data.id;
        int color =data.color;
        String imageFile=data.imageFile;
        String info=data.info;
        String licencePlate=data.licencePlate;
        String location=data.location;
        boolean status=data.status;
        String principal;
        final Subject subject = SecurityUtils.getSubject();
        
        System.out.println("I");
        return Response.status(Response.Status.BAD_REQUEST).build();
    }


    @Path("ride/{id}")
    @DELETE
    @Consumes(MediaType.TEXT_PLAIN)
    public Response deletride(@PathParam("id") final long id) {
        String principal;
        final Subject subject = SecurityUtils.getSubject();
        if (subject.getPrincipal() != null) {
            principal = subject.getPrincipal().toString();
            if (principal != null) {
                DBRide ride =this.entityManager.find(DBRide.class,id);
                if(ride!=null)
                {
                    DBUser user = this.entityManager.find(DBUser.class,getIdFromUname(principal));
                    if(user!=null)
                    {
                        if(ride.getCar().getOwner().getId()==user.getId()||subject.hasRole("admin"))
                        {
                            this.entityManager.remove(ride);
                            return Response.ok().build();
                        }
                        else{
                            return Response.status(Response.Status.UNAUTHORIZED).build();
                        }
                    }
                }
                else{
                    return Response.status(Response.Status.NOT_FOUND).build();
                }

            }
        }

        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @Path("createcommunity")//name
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCommunity(final Createcommunity data) {
        String name= data.name;
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

    @Path("community/{comid}/user")//userid comid
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addusertcommunity(@PathParam("comid") long comId, final Id data) {

        long userId=data.id;
        DBCommunity community;
        DBUser user;
        final Subject subject = SecurityUtils.getSubject();
        if(subject!=null)
        {
            if((community=this.entityManager.find(DBCommunity.class,comId ))!=null);
            {
                if(subject.getPrincipal()!=null) {
                    if (community.getAdmin().getUsername().equals(subject.getPrincipal())) {
                        if ((user = this.entityManager.find(DBUser.class, userId)) != null) {
                            if(community.getUsers().contains(user))
                                return Response.status(Response.Status.BAD_REQUEST).build();
                            else
                            {
                                community.addUser(user);
                                return Response.ok().build();}
                        }
                    } else {
                        return Response.status(Response.Status.UNAUTHORIZED).build();
                    }
                }
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
    private boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }
    private List<DBRide> getIntersects(Timestamp startStamp,Timestamp endStamp,long carid)
    {

        final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        final CriteriaQuery<DBRide> query = builder.createQuery(DBRide.class);
        final Root<DBRide> from = query.from(DBRide.class);
        final Join<DBRide,DBCar> join = from.join(DBRide_.car);
        Predicate predicate1 = builder.equal(join.get(DBCar_.id),carid);
        Predicate predicate2 = builder.greaterThanOrEqualTo(from.get(DBRide_.end),startStamp);
        Predicate predicate3 = builder.lessThanOrEqualTo(from.get(DBRide_.start),startStamp);

        Predicate startpred =builder.and(predicate2,predicate3);
        Predicate predicate4 = builder.greaterThanOrEqualTo(from.get(DBRide_.end),endStamp);
        Predicate predicate5 = builder.lessThanOrEqualTo(from.get(DBRide_.start),endStamp);
        Predicate endpred =builder.and(predicate4,predicate5);
        Predicate predicate6 = builder.greaterThanOrEqualTo(from.get(DBRide_.start),startStamp);
        Predicate predicate7 = builder.lessThanOrEqualTo(from.get(DBRide_.end),endStamp);
        Predicate enclosepred =builder.and(predicate6,predicate7);
        Predicate intersect = builder.or(endpred,startpred);
        intersect=builder.or(intersect,enclosepred);
        Predicate whole = builder.and(predicate1,intersect);
        Order order = builder.asc(from.get(DBRide_.end));
        query.select(from).where(whole).orderBy(order);

        return this.entityManager.createQuery(query).getResultList();

    }



}