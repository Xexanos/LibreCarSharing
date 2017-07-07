package de.librecarsharing.auth;

import de.librecarsharing.*;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAccount;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;

@Transactional
public class DatabaseAuthenticator {

	@PersistenceContext
	private EntityManager entityManager;

	public AuthenticationInfo fetchAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		final String loginname = (String) token.getPrincipal();
		final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		final CriteriaQuery<DBUser> query = builder.createQuery(DBUser.class);
		final Root<DBUser> from = query.from(DBUser.class);
		Predicate predicate = builder.equal(from.get(DBUser_.username),loginname);
		query.select(from).where(predicate);
		final DBUser user = this.entityManager.createQuery(query).getSingleResult();
		return new SimpleAccount(user.getUsername(), user.getPassword(), WT2Realm.REALM);
	}

}