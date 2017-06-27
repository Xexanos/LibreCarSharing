package de.librecarsharing;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;





@Path("/news")
@Transactional
public class RestApi {




    @Path("/{Cars}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public DBNews readAsJSON(@PathParam("id") final long id) {
        return this.entityManager.find(DBNews.class, id);
    }












    // beispiel
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
}