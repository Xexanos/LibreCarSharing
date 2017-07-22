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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Path("")
@Transactional
public class RestApi {

    @PersistenceContext
    private EntityManager entityManager;

    @Path("user/{userid}/car") // all OWNED by user x
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCarsFromUser(@PathParam("userid") final long userId) {

        final Subject subject = SecurityUtils.getSubject();
        System.out.println("a" + subject);
        if (subject != null && subject.getPrincipal() != null) {
            Long subjectId = this.getIdFromUsername(subject.getPrincipal().toString());
            System.out.println("b" + subjectId);
            if ((subjectId != null && subjectId == userId) || subject.hasRole("admin")) {
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
        System.out.println("failed");
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @Path("community/{comid}/car")//{communityid}
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCarsFromCommunity(@PathParam("comid") final long communityId) {
        final Subject subject = SecurityUtils.getSubject();
        DBCommunity community = this.entityManager.find(DBCommunity.class, communityId);
        if (community != null) {
            if (subject != null && subject.getPrincipal() != null) {
                if ((community.getUsers().stream().map(DBUser::getUsername)
                        .collect(Collectors.toList()).contains(subject.getPrincipal().toString())) || subject.hasRole("admin")) {
                    List<CarWithoutRides> cars = community.getCars().stream().map(CarWithoutRides::new).collect(Collectors.toList());
                    System.out.println("result " + cars);
                    return Response.ok(cars).build();
                }
            }
            return Response.status(Response.Status.UNAUTHORIZED).build();
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
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCommunitiesFromUser(@PathParam("userid") final long userId) {
        final Subject subject = SecurityUtils.getSubject();
        DBUser user = this.entityManager.find(DBUser.class, userId);
        if (user != null) {
            if (subject != null && (subject.getPrincipal() != null && (subject.getPrincipal().toString().equals(user.getUsername())) || subject.hasRole("admin"))) {
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

    @Path("communities")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<CommunityNoRef> getAllCommunities() {
        final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        final CriteriaQuery<DBCommunity> query = builder.createQuery(DBCommunity.class);
        final Root<DBCommunity> from = query.from(DBCommunity.class);
        Order order = builder.asc(from.get(DBCommunity_.name));
        query.select(from).orderBy(order);
        final List<DBCommunity> communities = this.entityManager.createQuery(query).getResultList();
        System.out.println("result " + communities);
        return communities.stream().map(CommunityNoRef::new).collect(Collectors.toList());
    }

    @Path("car/{carid}/ride") //carid
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRidesFromCar(@PathParam("carid") final long carId) {
        String principal;
        final Subject subject = SecurityUtils.getSubject();
        DBCar car;
        if ((car = this.entityManager.find(DBCar.class, carId)) != null) {
            DBCommunity community;
            if ((community = car.getCommunity()) != null) {

                if (subject.getPrincipal() != null) {
                    principal = subject.getPrincipal().toString();
                    Long subjectId = getIdFromUsername(principal);
                    if (subjectId != null) {
                        if (community.getUsers().stream().map(DBUser::getUsername)
                                .collect(Collectors.toList()).contains(subject.getPrincipal().toString()) || subject.hasRole("admin")) {
                            final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
                            final CriteriaQuery<DBRide> query = builder.createQuery(DBRide.class);
                            final Root<DBRide> from = query.from(DBRide.class);
                            final Join<DBRide, DBCar> join = from.join(DBRide_.car);
                            Predicate predicate = builder.equal(join.get(DBCar_.id), carId);
                            Order order = builder.asc(from.get(DBRide_.id));
                            query.select(from).where(predicate).orderBy(order);
                            final List<DBRide> rides = this.entityManager.createQuery(query).getResultList();
                            System.out.println("result " + rides);
                            List<RideBool> rideBools = new ArrayList<>();
                            for (DBRide ride : rides) {
                                rideBools.add(new RideBool(ride, ride.getCreator().getId() == subjectId));
                            }
                            return Response.ok(rideBools).build();
                        }
                    }
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
        if (subject != null) {
            if (subject.getPrincipal() != null) {
                Long id = getIdFromUsername(subject.getPrincipal().toString());
                if (id != null) {
                    DBUser user = this.entityManager.find(DBUser.class, id);
                    if (user != null)
                        return Response.ok(new UserNoRef(user)).build();
                    else
                        return Response.status(Response.Status.NOT_FOUND).build();
                }

            }
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }


    @Path("car/{carid}/owner")//carid
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOwnerOfCar(@PathParam("carid") final long carId) {

        final Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            if (subject.getPrincipal() != null) {
                DBCar car = this.entityManager.find(DBCar.class, carId);
                if (car != null)
                    if (car.getCommunity().getUsers().stream().map(DBUser::getUsername).collect(Collectors.toList())
                            .contains(subject.getPrincipal().toString()) || subject.hasRole("admin"))
                        return Response.ok(new UserNoRef(car.getOwner())).build();
                    else
                        return Response.status(Response.Status.NOT_FOUND).build();
            }
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @Path("carwrides/{carid}")//eher für admins
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response carsWithRides(@PathParam("carid") final long id) {
        final Subject subject = SecurityUtils.getSubject();
        if (subject.hasRole("admin"))
            return Response.ok(new CarWithRides(this.entityManager.find(DBCar.class, id))).build();
        else
            return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @Path("ride/{rideid}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRideById(@PathParam("rideid") final long rideId) {

        final Subject subject = SecurityUtils.getSubject();
        if (subject == null || subject.getPrincipal() == null)
            return Response.status(Response.Status.UNAUTHORIZED).build();
        String principal = subject.getPrincipal().toString();
        Long subjectId = getIdFromUsername(principal);
        if (subjectId != null) {
            DBUser user = this.entityManager.find(DBUser.class, subjectId);
            DBRide ride = this.entityManager.find(DBRide.class, rideId);
            if (user != null && ride != null) {
                if (subject.hasRole("admin") || ride.getCar().getCommunity().getUsers().stream().map(DBUser::getId).collect(Collectors.toList()).contains(user.getId()))
                    return Response.ok(new RideNoRef(ride)).build();
                else
                    return Response.status(Response.Status.UNAUTHORIZED).build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @Path("car/{carid}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCarById(@PathParam("carid") final long carId) {
        final Subject subject = SecurityUtils.getSubject();
        if (subject == null || subject.getPrincipal() == null)
            return Response.status(Response.Status.UNAUTHORIZED).build();
        String principal = subject.getPrincipal().toString();
        Long subjectId = getIdFromUsername(principal);
        if (subjectId != null) {
            DBUser user = this.entityManager.find(DBUser.class, subjectId);
            DBCar car = this.entityManager.find(DBCar.class, carId);
            if (user != null && car != null) {
                if (subject.hasRole("admin") || car.getCommunity().getUsers().stream().map(DBUser::getId).collect(Collectors.toList()).contains(user.getId()))
                    return Response.ok(new CarWithoutRides(car)).build();
                else
                    return Response.status(Response.Status.UNAUTHORIZED).build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @Path("community/{comid}")//comid
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCommunityById(@PathParam("comid") final long comId) {

        final Subject subject = SecurityUtils.getSubject();
        if (subject == null || subject.getPrincipal() == null)
            return Response.status(Response.Status.UNAUTHORIZED).build();
        String principal = subject.getPrincipal().toString();
        Long subjectId = getIdFromUsername(principal);
        if (subjectId != null) {
            DBUser user = this.entityManager.find(DBUser.class, subjectId);
            DBCommunity community = this.entityManager.find(DBCommunity.class, comId);
            if (user != null && community != null) {
                if (subject.hasRole("admin") || community.getUsers().stream().map(DBUser::getId).collect(Collectors.toList()).contains(user.getId()))
                    return Response.ok(new CommunityNoRef(community)).build();
                else
                    return Response.status(Response.Status.UNAUTHORIZED).build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).build();

    }

    @Path("car/{carid}/ride")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addRide(@PathParam("carid") final long carId, final JsonRide data) {

        String name = data.name;
        if (name == null)
            name = "";

        final Subject subject = SecurityUtils.getSubject();
        DBCar car;
        if ((car = this.entityManager.find(DBCar.class, carId)) != null && data.start < data.end) {
            DBCommunity community;
            if ((community = car.getCommunity()) != null) {
                if (subject == null)
                    return Response.status(Response.Status.UNAUTHORIZED).build();
                if (subject.getPrincipal() != null && community.getUsers().stream().map(DBUser::getUsername)
                        .collect(Collectors.toList()).contains(subject.getPrincipal().toString()) || subject.hasRole("admin")) {
                    Timestamp startStamp = new Timestamp(data.start);
                    Timestamp endStamp = new Timestamp(data.end);
                    List<DBRide> intersects = getIntersects(startStamp, endStamp, carId);
                    if (intersects.size() == 0) {
                        Long creatorId = getIdFromUsername(subject.getPrincipal().toString());
                        if (creatorId == null) {
                            return Response.status(Response.Status.BAD_REQUEST).build();
                        }
                        DBUser creator = this.entityManager.find(DBUser.class, creatorId);
                        if (creator != null) {
                            if (!isValidNotJustSpace(name)) {
                                name = creator.getDisplayName();
                            }
                            DBRide newRide = new DBRide();
                            newRide.setCar(car);
                            newRide.setEnd(endStamp);
                            newRide.setStart(startStamp);
                            newRide.setCreator(creator);
                            newRide.setName(name);
                            this.entityManager.persist(newRide);
                            car.addRide(newRide);
                            System.out.println("added" + newRide);
                            return Response.ok(new RideNoRef(newRide)).build();
                        }
                    } else {
                        return Response.status(Response.Status.BAD_REQUEST).entity(intersects.stream().map(RideNoRef::new).collect(Collectors.toList())).build();
                    }
                }
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }


    @Path("user/{userid}")//update TODO:fix
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(final UpdateUser data, @PathParam("userid") final long userId) {
        String email = data.email;
        String username = data.username;
        String password = data.password;
        String displayName = data.displayName;
        String imageFile = data.imageFile;
        String newPassword = data.newPassword;
        System.out.println(email + ", " + username + ", " + password + ", " + displayName + ", " + imageFile + ", " + newPassword);
        if (username == null || password == null || email == null)
            return Response.status(Response.Status.BAD_REQUEST).build();
        username = username.toLowerCase();
        if (displayName == null || !isValidNotJustSpace(displayName))
            displayName = username;
        if (newPassword == null)
            newPassword = password;
        String principal;

        final Subject subject = SecurityUtils.getSubject();
        if (subject != null && subject.getPrincipal() != null) {
            principal = subject.getPrincipal().toString();
            if (principal.equals(username)) {
                DBUser user = this.entityManager.find(DBUser.class, userId);
                if (user != null && user.getUsername() != null && user.getUsername().equals(username) &&
                        user.getPassword() != null && (password.equals(user.getPassword()) || subject.hasRole("admin"))) {
                    if (!isValidEmailAddress(email) || !isValidPassword(newPassword)) {
                        System.out.println(isValidEmailAddress(email) + "" + isValidPassword(newPassword));
                        return Response.status(Response.Status.BAD_REQUEST).build();
                    }
                    System.out.println(user.getEmail() + "" + email);
                    if (!user.getEmail().equals(email)) {
                        final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
                        final CriteriaQuery<DBUser> query = builder.createQuery(DBUser.class);
                        final Root<DBUser> from = query.from(DBUser.class);
                        Predicate predicate = builder.equal(from.get(DBUser_.email), email);
                        query.select(from).where(predicate);
                        if (this.entityManager.createQuery(query).getResultList().size() > 0) {
                            return Response.status(Response.Status.UNAUTHORIZED).build();
                        }
                    }

                    user.setEmail(email);
                    user.setDisplayName(displayName);
                    user.setImageFile(imageFile);
                    user.setPassword(newPassword);
                    return Response.ok(new UserNoRef(user)).build();
                }
            }
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }


    @Path("ride/{rideid}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRide(@PathParam("rideid") final long rideId, final JsonRide data) {
        String name = data.name;
        if (name == null)
            name = "";

        Timestamp newStart = new Timestamp(data.start);
        Timestamp newEnd = new Timestamp(data.end);
        String principal;
        final Subject subject = SecurityUtils.getSubject();
        if (subject.getPrincipal() != null) {
            principal = subject.getPrincipal().toString();
            if (principal != null) {
                DBRide ride = this.entityManager.find(DBRide.class, rideId);

                if (ride != null) {
                    System.out.println("D" + ride.getCreator().getId());

                    Long subjectId = this.getIdFromUsername(subject.getPrincipal().toString());
                    if (subjectId != null && ride.getCreator().getId() == subjectId) {
                        DBUser user = entityManager.find(DBUser.class, subjectId);
                        if (user != null) {
                            List<DBRide> intersects = this.getIntersects(newStart, newEnd, ride.getCar().getId());
                            if (intersects.size() == 0 || (intersects.size() == 1 && intersects.get(0).getId() == rideId)) {
                                if (!isValidNotJustSpace(name))
                                    name = user.getDisplayName();
                                ride.setStart(newStart);
                                ride.setEnd(newEnd);
                                ride.setName(name);
                                return Response.ok(new RideNoRef(ride)).build();

                            }
                            return Response.status(Response.Status.NOT_FOUND).build();
                        }
                    }
                    return Response.status(Response.Status.UNAUTHORIZED).build();
                }
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @Path("car/{carid}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCar(@PathParam("carid") final long carId, final JsonCar data) {
        int color = data.color;
        String imageFile = data.imageFile;
        String info = data.info;
        String licencePlate = data.licencePlate;
        String location = data.location;
        String type = data.type;
        int seats = data.seats;
        boolean status = data.status;
        String principal;
        final Subject subject = SecurityUtils.getSubject();
        if (subject != null && subject.getPrincipal() != null) {
            principal = subject.getPrincipal().toString();
            DBCar car = this.entityManager.find(DBCar.class, carId);
            if (car != null) {
                DBType typeToSet = checkType(type);
                if (car.getOwner().getUsername().equals(principal) || subject.hasRole("admin")) {
                    car.setColor(color);
                    car.setImageFile(imageFile);
                    car.setInfo(info);
                    car.setLicencePlate(licencePlate);
                    car.setLocation(location);
                    car.setStatus(status);
                    car.setType(typeToSet);
                    car.setSeats(seats);
                    return Response.ok().build();

                } else {
                    return Response.status(Response.Status.UNAUTHORIZED).build();
                }
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @Path("user") //register
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(final Credentials data) {
        String email = data.email;
        String username = data.username;
        String password = data.password;
        String displayName = data.displayName;
        if (email == null || username == null || password == null || displayName == null)
            return Response.status(Response.Status.BAD_REQUEST).build();
        if (isValidNotJustSpace(username)) {
            username = username.toLowerCase();
            if (!isValidNotJustSpace(displayName))
                displayName = username;
            if (!isValidEmailAddress(email) || !isValidPassword(password))
                return Response.status(Response.Status.BAD_REQUEST).build();
            final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
            final CriteriaQuery<DBUser> query = builder.createQuery(DBUser.class);
            final Root<DBUser> from = query.from(DBUser.class);
            Predicate predicate = builder.equal(from.get(DBUser_.username), username);
            query.select(from).where(predicate);
            if (this.entityManager.createQuery(query).getResultList().size() == 0) {
                predicate = builder.equal(from.get(DBUser_.email), email);
                query.select(from).where(predicate);
                if (this.entityManager.createQuery(query).getResultList().size() != 0) {
                    return Response.status(Response.Status.BAD_REQUEST).build();
                }
                final DBUser user = new DBUser();
                user.setDisplayName(displayName);
                user.setUsername(username);
                user.setPassword(password);
                user.setEmail(email);

                this.entityManager.persist(user);
                return Response.ok(new UserNoRef(user)).build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @Path("ride/{rideid}")
    @DELETE
    @Consumes(MediaType.TEXT_PLAIN)
    public Response deletRide(@PathParam("rideid") final long rideId) {
        String principal;
        final Subject subject = SecurityUtils.getSubject();
        if (subject.getPrincipal() != null) {
            principal = subject.getPrincipal().toString();
            if (principal != null) {
                DBRide ride = this.entityManager.find(DBRide.class, rideId);
                if (ride != null) {
                    DBUser user = this.entityManager.find(DBUser.class, getIdFromUsername(principal));
                    if (user != null) {
                        if (ride.getCar().getOwner().getId() == user.getId() || subject.hasRole("admin")) {
                            this.entityManager.remove(ride);
                            return Response.ok().build();
                        } else {
                            return Response.status(Response.Status.UNAUTHORIZED).build();
                        }
                    }
                } else {
                    return Response.status(Response.Status.NOT_FOUND).build();
                }
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @Path("car/{carid}")
    @DELETE
    @Consumes(MediaType.TEXT_PLAIN)
    public Response deleteCar(@PathParam("carid") final long carId) {
        String principal;
        final Subject subject = SecurityUtils.getSubject();
        if (subject.getPrincipal() != null) {
            principal = subject.getPrincipal().toString();
            if (principal != null) {
                DBCar car = this.entityManager.find(DBCar.class, carId);
                if (car != null) {
                    DBUser user = this.entityManager.find(DBUser.class, getIdFromUsername(principal));
                    if (user != null) {
                        if (car.getOwner().getId() == user.getId() || subject.hasRole("admin")) {
                            this.entityManager.remove(car);
                            return Response.ok().build();
                        } else {
                            return Response.status(Response.Status.UNAUTHORIZED).build();
                        }
                    }
                } else {
                    return Response.status(Response.Status.NOT_FOUND).build();
                }
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @Path("community")//name
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCommunity(final Createcommunity data) {
        String name = data.name;
        if (name == null)
            return Response.status(Response.Status.BAD_REQUEST).build();
        final Subject subject = SecurityUtils.getSubject();
        final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        final CriteriaQuery<DBCommunity> query = builder.createQuery(DBCommunity.class);
        final Root<DBCommunity> from = query.from(DBCommunity.class);
        Predicate predicate = builder.equal(from.get(DBCommunity_.name), name);
        query.select(from).where(predicate);
        if (this.entityManager.createQuery(query).getResultList().size() == 0) {
            if (subject != null && subject.getPrincipal() != null) {
                DBUser creator = entityManager.find(DBUser.class, getIdFromUsername(subject.getPrincipal().toString()));
                if (creator != null) {
                    final DBCommunity community = new DBCommunity();
                    community.setAdmin(creator);
                    community.setName(name);
                    this.entityManager.persist(community);
                    return Response.ok(new CommunityNoRef(community)).build();
                }
            }
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }


    @Path("community/{comid}/car")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createCarInCommunity(@PathParam("comid") long comId, final JsonCar data) {
        int color = data.color;
        String imageFile = data.imageFile;
        String info = data.info;
        String licencePlate = data.licencePlate;
        String location = data.location;
        String type = data.type;
        String name = data.name;
        int seats = data.seats;
        boolean status = data.status;
        String principal;
        if (info == null)
            info = "";
        if (licencePlate == null)
            licencePlate = "";
        if (location == null)
            location = "";
        if (imageFile == null)
            imageFile = "";
        if (name == null)
            name = "";
        if (type == null || isValidNotJustSpace(type))
            return Response.status(Response.Status.BAD_REQUEST).build();
        DBCommunity community = this.entityManager.find(DBCommunity.class, comId);
        if (community == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        final Subject subject = SecurityUtils.getSubject();
        if (subject != null && subject.getPrincipal() != null) {
            principal = subject.getPrincipal().toString();
            Long userId = getIdFromUsername(principal);
            if (userId != null) {
                DBUser user = this.entityManager.find(DBUser.class, userId);
                if (user.getCommunities().stream().map(DBCommunity::getId).collect(Collectors.toList())
                        .contains(comId) || subject.hasRole("admin")) {
                    DBType typeToSet = checkType(type);
                    DBCar car = new DBCar();
                    car.setName(name);
                    car.setColor(color);
                    car.setImageFile(imageFile);
                    car.setInfo(info);
                    car.setLicencePlate(licencePlate);
                    car.setLocation(location);
                    car.setStatus(status);
                    car.setSeats(seats);
                    this.entityManager.persist(car);
                    user.addCar(car);
                    car.setType(typeToSet);
                    community.addCar(car);

                    return Response.ok().build();
                }
            }
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @Path("types")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTypeList() {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<DBType> query = builder.createQuery(DBType.class);
        final Root<DBType> from = query.from(DBType.class);

        query.select(from);
        List<DBType> types = entityManager.createQuery(query).getResultList();
        return Response.ok(types).build();
    }

    @Path("currentuser/car/{typeid}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCarsFromUserWithType(@PathParam("typeid") final long typeId) {

        final Subject subject = SecurityUtils.getSubject();

        if (subject != null && subject.getPrincipal() != null) {
            Long subjectId = this.getIdFromUsername(subject.getPrincipal().toString());
            if ((subjectId != null) || subject.hasRole("admin")) {
                final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
                final CriteriaQuery<DBCar> query = builder.createQuery(DBCar.class);
                final Root<DBCar> from = query.from(DBCar.class);
                final Join<DBCar, DBUser> join = from.join(DBCar_.owner);
                Predicate predicate = builder.equal(join.get(DBCommunity_.id), subjectId);
                Predicate typepred = builder.equal(from.get(DBCar_.type.getName()), typeId);
                Predicate where = builder.or(predicate, typepred);
                Order order = builder.asc(from.get(DBCar_.name));
                query.select(from).where(where).orderBy(order);
                final List<DBCar> cars = this.entityManager.createQuery(query).getResultList();
                System.out.println("result " + cars);
                List<CarWithoutRides> response = cars.stream().map(CarWithoutRides::new).collect(Collectors.toList());
                return Response.ok(response).build();
            }
        }
        System.out.println("failed");
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @Path("community/{comid}/user/{userid}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUserToCommunity(@PathParam("comid") long comId, @PathParam("userid") final long userId) {

        DBCommunity community;
        DBUser user;
        final Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            community = this.entityManager.find(DBCommunity.class, comId);
            if (community != null) {
                if (subject.getPrincipal() != null) {
                    if (community.getAdmin().getUsername().equals(subject.getPrincipal())) {
                        if ((user = this.entityManager.find(DBUser.class, userId)) != null) {
                            if (community.getUsers().contains(user)) {
                                return Response.status(Response.Status.BAD_REQUEST).build();
                            } else {
                                community.addUser(user);
                                return Response.ok().build();
                            }
                        }
                    } else {
                        return Response.status(Response.Status.UNAUTHORIZED).build();
                    }
                }
            }
        }
        return Response.ok().build();
    }

    @Path("user/{userid}") //TODO: use flags instead of removing things permanently
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("userid") final long userid, final Credentials data) {
        String password = data.password;

        final Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            if (subject.getPrincipal() != null) {
                Long subjectId;
                subjectId = this.getIdFromUsername(subject.getPrincipal().toString());
                if ((subjectId != null)) {
                    DBUser user = this.entityManager.find(DBUser.class, userid);
                    if (user != null) {
                        if (user.getId() == subjectId && user.getPassword().equals(password) || subject.hasRole("admin")) {

                            user.setCommunities(Collections.emptySet());
                            for (DBCommunity community : user.getAdministartes()) {
                                community.setAdmin(null);
                            }

                            entityManager.remove(user);
                            return Response.ok().build();
                        }
                    }
                    return Response.status(Response.Status.NOT_FOUND).build();
                }
            }
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    private Long getIdFromUsername(String username) {
        final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        final CriteriaQuery<DBUser> query = builder.createQuery(DBUser.class);
        final Root<DBUser> from = query.from(DBUser.class);
        Predicate predicate = builder.equal(from.get(DBUser_.username), username);
        query.select(from).where(predicate);

        List<DBUser> user = this.entityManager.createQuery(query).getResultList();
        if (user.size() == 0)
            return null;
        else {
            return user.get(0).getId();
        }
    }

    private boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddress = new InternetAddress(email);
            emailAddress.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    private boolean isValidPassword(String password) {
        Pattern letters = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$");
        Matcher m = letters.matcher(password);
        return (m.matches());
    }

    private boolean isValidNotJustSpace(String str) {
        Pattern letters = Pattern.compile(".*\\S.*");
        Matcher m = letters.matcher(str);
        return (m.matches());
    }

    private List<DBRide> getIntersects(Timestamp startStamp, Timestamp endStamp, long carId) {
        final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        final CriteriaQuery<DBRide> query = builder.createQuery(DBRide.class);
        final Root<DBRide> from = query.from(DBRide.class);
        final Join<DBRide, DBCar> join = from.join(DBRide_.car);
        Predicate predicate1 = builder.equal(join.get(DBCar_.id), carId);
        Predicate predicate2 = builder.greaterThanOrEqualTo(from.get(DBRide_.end), startStamp);
        Predicate predicate3 = builder.lessThanOrEqualTo(from.get(DBRide_.start), startStamp);

        Predicate startPredicate = builder.and(predicate2, predicate3);
        Predicate predicate4 = builder.greaterThanOrEqualTo(from.get(DBRide_.end), endStamp);
        Predicate predicate5 = builder.lessThanOrEqualTo(from.get(DBRide_.start), endStamp);
        Predicate endPredicate = builder.and(predicate4, predicate5);
        Predicate predicate6 = builder.greaterThanOrEqualTo(from.get(DBRide_.start), startStamp);
        Predicate predicate7 = builder.lessThanOrEqualTo(from.get(DBRide_.end), endStamp);
        Predicate enclosePredicate = builder.and(predicate6, predicate7);
        Predicate intersect = builder.or(endPredicate, startPredicate);
        intersect = builder.or(intersect, enclosePredicate);
        Predicate whole = builder.and(predicate1, intersect);
        Order order = builder.asc(from.get(DBRide_.end));
        query.select(from).where(whole).orderBy(order);

        return this.entityManager.createQuery(query).getResultList();
    }

    private DBType checkType(String type) {
        final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        final CriteriaQuery<DBType> query = builder.createQuery(DBType.class);
        final Root<DBType> from = query.from(DBType.class);
        query.select(from);
        DBType typeToSet = null;
        List<DBType> types = this.entityManager.createQuery(query).getResultList();
        List<String> typeNames = types.stream().map(DBType::getName).collect(Collectors.toList());
        if (!typeNames.contains(type)) {
            DBType newType = new DBType();
            newType.setName(type);
            entityManager.persist(newType);
            typeToSet = newType;
        } else {
            Predicate where = builder.equal(from.get(DBType_.name), type);
            query.select(from).where(where);
            if ((types = this.entityManager.createQuery(query).getResultList()).size() == 1) {
                typeToSet = types.get(0);
            }

        }
        return typeToSet;
    }
}