package de.librecarsharing.auth.permission;


import de.librecarsharing.DBUser;
import org.apache.shiro.authz.Permission;

public class ViewFirstFiveNewsItemsPermission implements Permission {

	private final DBUser user;

	public ViewFirstFiveNewsItemsPermission(final DBUser user) {
		this.user = user;
	}
	@Override
	public boolean implies(Permission p) {
		return false;
	}

	public boolean check() {
		return this.user.getId() < 5;
	}
}
