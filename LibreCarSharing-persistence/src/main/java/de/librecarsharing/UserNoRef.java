package de.librecarsharing;

/**
 * Created by fred on 30.06.17.
 */
public class UserNoRef {

    private DBUser dbUser;
    public UserNoRef(DBUser dbUser)
    {
        this.dbUser=dbUser;
    }
    public String getName()
    {
        return dbUser.getName();
    }

    public long getId()
    {

        return dbUser.getId();

    }
}

