package de.librecarsharing;

/**
 * Created by fred on 04.07.17.
 */
public class CommunityNoRef {

    private DBCommunity dbCommunity;
    public CommunityNoRef(DBCommunity community){this.dbCommunity=community;}
    public String getName(){return this.dbCommunity.getName();}
    public long getId(){return this.dbCommunity.getId();}
    

}
