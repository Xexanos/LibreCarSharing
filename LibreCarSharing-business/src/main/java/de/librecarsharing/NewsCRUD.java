package de.librecarsharing;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;

@Path("/news")
@Transactional
public class NewsCRUD {

	@PersistenceContext
	private EntityManager entityManager;

	@GET
	@Path("newest")
	@Produces(MediaType.APPLICATION_JSON)
	public Response readNewestNews() {
		final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		final CriteriaQuery<DBNews> query = builder.createQuery(DBNews.class);

		final Root<DBNews> from = query.from(DBNews.class);

		final Order order = builder.desc(from.get(DBNews_.publishedOn));

		query.select(from).orderBy(order);

		return Response.ok(
				this.entityManager.createQuery(query).setMaxResults(1).getSingleResult()
		).build();
	}

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
