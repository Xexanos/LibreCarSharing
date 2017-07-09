package de.librecarsharing.auth.permission;

import de.librecarsharing.DBCommunity;
import org.apache.shiro.authz.Permission;

/**
 * Created by fred on 09.07.17.
 */
public class AccessCommunity implements Permission {

        private final DBCommunity community;

        public AccessCommunity(final DBCommunity community) {
            this.community = community;
        }
        @Override
        public boolean implies(Permission p) {
            return false;
        }

        public boolean check() {
            return this.community.getUsers().contains(null);
        }


}
