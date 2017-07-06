package de.librecarsharing.auth;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAccount;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Transactional
public class DatabaseAuthenticator {

	@PersistenceContext
	private EntityManager entityManager;

	public AuthenticationInfo fetchAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		final String user = (String) token.getPrincipal();
		return new SimpleAccount(user, user.toCharArray(), WT2Realm.REALM);
	}

}