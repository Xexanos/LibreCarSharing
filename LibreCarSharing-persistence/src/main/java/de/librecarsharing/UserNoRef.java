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

    public String getUsername() {return dbUser.getUsername();}
    public String getDisplayName() {return dbUser.getDisplayName();}
    public long getId() {return dbUser.getId();}
    public String getImageFile() {return dbUser.getImageFile();}
    public String getEmail(){return dbUser.getEmail();}
}

