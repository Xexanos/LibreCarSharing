package de.librecarsharing.auth;

import de.librecarsharing.DBUser;
import de.librecarsharing.DBUser_;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAccount;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class DatabaseAuthenticator {

	@PersistenceContext
	private EntityManager entityManager;

	public AuthenticationInfo fetchAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		final String username = (String) token.getPrincipal();
		final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		final CriteriaQuery<DBUser> query = builder.createQuery(DBUser.class);
		final Root<DBUser> from = query.from(DBUser.class);
		Predicate predicate = builder.equal(from.get(DBUser_.username),username);
		query.select(from).where(predicate);

		List<DBUser> list= this.entityManager.createQuery(query).getResultList();
		if(list.size()!=1)
			return null;
		else
		{
			DBUser user = list.get(0);

			return new SimpleAccount(user.getUsername(), user.getPassword(), WT2Realm.REALM);
		}



	}

}